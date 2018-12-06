package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Role;
import com.scalefocus.shop.repository.specification.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public RoleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void createRole(Role role) {
        em.createNativeQuery("insert into roles (name) values (:name)", Role.class)
                .setParameter("name", role.getName())
                .executeUpdate();
    }

    @Override
    public Role getRole(String roleName) {
        return (Role) em.createNativeQuery("select * from roles where name=:roleName", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
    }

    @Override
    public boolean rolesExist() {
        return !(em.createNativeQuery("select * from roles")
                .getResultList()
                .isEmpty());
    }

}
