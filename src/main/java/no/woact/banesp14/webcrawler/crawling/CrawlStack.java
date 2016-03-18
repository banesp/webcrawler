package no.woact.banesp14.webcrawler.crawling;

import edu.princeton.cs.algorithms.LinkedStack;

public class CrawlStack implements DataStructure {

    private LinkedStack<String> linkedStack;

    public CrawlStack() {
        linkedStack = new LinkedStack();
    }

    @Override
    public void insert(String link) {
        linkedStack.push(link);
    }

    @Override
    public String extract() {
        return linkedStack.pop();
    }

    @Override
    public boolean isEmpty() {
        return linkedStack.isEmpty();
    }
}
