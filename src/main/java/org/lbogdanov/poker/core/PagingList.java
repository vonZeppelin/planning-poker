/**
 * Copyright 2012 Leonid Bogdanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lbogdanov.poker.core;

import java.util.List;


/**
 * Represents a list used to page through a set of items.
 * 
 * @author Alexandra Fomina
 */
public interface PagingList<T> {

    /**
     * Returns all pages in a single list.
     * 
     * @return the list with data
     */
    public List<T> getAsList();

    /**
     * Returns page size.
     * 
     * @return the number of rows per page
     */
    public int getPageSize();

    /**
     * Returns total row count.
     * 
     * @return the total row count
     */
    public int getTotalRowCount();

    /**
     * Returns total page count.
     * 
     * @return total page count
     */
    public int getTotalPageCount();

    /**
     * Returns a page with specified number as a <code>List</code>.
     * 
     * @param i the page number, starting at 0
     * @return the page
     */
    public List<T> getPage(int i);

}
