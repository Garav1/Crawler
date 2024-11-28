package com.crawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DFSCrawler {

    public static void liveCrawl(String url, int maxDepth, SseEmitter emitter) {
        Set<String> visited = new HashSet<>();
        dfs(url, visited, 0, maxDepth, emitter);
    }

    private static void dfs(String url, Set<String> visited, int depth, int maxDepth, SseEmitter emitter) {
        if (depth > maxDepth || visited.contains(url)) {
            return;
        }

        try {
            visited.add(url);
            emitter.send(url); // Send URL to client in real-time

            Document doc = Jsoup.connect(url)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.absUrl("href");
                if (!visited.contains(href) && href.startsWith("http")) {
                    dfs(href, visited, depth + 1, maxDepth, emitter);
                }
            }
        } catch (IOException e) {
            try {
                emitter.send("Error crawling URL: " + url + " - " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
