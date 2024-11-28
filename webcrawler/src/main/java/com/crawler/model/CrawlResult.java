package com.crawler.model;

import java.util.List;

public class CrawlResult {
    private long executionTime;
    private List<String> urls;

    // Default constructor (required for frameworks like Spring)
    public CrawlResult() {
    }

    // Parameterized constructor
    public CrawlResult(long executionTime, List<String> urls) {
        this.executionTime = executionTime;
        this.urls = urls;
    }

    // Getters and Setters
    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}