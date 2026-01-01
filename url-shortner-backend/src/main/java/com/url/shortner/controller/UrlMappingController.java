package com.url.shortner.controller;

import com.google.gson.Gson;
import com.url.shortner.dto.ClickEventDto;
import com.url.shortner.dto.UrlMappingDto;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/urls")
@CrossOrigin
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;
    private Gson gson;


    private static final Logger logger = LoggerFactory.getLogger(UrlMappingController.class);

    // request -> {"originalUrl":"https://google.com"}
    @PostMapping("/short")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {
        logger.info("Request Hit in controller for createShortUrl with received request {}", request);
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDto urlMappingDto = urlMappingService.createShortUrl(originalUrl, user);
        logger.info("Response returned for createShortUrl is {}", urlMappingDto);
        return ResponseEntity.ok(urlMappingDto);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> getUserUrls(Principal principal) {
        logger.info("Request Hit in controller for getUserUrls");
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDto> url = urlMappingService.getUrlsByUser(user);
        logger.info("Response returned for getUserUrls is {}", url);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDto>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
//        2011-12-03T10:15:30
        logger.info("Request Hit in controller for get analytics of a specific url. Requested shorturl: {}, Start date {} & End date {}", shortUrl, startDate, endDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endDate, dateTimeFormatter);
        List<ClickEventDto> clickEventDtos= urlMappingService.getClickEventsByDate(shortUrl, start, end);
        logger.info("Response returned is {}", clickEventDtos);
        return ResponseEntity.ok(clickEventDtos);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Principal principal) {
        logger.info("Request hit in controller for get total click by date. startdate {} & enddate {}", startDate, endDate);
        User user = userService.findByUsername(principal.getName());
//        2011-12-03T10:15:30
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);
        Map<LocalDate, Long> clickCount= urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        logger.info("Response returned is {}", clickCount);
        return ResponseEntity.ok(clickCount);
    }
}
