package no.woact.banesp14.webcrawler.crawling;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class WebPageReader implements Runnable {

    public static final String UTF = "UTF-8";
    public static final String WESTERN = "ISO-8859-1";

    private static final Pattern START_BODY = Pattern.compile ("<body");
    private static final Pattern END_BODY = Pattern.compile ("</body");
    private static final Pattern WORD_DELIMITER = Pattern.compile ("[^\\p{Alpha}]+");

    private String link;
    private URL parent = null;
    private URL url = null;
    private String encoding;
    private HashSet < String > links = null;
    private HashSet < String > words = null;

    public WebPageReader (String theUrl) {
        this (theUrl, null, WESTERN);
    }

    public void run () {
        if (!alreadyDownloaded ())
            download ();
    }

    public Set < String > getLinks () {
        run ();
        return links;
    }

    public Set < String > getWords () {
        run ();
        return words;
    }

    /**
     * If you encounter problems related to the encoding, 
     * this may be of some help.
     */
    public void setEncoding (String theEncoding) {
        this.encoding = theEncoding;
    }

    public String toString () {
        return link;
    }

    /**
     * This method interprets the given
     * link within the context of this web page.
     */
    private WebPageReader (String theLink, URL theParent, String theEncoding) {
        link = theLink;
        url = establishURL(theParent,link);
        if (url != null)
           link = url.toString();

        encoding = theEncoding;
    }

    private String getAsChild (String theLink) {
        URL url = establishURL(this.url,theLink);
        if (url == null)
            return "invalid link";
        else
            return url.toString();
    }

    private void download () {
        links = new HashSet < String > ();
        words = new HashSet < String > ();

        if (url != null) {
            String contents = getContents (url, encoding);
            collectLinks (contents, links);
            collectWords (contents, words);
        }
    }

    private static URL establishURL(URL parent, String link) {
        try {
            return new URL (parent, link);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private boolean alreadyDownloaded () {
        return links != null;
    }

    private static String getContents (URL url, String encoding) {
        if (url == null)
            return "";

        Scanner scanner = null;

        try {
            scanner =
                new Scanner (new
                             BufferedInputStream (url.
                                                  openConnection ().getInputStream
                                                  ()), encoding);
        } catch (IOException e) {
            return "";
        }

        if (!scanner.hasNextLine ()) {
            return "";
        }

        scanner.useDelimiter (START_BODY);
        scanner.next ();
        scanner.useDelimiter (END_BODY);
        try {
            return scanner.next ().toLowerCase ();
        } catch (NoSuchElementException e) {
            return "";
        }
    }


    private void collectLinks (String contents, Set < String > set) {

        int off = 9;
        int begin = 0;
        int end = begin;

        while (true) {
            begin = contents.indexOf ("<a href=\"", end);
            end = contents.indexOf ("\"", begin + off);

            if (begin == -1 || end == -1) {break;}

            String possibleLink = contents.substring (begin + off, end);
            possibleLink = getAsChild(possibleLink);
            if (!(possibleLink.contains ("mailto") || possibleLink.contains ("#"))) {
                set.add (possibleLink);
            }
        }
    }

    private static void collectWords (String contents, Set < String > set) {
        Scanner sc = new Scanner (contents);
        sc.useDelimiter(WORD_DELIMITER);
        while (sc.hasNext ()) {
            String word = sc.next ();
            set.add (word);
        }
    }
}
