package project.Dao;

import org.springframework.stereotype.Repository;
import project.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(User user) {
        em.persist(user);
    }

    @Override
    public List<User> listUsers() {
        javax.persistence.criteria.CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
        @SuppressWarnings("unused")
        Root<User> root = criteriaQuery.from(User.class);
        return (List<User>) em.createQuery(criteriaQuery).getResultList();
    }
}
