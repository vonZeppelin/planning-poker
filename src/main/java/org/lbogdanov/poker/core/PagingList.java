package org.lbogdanov.poker.core;

import java.util.List;

/**
 * Represents a list used to page through a set of items.
 * 
 * @author Alexandra Fomina
 */
public interface PagingList<T> {

    /**
     * Returns all pages as a single list.
     * 
     * @return the list
     */
    public List<T> getAsList();

    /**
     * Returns page size.
     * 
     * @return page size
     */
    public int getPageSize();

    /**
     * Returns total row count.
     * 
     * @return total row count
     */
    public int getTotalRowCount();

    /**
     * Returns total page count.
     * 
     * @return total page count
     */
    public int getTotalPageCount();

    /**
     * Returns a page with specified number as <code>List</code>.
     * 
     * @param i the page number
     * @return the page
     */
    public List<T> getPage(int i);

}
