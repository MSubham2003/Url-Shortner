package com.url.shortner.controller;

import com.url.shortner.dto.ClickEventDto;
import com.url.shortner.dto.UrlMappingDto;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;

    // request -> {"originalUrl":"https://google.com"}
    @PostMapping("/short")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDto urlMappingDto = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDto);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDto> url = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDto>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
//        2011-12-03T10:15:30
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endDate, dateTimeFormatter);
        List<ClickEventDto> clickEventDtos= urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDtos);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Principal principal) {
        User user = userService.findByUsername(principal.getName());
//        2011-12-03T10:15:30
        System.out.println(startDate+"--"+endDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);
        Map<LocalDate, Long> clickCount= urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        return ResponseEntity.ok(clickCount);
    }
}
