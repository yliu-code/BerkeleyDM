package bearmaps.utils.ps;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// resources:
// Trie code from Spring’s optional textbook.
// Spring Lecture 21 slides.
// Tries from pages 173 to 180 of Data Structures Into Java, from Spring.

public class TrieSet {
    private Node root;

    public TrieSet() {
        root = new Node();
    }

    /**
     * Clears all items out of Trie
     */
    public void clear() {
        root = new Node();
    }

    public List<Map<String, Object>> getLoc(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (key.equals("")) {
            Node curr = root;
            char c = '\u0000';
            curr = curr.map.get(c);
            return curr.locations;
        } else {
            Node x = get(root, key);
            if (x != null && x.isKey) {
                return x.locations;
            }
        }
        return new LinkedList<>();
    }

    public char get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = get(root, key);
        if (x == null) {
            return '\u0000';
        }
        return x.ch;
    }


    private Node get(Node x, String key) {
        if (x == null) {
            return null;
        }
        if (key.equals("")) {
            return x;
        }
        char c = key.charAt(0);
        return get(x.map.get(c), key.substring(1));
    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     */
    public boolean contains(String key) {
        Node currN = get(root, key);
        return currN != null && currN.isKey;
    }

    /**
     * Inserts string KEY into Trie
     */
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    public void addLocation(String key, HashMap<String, Object> location) {
        if (key != null && key.equals("")) {
            Node curr = root;
            char c = '\u0000';
            if (curr.map.get(c) == null) {
                curr.map.put(c, new Node(c, true));
            }
            curr = curr.map.get(c);
            curr.isKey = true;
            curr.addLocation(location);
        }
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
        curr.addLocation(location);
    }

    /**
     * Returns a list of all words that start with PREFIX
     */
    public List<String> keysWithPrefix(String prefix) {
        List<String> results = new LinkedList<>();
        Node x = get(root, prefix);
        collect(x, results);
        return results;
    }


    private void collect(Node x, List<String> results) {
        if (x == null) {
            return;
        }
        if (x.ch != '\u0000' && x.isKey) {
            for (Map<String, Object> location : x.locations) {
                results.add((String) location.get("name"));
            }
        }
        for (char c : x.map.keySet()) {
            collect(x.map.get(c), results);
        }
    }

    /**
     * Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 18. If you don't implement this, throw an
     * UnsupportedOperationException.
     */

    private static class Node {
        private char ch;
        private boolean isKey;
        private HashMap<Character, Node> map;
        private List<Map<String, Object>> locations;

        private Node() {
            this.map = new HashMap<>();
            this.locations = new LinkedList<>();
        }

        private Node(char c, boolean b) {
            this.ch = c;
            this.isKey = b;
            this.map = new HashMap<>();

        }

        private void addLocation(HashMap<String, Object> location) {
            if (locations == null) {
                this.locations = new LinkedList<>();
            }
            locations.add(location);
        }
    }

}
