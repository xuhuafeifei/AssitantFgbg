package com.fgbg.com.fgbg.service.impl

import com.fgbg.com.fgbg.service.PdfService
import com.fgbg.entity.ByteArrayMultipartFile
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.utils.PdfMerger
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.UUID.randomUUID

@Service
class PdfServiceImpl : PdfService {

    override fun merge(preFile: MultipartFile, postFile: MultipartFile): ResponseEntity<Resource> {
        // 检查是否是 PDF 文件
        checkPdf(preFile)
        checkPdf(postFile)

        // 合并 PDF 到字节数组
        val mergedPdfBytes = mergePdfs(listOf(preFile, postFile))

        // 封装为 Resource 并返回
        val resource = ByteArrayResource(mergedPdfBytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"merged.pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body<Resource>(resource)
    }

    private fun checkPdf(file: MultipartFile) {
        if (!isPdf(file)) {
            throw IllegalArgumentException("只支持 PDF 文件")
        }
    }

    private fun isPdf(file: MultipartFile): Boolean {
        val contentType = file.contentType
        return contentType != null && contentType.equals("application/pdf", ignoreCase = true)
    }

    private fun mergePdfs(files: List<MultipartFile>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDoc = PdfDocument(pdfWriter)
        val pdfMerger = PdfMerger(pdfDoc)

        for (file in files) {
            val reader = PdfDocument(PdfReader(file.inputStream))
            pdfMerger.merge(reader, 1, reader.numberOfPages)
            reader.close()
        }

        pdfMerger.close()
        pdfDoc.close()

        return outputStream.toByteArray()
    }

    override fun cutFor(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource> {
        val pdfDoc = checkAndGetPdfDoc(file, start, end)

        val totalPages = pdfDoc.numberOfPages
        require(end <= totalPages) { "结束页不能超过总页数 $totalPages" }

        val outputStream = ByteArrayOutputStream()
        val writer = PdfWriter(outputStream)
        val newPdfDoc = PdfDocument(writer)

        pdfDoc.copyPagesTo(start, end, newPdfDoc)

        newPdfDoc.close()
        pdfDoc.close()

        val resource = ByteArrayResource(outputStream.toByteArray())

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"extracted_${start}_to_${end}_${randomUUID()}.pdf\""
            )
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource)
    }

    override fun cutWithout(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource> {
        val pdfDoc = checkAndGetPdfDoc(file, start, end)

        val totalPages = pdfDoc.numberOfPages
        require(end <= totalPages) { "结束页不能超过总页数 $totalPages" }

        val firstPart = extractPages(file, 1, start - 1) // 前半部分 (1..<start)
        val secondPart = extractPages(file, end + 1, totalPages) // 后半部分 (end+1..最后一页)

        return merge(firstPart, secondPart)
    }

    private fun checkAndGetPdfDoc(file: MultipartFile, start: Int, end: Int): PdfDocument {
        require(start > 0) { "起始页必须大于 0" }
        require(end >= start) { "结束页不能小于起始页" }

        val inputStream = file.inputStream
        val reader = PdfReader(inputStream)
        val pdfDoc = PdfDocument(reader)
        return pdfDoc
    }

    /**
     * 提取 PDF 的指定页范围
     */
    private fun extractPages(file: MultipartFile, start: Int, end: Int): MultipartFile {
        if (start > end) return emptyMultipartFile() // 如果范围无效，返回空文件

        val inputStream = file.inputStream
        val reader = PdfReader(inputStream)
        val pdfDoc = PdfDocument(reader)

        val outputStream = ByteArrayOutputStream()
        val writer = PdfWriter(outputStream)
        val newPdfDoc = PdfDocument(writer)

        pdfDoc.copyPagesTo(start, end, newPdfDoc)

        newPdfDoc.close()
        pdfDoc.close()

        return ByteArrayMultipartFile(
            "extracted.pdf",
            outputStream.toByteArray()
        )
    }

    /**
     * 创建一个空的 MultipartFile
     */
    private fun emptyMultipartFile(): MultipartFile {
        return ByteArrayMultipartFile(
            "empty.pdf",
            ByteArray(0)
        )
    }

}