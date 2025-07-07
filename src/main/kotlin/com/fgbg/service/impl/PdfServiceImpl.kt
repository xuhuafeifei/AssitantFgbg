package com.fgbg.com.fgbg.service.impl

import com.fgbg.com.fgbg.service.PdfService
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.utils.PdfMerger
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@Service
class PdfServiceImpl: PdfService {

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
            val reader = PdfDocument(com.itextpdf.kernel.pdf.PdfReader(file.inputStream))
            pdfMerger.merge(reader, 1, reader.numberOfPages)
            reader.close()
        }

        pdfMerger.close()
        pdfDoc.close()

        return outputStream.toByteArray()
    }

}