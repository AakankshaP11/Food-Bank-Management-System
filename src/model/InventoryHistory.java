package model;
import common.InventoryChangeType;
import java.time.*;

public class InventoryHistory {
    private final int inventoryId;
    private final int quantityChange;
    private final InventoryChangeType changeType;
    private final Instant occurredAt;
    public InventoryHistory(int inventoryId, int quantityChange, InventoryChangeType changeType) {
        if (inventoryId <= 0) {
            throw new IllegalArgumentException("Inventory ID must be positive");
        }
        if (changeType == null) {
            throw new IllegalArgumentException("Change type cannot be null");
        }
        if (occurredAt == null) {
            throw new IllegalArgumentException("Occurred at timestamp cannot be null");
        }
        this.inventoryId = inventoryId;
        this.quantityChange = quantityChange;
        this.changeType = changeType;
        this.occurredAt = Instant.now();

        public int getInventoryId() {
            return inventoryId;
        }

        public int getQuantityChange() {
            return quantityChange;
        }
        public InventoryChangeType getChangeType() {
            return changeType;
        }
        public Instant getOccurredAt() {
            return occurredAt;
        }

    }
}