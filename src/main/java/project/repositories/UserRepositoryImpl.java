package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
        em.persist(user);
    }

    @Override
    @Transactional
    public User get(String username) {

        TypedQuery<User> typedQuery = em
                .createQuery("SELECT u FROM User u where u.username=:username", User.class)
                .setParameter("username", username);
        return typedQuery.getSingleResult();
    }

    @Override
    @Transactional
    public List<User> all() {
        return (List<User>) em.createQuery("select u from User u");
    }
}