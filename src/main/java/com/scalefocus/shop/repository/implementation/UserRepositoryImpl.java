package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Role;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.repository.specification.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;


@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryImpl(EntityManager em, BCryptPasswordEncoder passwordEncoder) {
        this.em = em;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user) {
        em.createNativeQuery("insert into users (address, email, firstName, lastName, password, username, role_id, enabled) " +
                "values (:address, :email, :firstName, :lastName, :password, :username, :role_id, :enabled)", User.class)
                .setParameter("address", user.getAddress())
                .setParameter("email", user.getEmail())
                .setParameter("firstName", user.getFirstName())
                .setParameter("lastName", user.getLastName())
                .setParameter("password", user.getPassword())
                .setParameter("username", user.getUsername())
                .setParameter("role_id", user.getRole().getId())
                .setParameter("enabled", user.isEnabled());
    }

    @Override
    public int usersCount() {
        BigInteger result = (BigInteger) em.createNativeQuery("select count(id) from users")
                .getSingleResult();

        return result.intValue();
    }

    @Override
    public User getUser(String username) {
        return (User) em.createNativeQuery("select * from users where username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public User checkToken(String token) {
        return (User) em.createNativeQuery("select * from users where token=:token", User.class)
                .setParameter("token", token)
                .getSingleResult();
    }

    @Override
    public void setToken(String token, User user) {
        em.createNativeQuery("update users set token=:token where username=:username", User.class)
                .setParameter("token", token)
                .setParameter("username", user.getUsername())
                .executeUpdate();
    }

    @Override
    public boolean userExists(String username) {
        return !(em.createNativeQuery("select * from users where username=:username")
                .setParameter("username", username)
                .getResultList()
                .isEmpty());
    }

    @Override
    public boolean emailExists(String email) {
        return !(em.createNativeQuery("select * from users where email=:email")
                .setParameter("email", email)
                .getResultList()
                .isEmpty());
    }

    @Override
    public void setEnabled(String username) {
        em.createNativeQuery("update users set enabled=:enabled where username=:username", User.class)
                .setParameter("enabled", true)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public List<User> getAllUsers() {
        return em.createNativeQuery("select * from users order by role_id asc", User.class)
                .getResultList();
    }

    @Override
    public void changeRole(String username, String roleName) {
        Role role = (Role) em.createNativeQuery("select * from roles where name=:roleName", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult();

        em.createNativeQuery("update users set role_id=:roleId where username=:username", User.class)
                .setParameter("roleId", role.getId())
                .setParameter("username", username)
                .executeUpdate();
    }
}
