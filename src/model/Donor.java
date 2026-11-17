package model;

public class Donor extends User {
     public Donor(int userId, String name, String email, String passwordHash, String phone) {
        super(userId, name, email, passwordHash, phone, "Donor");
     }
    
}
