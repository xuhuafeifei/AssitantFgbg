package com.fgbg.com.fgbg.controller

import com.fgbg.com.fgbg.service.PdfService
import com.fgbg.common.GlobalExceptionHandler
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

@RestController
class PdfController {

    companion object {
        private val log = LoggerFactory.getLogger(PdfController::class.java)
    }

    @Resource
    private lateinit var pdfService: PdfService

    @PostMapping("/merge")
    fun merge(
        @RequestPart("preFile") preFile: MultipartFile,
        @RequestPart("postFile") postFile: MultipartFile
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("merge")
        val responseEntity = pdfService.merge(preFile, postFile)
        return responseEntity
    }

    @PostMapping("/cutFor")
    fun cutFor(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("startPage") startPage: Int,
        @RequestPart("endPage") endPage: Int
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("cutFor")
        val responseEntity = pdfService.cutFor(file, startPage, endPage)
        return responseEntity
    }

    @PostMapping("/cutWithout")
    fun cutWithout(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("startPage") startPage: Int,
        @RequestPart("endPage") endPage: Int
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("cutWithout")
        val responseEntity = pdfService.cutWithout(file, startPage, endPage)
        return responseEntity
    }
}