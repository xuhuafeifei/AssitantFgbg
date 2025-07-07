package com.fgbg.com.fgbg.service

import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface PdfService {
    fun merge(preFile: MultipartFile, postFile: MultipartFile): ResponseEntity<Resource>
}