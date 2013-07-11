package org.lbogdanov.poker.core.impl;

import javax.inject.Inject;

import org.lbogdanov.poker.core.*;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.annotation.Transactional;

/**
 * An {@link ItemService} interface implementation.
 * 
 * @author Alexandra Fomina
 */
public class ItemServiceImpl implements ItemService {

    @Inject
    private EbeanServer ebean;
    @Inject
    private UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Item item) {
        ebean.delete(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save(Item item) {
        ebean.save(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void approve(Estimate estimate) {
        if (estimate.getUser() == null) {
            estimate.setUser(userService.getCurrentUser());
        }
        ebean.save(estimate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Estimate find(User user, Item item) {
        return ebean.find(Estimate.class).where().eq("user", user).eq("item", item).findUnique();
    }

}
