package com.fgbg.entity

import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class ByteArrayMultipartFile(
    private val name: String,
    private val content: ByteArray
) : MultipartFile {
    override fun getName(): String = name
    override fun getOriginalFilename(): String = name
    override fun getContentType(): String? = "application/pdf"
    override fun isEmpty(): Boolean = content.isEmpty()
    override fun getSize(): Long = content.size.toLong()
    override fun getBytes(): ByteArray = content
    override fun getInputStream(): InputStream = ByteArrayInputStream(content)
    override fun transferTo(dest: File) = throw UnsupportedOperationException()
}