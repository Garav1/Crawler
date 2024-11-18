package com.minorproject.webcrawler;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DFSCrawler {

    public static List<String> crawl(String url, int maxDepth) {
        List<String> crawledUrls = new ArrayList<>();
        crawl(url, crawledUrls, 0, maxDepth);
        return crawledUrls;
    }

    private static void crawl(String url, List<String> crawledUrls, int depth, int maxDepth) {
        if (depth > maxDepth) return;
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .get();
            crawledUrls.add(url);

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.absUrl("href");
                if (!crawledUrls.contains(href)) {
                    crawl(href, crawledUrls, depth + 1, maxDepth);
                }
            }
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 403) {
                System.out.println("Skipping URL: " + url + " due to HTTP error 403");
            } else {
                System.out.println("HTTP error fetching URL. Status=" + e.getStatusCode() + ", URL=[" + url + "]");
            }
        } catch (UnsupportedMimeTypeException e) {
            System.out.println("Unsupported mime type error for URL=[" + url + "]");
        } catch (IOException e) {
            System.out.println("Error occurred during crawling: " + e.getMessage());
        }
    }
}