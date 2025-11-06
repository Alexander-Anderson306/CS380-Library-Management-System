package com.cwu.library;
import java.util.HashMap;

class Item {
    //every item has a unique id (no need to store in the map)
    private int id;
    //attributes stored as key-value pairs in a map (more scalable :D)
    private HashMap<String, String> attributes;

    /**
     * Constructor for Item class. Initializes id and attributes map.
     * @param id The unique ID of the item.
     */
    public Item(int id) {
        this.id = id;
        this.attributes = new HashMap<>();
    }

    /**
     * Gets the unique ID of the item.
     * @return The unique ID of the item.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the item.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the value of the specified attribute.
     * @param key The attribute key.
     * @return The attribute value.
     */
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Sets the value of the specified attribute.
     * @param key The attribute key.
     * @param value The attribute value.
     */
    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    /**
     * Checks if the item has the specified attribute.
     * @param key The attribute key.
     * @return True if the item has the attribute; false otherwise.
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Initializes the item as a book with the specified attributes.
     * @param title Title of the book
     * @param year Publication year of the book
     * @param isbn ISBN number of the book
     * @param callNumber Call number of the book
     */
    public void initializeBook(String title, int year, int isbn, String callNumber) {
        setAttribute("title", title);
        setAttribute("year", Integer.toString(year));
        setAttribute("isbn", Integer.toString(isbn));
        setAttribute("callNumber", callNumber);
    }

    /**
     * Initializes the item as a CD with the specified attributes.
     * @param title Title of the CD
     * @param year Publication year of the CD
     * @param callNumber Call number of the CD
     */
    public void initializeCD(String title, int year, String callNumber) {
        setAttribute("title", title);
        setAttribute("year", Integer.toString(year));
        setAttribute("callNumber", callNumber);

    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Item ID: ").append(id);
        for(String key : attributes.keySet()) {
            sb.append(", ").append(key).append(": ").append(attributes.get(key));
        }
        return sb.toString();
    }

    public boolean equals(Item other) {
        return this.id == other.id;
    }
}