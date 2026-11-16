import java.util.*;


import model.InventoryItem;
public class InventoryRepository {
    
    private final Map<Integer, InventoryItem> inventory = new HashMap<>();

    public void save(InventoryItem inventoryItem) {
        if(inventoryItem == null) {
            throw new IllegalArgumentException("Inventory item cannot be null");
        }
        int id = inventoryItem.getInventoryId();

        if (inventory.containsKey(id)) {
            throw new IllegalArgumentException("Inventory item with ID " + id + " already exists");
        }

        inventory.put(inventoryItem.getInventoryId(), inventoryItem);
    }

    public InventoryItem getInventoryItemById(int inventoryId) {
        if (inventoryId <= 0) {
            throw new IllegalArgumentException("Inventory ID must be positive");
        }
        InventoryItem item = inventory.get(inventoryId);
        if (item == null) {
            throw new IllegalArgumentException("Inventory item with ID " + inventoryId + " does not exist");
        }
        return item;
    }

    public List<InventoryItem> findDonationByDonationId(int donationId) {
        List<InventoryItem> result = new ArrayList<>();
        for (InventoryItem item : inventory.values()) {
            if (item.getDonationId() == donationId) {
                result.add(item);
            }
        }
        return result;
    }

    public List<InventoryItem> getAllInventoryItems() {
        return new ArrayList<>(inventory.values());
    }

    public void deleteInventoryItem(int inventoryId) {
        if (inventoryId <= 0) {
            throw new IllegalArgumentException("Inventory ID must be positive");
        }
        if (!inventory.containsKey(inventoryId)) {
            throw new IllegalArgumentException("Inventory item with ID " + inventoryId + " does not exist");
        }
        inventory.remove(inventoryId);
    }

}
