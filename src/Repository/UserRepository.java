package Repository;

import java.util.*;

import common.RecipientApprovalStatus;
import model.Recipient;
import model.User;

public class UserRepository {
    // <key: userId, value: User (Donor, Recipient, Volunteer or Coordinator)>
    private final Map<Integer, User> users = new HashMap<>();

    // saves a new user (sign-up)
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        int id = user.getUserId();
        if (users.containsKey(id)) {
            throw new IllegalArgumentException("User with ID " + id + " already exists");
        }
        if (findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email " + user.getEmail() + " is already registered");
        }
        users.put(id, user);
    }

    // finds a user by id, returns null if not found
    public User getUserById(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        return users.get(userId);
    }

    // finds a user by email (used for login), returns null if not found
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    // replaces an existing user with the edited version (edit account info)
    public void update(User user) {
        if (user == null || !users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User does not exist");
        }
        users.put(user.getUserId(), user);
    }

    // coordinator deletes an account
    public void delete(int userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }
        users.remove(userId);
    }

    // lists recipients waiting for coordinator approval
    public List<Recipient> getPendingRecipients() {
        List<Recipient> result = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Recipient) {
                Recipient r = (Recipient) user;
                if (r.getApprovalStatus() == RecipientApprovalStatus.PENDING) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    // lists all users (coordinator view)
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}