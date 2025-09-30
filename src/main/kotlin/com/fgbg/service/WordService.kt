package com.fgbg.com.fgbg.service

import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface WordService {
    fun merge(preFile: MultipartFile, postFile: MultipartFile): ResponseEntity<Resource>

    fun cutFor(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource>

    fun cutWithout(file: MultipartFile, start: Int, end: Int): ResponseEntity<Resource>
}