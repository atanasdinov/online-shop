package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.DTOS.UserLoginDTO;
import project.model.entities.User;

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
    public void add(User user) {
        em.merge(user);
    }


    @Override
    @Transactional
    public List<User> all() {
        TypedQuery<User> typedQuery = em
                .createQuery("select u from User u", User.class);

        return typedQuery.getResultList();
    }

    @Override
    public User getExistingUser(String username, String password) {
        TypedQuery<User> typedQuery = em
                .createQuery("SELECT u FROM User u where u.username=:username AND u.password=:password", User.class)
                .setParameter("username", username)
                .setParameter("password", password);

        return typedQuery.getSingleResult();
    }

    @Override
    public User getUserByUsername(String username) {
        TypedQuery<User> typedQuery = em
                .createQuery("SELECT u FROM User u where u.username=:username", User.class)
                .setParameter("username", username);

        return typedQuery.getSingleResult();
    }

}