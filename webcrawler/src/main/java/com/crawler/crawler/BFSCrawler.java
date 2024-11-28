package com.crawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BFSCrawler {

    public static void liveCrawl(String startUrl, int maxDepth, SseEmitter emitter) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startUrl);

        while (!queue.isEmpty() && visited.size() < maxDepth) {
            String url = queue.poll();
            if (visited.contains(url)) {
                continue;
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
                        queue.add(href);
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
}
