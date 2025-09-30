package com.fgbg.service.impl

import com.fgbg.com.fgbg.service.WordService
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

@Service
class WordServiceImpl : WordService {

    // 合并两个Word文档
    override fun merge(preFile: MultipartFile, postFile: MultipartFile): ResponseEntity<Resource> {
        // 读取两个文档
        val preDoc = XWPFDocument(preFile.inputStream)
        val postDoc = XWPFDocument(postFile.inputStream)

        // 创建新文档用于合并内容
        val mergedDoc = XWPFDocument()

        // 复制第一个文档的内容
        for (paragraph in preDoc.paragraphs) {
            val newParagraph = mergedDoc.createParagraph()
            newParagraph.createRun().setText(paragraph.text)
        }

        // 复制第二个文档的内容
        for (paragraph in postDoc.paragraphs) {
            val newParagraph = mergedDoc.createParagraph()
            newParagraph.createRun().setText(paragraph.text)
        }

        // 转换为字节数组
        val outputStream = ByteArrayOutputStream()
        mergedDoc.write(outputStream)
        mergedDoc.close()

        // 准备响应
        val resource = ByteArrayResource(outputStream.toByteArray())
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged.docx") // 设置下载文件名
            .contentType(MediaType.APPLICATION_OCTET_STREAM) // 设置内容类型
            .body(resource)
    }

    // 截取指定范围的段落(包含开始和结束段落)
    override fun cutFor(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource> {
        val doc = XWPFDocument(file.inputStream)
        val newDoc = XWPFDocument()

        // 只复制指定范围内的段落
        for (i in start..end) {
            if (i < doc.paragraphs.size) { // 检查索引是否有效
                val paragraph = doc.paragraphs[i]
                val newParagraph = newDoc.createParagraph()
                newParagraph.createRun().setText(paragraph.text)
            }
        }

        // 转换为字节数组
        val outputStream = ByteArrayOutputStream()
        newDoc.write(outputStream)
        newDoc.close()
        doc.close()

        val resource = ByteArrayResource(outputStream.toByteArray())
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cut_for.docx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

    // 截取除指定范围外的所有段落
    override fun cutWithout(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource> {
        val doc = XWPFDocument(file.inputStream)
        val newDoc = XWPFDocument()

        // 复制所有不在指定范围内的段落
        for (i in 0 until doc.paragraphs.size) {
            if (i < start || i > end) { // 只保留范围外的段落
                val paragraph = doc.paragraphs[i]
                val newParagraph = newDoc.createParagraph()
                newParagraph.createRun().setText(paragraph.text)
            }
        }

        // 转换为字节数组
        val outputStream = ByteArrayOutputStream()
        newDoc.write(outputStream)
        newDoc.close()
        doc.close()

        val resource = ByteArrayResource(outputStream.toByteArray())
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cut_without.docx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}