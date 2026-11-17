package Services;

import model.Donation;
import model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

import Repository.DonationRepository;
import common.DonationStatus;

public class DonationService {
    private final DonationRepository donationRepository; // donationrepository handles donation
    private final InventoryService inventoryService; // inventoryservice handles inventory stock updates and history for auditing.

    public DonationService(DonationRepository donationRepository, InventoryService inventoryService) {
        if(donationRepository == null) {
            throw new IllegalArgumentException("DonationRepository cannot be null");
        }
        if(inventoryService == null) {
            throw new IllegalArgumentException("InventoryService cannot be null");
        }
        this.donationRepository = donationRepository;
        this.inventoryService = inventoryService;
    }

    //validates and save a new donation
    public void createDonation(Donation donation) {
        if(donation == null) {
            throw new IllegalArgumentException("Donation cannot be null");
        }
        donationRepository.save(donation);
    }

    // create inventory and mark the donation comepleted
    
    public void completeDonation(
            int donationId,
            InventoryItem inventoryItem) {

        List<Donation> donationList = donationRepository.getDonationById(donationId);

        if (donationList.isEmpty()) {
            throw new IllegalArgumentException("Donation not found");
        }

        Donation donation = donationList.get(0);

        if (donation.getStatus() == DonationStatus.COMPLETED) {
            throw new IllegalStateException("Donation already completed");
        }

        if (inventoryItem == null || inventoryItem.getDonationId() != donationId) {
            throw new IllegalArgumentException(
                "Inventory must belong to this donation"
            );
        }

        if (donation.getStatus() == DonationStatus.CREATED ||
            donation.getStatus() == DonationStatus.FAILED) {
            donation.setStatus(DonationStatus.PENDING);
        }

        try {
            inventoryService.addInventoryItem(inventoryItem);
            donation.setStatus(DonationStatus.COMPLETED);
        } catch (RuntimeException ex) {
            if (donation.getStatus() == DonationStatus.PENDING) {
                donation.setStatus(DonationStatus.FAILED);
            }
            throw ex;
        }
    }

    // retrieve a donation
    public Donation getDonationById(int donationId) {
        if(donationId <= 0) {
            throw new IllegalArgumentException("Donation ID must be positive");
        }
        List<Donation> donationList = donationRepository.getDonationById(donationId);
        if (donationList.isEmpty()) {
            throw new IllegalArgumentException("Donation not found");
        }
        return donationList.get(0);
    }
    
}