package project.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import project.model.entities.Role;
import project.model.entities.User;
import project.repository.specification.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void addUser(User user, Role role) {
        em
                .createNativeQuery("insert into users (address, email, firstName, lastName, password, username, role_id) " +
                        "values (:address, :email, :firstName, :lastName, :password, :username, :role_id)", User.class)
                .setParameter("address", user.getAddress())
                .setParameter("email", user.getEmail())
                .setParameter("firstName", user.getFirstName())
                .setParameter("lastName", user.getLastName())
                .setParameter("password", user.getPassword())
                .setParameter("username", user.getUsername())
                .setParameter("role_id", user.getRole().getId());
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> typedQuery = (TypedQuery<User>) em
                .createQuery("select u from User u", User.class);

        return typedQuery.getResultList();
    }

    @Override
    public User getExistingUser(String username, String password) {
        User user = (User) em
                .createNativeQuery("select * from users where username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();

        if (passwordEncoder.matches(password, user.getPassword()))
            return user;
        else
            return null;
    }

    @Override
    public User getExistingUserByUsername(String username) {
        return (User) em
                .createNativeQuery("select * from users where username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public User checkToken(String token) {
        return (User) em
                .createNativeQuery("select * from users where token=:token", User.class)
                .setParameter("token", token)
                .getSingleResult();
    }

    @Transactional
    @Override
    public void setToken(String token, User user) {
        em
                .createNativeQuery("update users set token=:token where username=:username", User.class)
                .setParameter("token", token)
                .setParameter("username", user.getUsername())
                .executeUpdate();
    }
}