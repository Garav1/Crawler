package com.minorproject.webcrawler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebcrawlerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testDFSCrawl() {
        List<String> urls = DFSCrawler.crawl("http://example.com", 3);
        assertNotNull(urls);
        assertFalse(urls.isEmpty());
    }

    @Test
    void testBFSCrawl() {
        List<String> urls = BFSCrawler.crawl("http://example.com", 3);
        assertNotNull(urls);
        assertFalse(urls.isEmpty());
    }
}