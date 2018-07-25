package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public RoleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Role findRole(String name) {
        TypedQuery<Role> typedQuery = em
                .createQuery("SELECT r FROM Role r WHERE r.name=:name", Role.class)
                .setParameter("name", name);

        return typedQuery.getSingleResult();
    }

    @Override
    @Transactional
    public void persist(Role role) {
        em.persist(role);
    }

    @Override
    public List<Role> getAll() {
        TypedQuery<Role> typedQuery = em
                .createQuery("SELECT r FROM Role r" , Role.class);

        return typedQuery.getResultList();
    }
}
