package no.woact.banesp14.webcrawler.crawling;

import edu.princeton.cs.introcs.In;

import java.util.*;

public class MyEngine implements SearchEngine {

    private static final int DEFAULT_SIZE = 5000;
    private static final int MAX_LENGTH_FOR_LINKS = 50;

    private Map<String, HashSet<String>> wordToLinks;
    private Set<String> allowedWords;
    private Set<String> visitedLinks;
    private DataStructure linksToVisit;
    private WebPageReader reader;
    private int max;
    private boolean isBreadthFirst;
    private int size;

    public MyEngine() {
        this(DEFAULT_SIZE);
    }

    public MyEngine(int max) {
        wordToLinks = new HashMap();
        visitedLinks = new HashSet();
        allowedWords = new HashSet();
        setMax(max);
        isBreadthFirst = true;
        fetchAllowedWordsFromFile();
    }

    public String[] searchHits(String target) {
       if (wordToLinks.containsKey(target)) {
           return wordToLinks.get(target).toArray(new String[0]);
        } else  {
            return new String[0];
        }
    }

    public void crawlFrom(String webAddress) {
        if (isBreadthFirst) {
            linksToVisit = new CrawlQueue();
        } else {
            linksToVisit = new CrawlStack();
        }
        crawl(webAddress);
        collectGarbage();
    }

    private void crawl(String webAddress) {
        linksToVisit.insert(webAddress);

        while (size() < max && !linksToVisit.isEmpty()) {
            webAddress = linksToVisit.extract();
            reader = new WebPageReader(webAddress);

            handleLinks();
            addWordsToMap(webAddress);
        }
    }

    private void addWordsToMap(String webAddress) {
        for (String w : reader.getWords()) {
            if (size() >= max) return;

            if (wordToLinks.containsKey(w)) {
                wordToLinks.get(w).add(webAddress); // Add URL to existing word in Map
                size++;
            } else if (allowedWords.contains(w)) {
                HashSet<String> newSet = new HashSet();
                newSet.add(webAddress);
                wordToLinks.put(w, newSet); // Add new word with URLs to Map
                size++;
            }
        }
    }

    private void handleLinks() {
        for (String link : reader.getLinks()) {
            if (link.length() < MAX_LENGTH_FOR_LINKS && !visitedLinks.contains(link)) {
                visitedLinks.add(link);
                linksToVisit.insert(link);
            }
        }
    }

    private void fetchAllowedWordsFromFile() {
        In words = new In("/words.txt");
        In stopwords = new In("/stopwords.txt");
        Set<String> stopwordSet = new HashSet();

        while (stopwords.hasNextLine()) {
            stopwordSet.add(stopwords.readLine());
        }
        stopwords.close();

        while (words.hasNextLine()) {
            String word = words.readLine();

            if (!stopwordSet.contains(word)) {
                allowedWords.add(word);
            }
        }
        words.close();
    }

    private void collectGarbage() {
        allowedWords = null;
        visitedLinks = null;
        linksToVisit = null;
        reader = null;
        System.gc();
    }

    public void setMax(int max){
        this.max = max;
    }

    public boolean setBreadthFirst(){
        isBreadthFirst = true;
        return true;
    }

    public boolean setDepthFirst(){
        isBreadthFirst = false;
        return true;
    }

    public int size(){
        return size;
    }
}
