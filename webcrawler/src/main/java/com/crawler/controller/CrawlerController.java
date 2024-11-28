package com.crawler.controller;

import com.crawler.crawler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class CrawlerController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping(value = "/api/crawl/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter startLiveCrawl(@RequestParam String url, @RequestParam String algorithm) {
        SseEmitter emitter = new SseEmitter();

        executor.submit(() -> {
            try {
                logger.info("Starting live crawl for URL: {}", url);
                if ("dfs".equalsIgnoreCase(algorithm)) {
                    DFSCrawler.liveCrawl(url, 100, emitter);
                } else if ("bfs".equalsIgnoreCase(algorithm)) {
                    BFSCrawler.liveCrawl(url, 100, emitter);
                } else {
                    emitter.send("Invalid algorithm specified");
                }
                emitter.complete();
            } catch (Exception e) {
                logger.error("Error during crawling", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
