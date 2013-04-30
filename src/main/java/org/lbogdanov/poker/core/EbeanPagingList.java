package org.lbogdanov.poker.core;

import java.util.List;

/**
 * A {@link PagingList} implementation, delegates on {@link com.avaje.ebean.PagingList}.
 * 
 * @author Alexandra Fomina
 */
public class EbeanPagingList<T> implements PagingList<T> {

    private com.avaje.ebean.PagingList<T> list;

    public EbeanPagingList(com.avaje.ebean.PagingList<T> pagingList) {
        this.list = pagingList;
    }

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

}
