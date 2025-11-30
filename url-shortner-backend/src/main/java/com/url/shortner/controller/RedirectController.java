package com.url.shortner.controller;

import com.url.shortner.models.UrlMapping;
import com.url.shortner.service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class RedirectController {
    private UrlMappingService urlMappingService;

    private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
        logger.info("Request hit in Redirect controller for redirect with received short url {}", shortUrl);
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
        if(urlMapping != null){
            logger.info("Received short url found in DB");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", urlMapping.getOriginalUrl());
            return ResponseEntity.status(302).headers(httpHeaders).build();
        } else{
            logger.info("Received short url not found in DB");
            return ResponseEntity.notFound().build();
        }
    }
}
