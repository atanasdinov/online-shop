package project.repositoriesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Role;
import project.model.entities.User;
import project.repositories.UserRepository;

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

    @Override
    @Transactional
    public void add(User user, Role role) {
                em
                    .createNativeQuery("INSERT INTO users (address, email, firstName, lastName, password, username, role_id) " +
                            "VALUES (:address, :email, :firstName, :lastName, :password, :username, :role_id)", User.class)
                    .setParameter("address", user.getAddress())
                    .setParameter("email", user.getEmail())
                    .setParameter("firstName", user.getFirstName())
                    .setParameter("lastName", user.getLastName())
                    .setParameter("password", user.getPassword())
                    .setParameter("username", user.getUsername())
                    .setParameter("role_id", user.getRole().getId());
    }


    @Override
    public List<User> all() {
              TypedQuery<User> typedQuery = (TypedQuery<User>) em
                    .createQuery("select u from User u", User.class);

              return typedQuery.getResultList();

    }

    @Override
    public User getExistingUser(String username, String password) {
              return (User) em
                    .createNativeQuery("SELECT * FROM users where username=:username AND password=:password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
    }

    @Override
    public User getUserByUsername(String username) {
                return (User) em
                .createNativeQuery("SELECT * FROM users where username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

}