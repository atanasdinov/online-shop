package project.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Role;
import project.repository.specification.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public RoleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Role findRole(String roleName) {
        TypedQuery<Role> typedQuery = em
                .createQuery("select r from Role r where r.name=:roleName", Role.class)
                .setParameter("roleName", roleName);

        return typedQuery.getSingleResult();
    }

    @Override
    public void persist(Role role) {
        em.persist(role);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> typedQuery = em
                .createQuery("select r from Role r", Role.class);

        return typedQuery.getResultList();
    }
}
