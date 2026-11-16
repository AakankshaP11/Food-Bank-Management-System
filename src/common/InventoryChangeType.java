package common;
// for auditing purposes, to find out reasons why inventory changed it could be donation, wasted or adjustment
public enum InventoryChangeType {
    DONATION_RECEIVED,
    DISTRIBUTED,
    WASTED,
    MANUAL_ADJUSTMENT
}