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
package org.lbogdanov.poker.core.impl;

import java.util.List;

import org.lbogdanov.poker.core.PagingList;


/**
 * A {@link PagingList} implementation, simply delegates to {@link com.avaje.ebean.PagingList}.
 * 
 * @author Alexandra Fomina
 */
class EbeanPagingList<T> implements PagingList<T> {

    private com.avaje.ebean.PagingList<T> list;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAsList() {
        return list.getAsList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPageSize() {
        return list.getPageSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalRowCount() {
        return list.getTotalRowCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalPageCount() {
        return list.getTotalPageCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getPage(int i) {
        return list.getPage(i).getList();
    }

    EbeanPagingList(com.avaje.ebean.PagingList<T> pagingList) {
        this.list = pagingList;
    }

}
