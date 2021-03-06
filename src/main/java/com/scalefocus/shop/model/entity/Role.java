package com.scalefocus.shop.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * <b>This entity is intended to define certain permissions for the {@link User} in the application.</b>
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<User> users;

    public Role() {
        this.users = new HashSet<>();
    }

    public Role(String name) {
        this.name = name;
        this.users = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void setId(Long id) {
        this.id = id;
    }
}
