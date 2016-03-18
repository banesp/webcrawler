package no.woact.banesp14.webcrawler.crawling;

import edu.princeton.cs.algorithms.LinkedQueue;

public class CrawlQueue implements DataStructure {

    private LinkedQueue<String> linkedQueue;

    public CrawlQueue() {
        linkedQueue = new LinkedQueue();
    }

    @Override
    public void insert(String link) {
        linkedQueue.enqueue(link);
    }

    @Override
    public String extract() {
        return linkedQueue.dequeue();
    }

    @Override
    public boolean isEmpty() {
        return linkedQueue.isEmpty();
    }
}
