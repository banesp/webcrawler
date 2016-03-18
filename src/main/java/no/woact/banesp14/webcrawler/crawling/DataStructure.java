package no.woact.banesp14.webcrawler.crawling;

/**
 * This interface serves as a wrapper for Datastructures with core-methods as below.
 * Since the datastructes insert and remove methods is abstracted,
 * it gives MyEngine better reusability because the class
 * does no longer need to handle 2 different cases
 * of searches. Because this is now directed by the datastructure itself.
 */
public interface DataStructure {
    /**
     * Inserts the parameter string (which holds a URL).
     */
    void insert(String link);

    /**
     * Removes the first element in the datastructure.
     * Then returns it to the caller.
     */
    String extract();

    /**
     * Returns a boolean telling if the datastructure is empty.
     */
    boolean isEmpty();
}
