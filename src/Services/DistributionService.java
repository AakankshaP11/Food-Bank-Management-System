package Services;

import java.util.HashMap;
import java.util.Map;

import Repository.DistributionRepository;
import Repository.FoodHamperRequestRepository;
import Repository.InventoryRepository;
import common.HamperRequestStatus;
import common.InventoryChangeType;
import common.PickupStatus;
import model.Distribution;
import model.DistributionItem;
import model.FoodHamperRequest;
import model.InventoryItem;

public class DistributionService {
    private final DistributionRepository distributionRepository;
    private final FoodHamperRequestRepository hamperRequestRepository;
    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;
    
    public DistributionService(DistributionRepository distributionRepository, FoodHamperRequestRepository hamperRequestRepository, InventoryService inventoryService, InventoryRepository inventoryRepository) {
       if (distributionRepository == null || hamperRequestRepository == null || inventoryService == null || inventoryRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.distributionRepository = distributionRepository;
        this.hamperRequestRepository = hamperRequestRepository;
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    public void validateDistribution(int distributionId) {
        if (distributionId <= 0) {
            throw new IllegalArgumentException("Distribution ID must be positive");
        }

        Distribution distribution = distributionRepository.findById(distributionId);

        FoodHamperRequest request = hamperRequestRepository.findById(distribution.getRequestId());

        if (distribution.getPickupStatus() == PickupStatus.FULFILLED) {
            throw new IllegalStateException(" Distribution is already fulfilled");
        }

        if(request.getStatus() != HamperRequestStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot validate distribution for a request that is not scheduled");
        }
        if (distribution.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot validate an empty distribution");
        }
    }

    public void validateInventory(int distributionId) {
        Distribution distribution = distributionRepository.findById(distributionId);

        Map<Integer, Integer> requiredQuantities = new HashMap<>();

        for (DistributionItem item : distribution.getItems()) {
            int id = item.getInventoryId();
            int quantity = item.getQuantity();
            int previous = requiredQuantities.getOrDefault(id, 0);
            int totalRequired = previous + quantity;
            InventoryItem stock = inventoryRepository.getInventoryItemById(id);

            if (stock == null) {
                throw new IllegalStateException(
                    "Inventory item not found: " + id
                );
            }
            if (stock.getQuantity() < totalRequired) {
                throw new IllegalStateException("Insufficient stock for inventory item ID: " + id);
            }
            requiredQuantities.put(id, totalRequired);
        }
    }

    public void fulfillDistribution(int distributionId){
        // Validation eligibility
        validateDistribution(distributionId);

        // Validate stock availability
        validateInventory(distributionId);

        // Find distribution
        Distribution distribution = distributionRepository.findById(distributionId);

        // Find hamper request
        FoodHamperRequest request = hamperRequestRepository.findById(distribution.getRequestId());

        // adjust stock
        for(DistributionItem item: distribution.getItems()) {
            int inventoryId = item.getInventoryId();
            int adjustment = item.getQuantity();
            inventoryService.adjustStock(inventoryId, -adjustment, InventoryChangeType.DISTRIBUTED);  
        }

        distribution.markPickupStatus(PickupStatus.FULFILLED);
        request.markFulfilled();
        
    }


}
