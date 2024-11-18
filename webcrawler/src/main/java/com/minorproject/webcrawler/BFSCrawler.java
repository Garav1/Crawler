package com.minorproject.webcrawler;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSCrawler {

    public static List<String> crawl(String url, int maxDepth) {
        List<String> crawledUrls = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(url);

        int currentDepth = 0;
        while (!queue.isEmpty() && currentDepth <= maxDepth) {
            String currentUrl = queue.poll();
            if (crawledUrls.contains(currentUrl)) {
                continue;
            }

            crawledUrls.add(currentUrl);

            try {
                Document doc = Jsoup.connect(currentUrl)
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                                    .get();
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String href = link.absUrl("href");
                    if (!crawledUrls.contains(href)) {
                        queue.add(href);
                    }
                }
            } catch (HttpStatusException e) {
                System.out.println("HTTP error fetching URL. Status=" + e.getStatusCode() + ", URL=[" + currentUrl + "]");
            } catch (UnsupportedMimeTypeException e) {
                System.out.println("Unsupported mime type error for URL=[" + currentUrl + "]");
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL: " + currentUrl);
            } catch (IOException e) {
                System.out.println("Error occurred during crawling: " + e.getMessage());
            }
            currentDepth++;
        }

        return crawledUrls;
    }
}