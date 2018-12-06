package com.scalefocus.shop.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static com.scalefocus.shop.model.constant.ValidationConstants.*;


/**
 * <b>This entity is intended to give an access to most of the functionalities of the app.
 * Such as adding {@link Product}s to {@link Cart} and purchase them or to make a {@link Comment}. It must be assigned to a {@link Role}.</b>
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = FIRST_NAME_REGEX, message = NAME_PATTERN_MISMATCH_ERROR)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = LAST_NAME_REGEX, message = NAME_PATTERN_MISMATCH_ERROR)
    private String lastName;

    @Column(unique = true)
    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_ADDRESS_ERROR_MESSAGE)
    private String email;

    @Column(unique = true)
    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = USERNAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = USERNAME_REGEX, message = USERNAME_PATTERN_MISMATCH_ERROR)
    private String username;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Size(min = PASSWORD_MIN_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    private String password;

    private String address;

    private String token;

    private Boolean enabled;

    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.PERSIST)
    private Role role;

    @OneToOne(targetEntity = Cart.class, cascade = CascadeType.MERGE)
    private Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    public User() {
        this.comments = new HashSet<>();
    }

    public User(String firstName, String lastName, String email, String username, String password, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.enabled = false;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}