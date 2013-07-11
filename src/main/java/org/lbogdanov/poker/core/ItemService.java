package org.lbogdanov.poker.core;

/**
 * A service to manipulate an {@link Item} instances and its estimates.
 * 
 * @author Alexandra Fomina
 */
public interface ItemService {

    /**
     * Deletes an item.
     * 
     * @param item the item to delete
     */
    public void delete(Item item);

    /**
     * Persists an item object in a storage.
     * 
     * @param item the item to save
     */
    public void save(Item item);

    /**
     * Approves a specified estimate.
     * 
     * @param estimate the estimate to approve
     */
    public void approve(Estimate estimate);

    /**
     * Returns a specified user estimate for a specified item.
     * 
     * @param user the estimate author
     * @param item the estimate item
     * @return the estimate
     */
    public Estimate find(User user, Item item);

}
