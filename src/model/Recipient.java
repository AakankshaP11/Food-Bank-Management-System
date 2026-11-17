package model;

import common.RecipientApprovalStatus;

public class Recipient extends User {
    private final int householdSize;
    private String dietaryRestrictions;
    private String emergencyContact;
    private RecipientApprovalStatus approvalStatus;

    public Recipient(int userId, String name, String email, String passwordHash, String phone , int householdSize, String dietaryRestrictions, String emergencyContact) {
        super(userId, name, email, passwordHash, phone, "Recipient");
        if(householdSize <= 0) {
            throw new IllegalArgumentException("Household size must be positive");
        }
        if(emergencyContact == null || emergencyContact.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact cannot be null or empty");
        }
        this.householdSize = householdSize;
        this.dietaryRestrictions = dietaryRestrictions;
        this.emergencyContact = emergencyContact;
        this.approvalStatus = RecipientApprovalStatus.PENDING;
    }
    public int getHouseholdSize() {
        return householdSize;
    }
    public String getDietaryRestrictions() {
        return dietaryRestrictions; 
    }
    public String getEmergencyContact() {
        return emergencyContact;
    }
    public RecipientApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
    public void updateApprovalStatus(RecipientApprovalStatus newStatus) {
        if(newStatus == null) {
            throw new IllegalArgumentException("Approval status cannot be null");
        }
        this.approvalStatus = newStatus;
    }
}
