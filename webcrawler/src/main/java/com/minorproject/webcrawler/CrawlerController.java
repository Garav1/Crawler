package com.minorproject.webcrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CrawlerController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    @PostMapping("/api/crawl/start")
    public ResponseEntity<CrawlResult> startCrawl(@RequestParam String url, @RequestParam String algorithm) {
        if (!isValidUrl(url)) {
            return ResponseEntity.badRequest().body(new CrawlResult(0, List.of("Invalid URL")));
        }

        try {
            logger.info("Received URL: {}", url);
            logger.info("Received Algorithm: {}", algorithm);

            List<String> crawledUrls;
            long startTime = System.currentTimeMillis();

            if ("dfs".equalsIgnoreCase(algorithm)) {
                crawledUrls = DFSCrawler.crawl(url, 100);
            } else if ("bfs".equalsIgnoreCase(algorithm)) {
                crawledUrls = BFSCrawler.crawl(url, 100);
            } else {
                return ResponseEntity.badRequest().body(new CrawlResult(0, List.of("Invalid algorithm specified")));
            }

            long executionTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(new CrawlResult(executionTime, crawledUrls));
        } catch (Exception e) {
            logger.error("Error during crawling", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new CrawlResult(0, List.of("Error occurred during crawling")));
        }
    }

    private boolean isValidUrl(String url) {
        return url != null && url.startsWith("http");
    }
}

class CrawlResult {
    private long executionTime;
    private List<String> urls;

    public CrawlResult(long executionTime, List<String> urls) {
        this.executionTime = executionTime;
        this.urls = urls;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public List<String> getUrls() {
        return urls;
    }
}