package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.User;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link User} data in the database.</b>
 */
public interface UserRepository {

    /**
     * This method registers a {@link User}.
     *
     * @param user contain the validated user data.
     */
    void addUser(User user);

    /**
     * This method returns the users' count.
     *
     * @return registered users' count
     */
    int usersCount();

    /**
     * Find a specific {@link User} by username.
     *
     * @param username the criteria to be found.
     * @return found user.
     */
    User getUser(String username);

    /**
     * Check if the value from the cookie match the token from an existing {@link User}.
     *
     * @param token contain the cookie value.
     * @return found user.
     */
    User checkToken(String token);

    /**
     * Set a specific token to a {@link User}.
     *
     * @param token contain the value to be set.
     * @param user  is the user whom the token will be set.
     */
    void setToken(String token, User user);

    /**
     * Check if an {@link User} exists in the database.
     *
     * @param username the criteria to be found.
     * @return whether is found.
     */
    boolean userExists(String username);

    /**
     * Check if there is such email already registered.
     *
     * @param email the criteria to be found.
     * @return whether is found.
     */
    boolean emailExists(String email);

    /**
     * Set a true value to verify the {@link User}.
     *
     * @param username the unique criteria for the user to be verified.
     */
    void setEnabled(String username);

    /**
     * This method returns all registered users in order by role's id.
     *
     * @return list of users
     */
    List<User> getAllUsers();

    /**
     * This method changes the role of a user.
     *
     * @param username username of the selected user
     * @param roleName name of the role to be given
     */
    void changeRole(String username, String roleName);
}
