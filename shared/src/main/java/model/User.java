package model;

/**
 * A model object representing the core data of a user.
 *
 * @param username the user's username
 * @param pwHash the user's hashed password
 * @param email    the user's email address
 */
public record User(String username, String pwHash, String email) {
}
