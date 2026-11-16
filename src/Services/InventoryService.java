package Services;
import model.InventoryItem;
import Repository.InventoryRepository;

public class InventoryService {
    private final InventoryRepository inventoryRepository;

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

    public void adjustStock(int inventoryId, int adjustment) {
        InventoryItem item = inventoryRepository.getInventoryItemById(inventoryId);
        item.adjustQuantity(adjustment);
    }

    
}
