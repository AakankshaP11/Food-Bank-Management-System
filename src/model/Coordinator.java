package model;

public class Coordinator extends User {
    public Coordinator(int userId, String name, String email, String passwordHash, String phone) {
        super(userId, name, email, passwordHash, phone, "Coordinator");
    }
    
}
