package com.fgbg.com.fgbg.controller

import com.fgbg.com.fgbg.service.PdfService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

@RestController
class PdfController {

    @Resource
    private lateinit var pdfService: PdfService

    @PostMapping("/merge")
    fun merge(
        @RequestPart("preFile") preFile: MultipartFile,
        @RequestPart("postFile") postFile: MultipartFile
    ): ResponseEntity<org.springframework.core.io.Resource> {
        println("merge")
        val responseEntity = pdfService.merge(preFile, postFile)
        return responseEntity
    }
}