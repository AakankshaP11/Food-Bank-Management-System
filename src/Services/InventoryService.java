package Services;
import model.InventoryItem;
import common.InventoryChangeType;
import Repository.InventoryRepository;

public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final List<InventoryHistory> history = new ArrayList<>();
    
    public InventoryService(InventoryRepository inventoryRepository) {
        if(inventoryRepository == null) {
            throw new IllegalArgumentException("InventoryRepository cannot be null");
        }
        this.inventoryRepository = inventoryRepository;
    }

    public void addInventoryItem(InventoryItem inventoryItem) {
        if(inventoryItem == null) {
            throw new IllegalArgumentException("Inventory item cannot be null");
        }
        inventoryRepository.save(inventoryItem);
    }

    public void adjustStock(int inventoryId, int adjustment, InventoryChangeType changeType) {
        if (inventoryId <= 0) {
            throw new IllegalArgumentException("Inventory ID must be positive");
        }
        if (changeType == null) {
            throw new IllegalArgumentException("Change type cannot be null");
        }
        InventoryItem item = inventoryRepository.getInventoryItemById(inventoryId);
        item.adjustQuantity(adjustment);
        history.add(new InventoryHistory(inventoryId, adjustment, changeType));
    }


}
