package com.fgbg.com.fgbg.controller

import com.fgbg.com.fgbg.service.WordService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

@RestController
class WordController {

    companion object {
        private val log = LoggerFactory.getLogger(WordController::class.java)
    }

    @Resource
    private lateinit var wordService: WordService

    @PostMapping("/word/merge")
    fun merge(
        @RequestPart("preFile") preFile: MultipartFile,
        @RequestPart("postFile") postFile: MultipartFile
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("word merge")
        val responseEntity = wordService.merge(preFile, postFile)
        return responseEntity
    }

    @PostMapping("/word/cutFor")
    fun cutFor(
        @RequestPart("file") file: MultipartFile,
        @RequestParam("startPage") startPage: Int,
        @RequestParam("endPage") endPage: Int
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("word cutFor")
        val responseEntity = wordService.cutFor(file, startPage, endPage)
        return responseEntity
    }

    @PostMapping("/word/cutWithout")
    fun cutWithout(
        @RequestPart("file") file: MultipartFile,
        @RequestParam("startPage") startPage: Int,
        @RequestParam("endPage") endPage: Int
    ): ResponseEntity<org.springframework.core.io.Resource> {
        log.info("word cutWithout")
        val responseEntity = wordService.cutWithout(file, startPage, endPage)
        return responseEntity
    }
}
