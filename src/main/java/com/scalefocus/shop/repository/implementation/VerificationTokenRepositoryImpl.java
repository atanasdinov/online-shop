package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.VerificationToken;
import com.scalefocus.shop.repository.specification.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public VerificationTokenRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addToken(VerificationToken token) {
        em.createNativeQuery("insert into verification_tokens (token, user_id, expiry_date) " + "values (:token, :userId, :expiryDate)", VerificationToken.class)
                .setParameter("token", token.getToken())
                .setParameter("userId", token.getUser().getId())
                .setParameter("expiryDate", token.getExpiryDate())
                .executeUpdate();
    }

    @Override
    public VerificationToken getToken(String token) {
        return (VerificationToken) em.createNativeQuery("select * from verification_tokens where token=:token", VerificationToken.class)
                .setParameter("token", token)
                .getSingleResult();
    }

    @Override
    public void deleteToken(long id) {
        em.createNativeQuery("delete from verification_tokens where user_id=:id", VerificationToken.class)
                .setParameter("id", id)
                .executeUpdate();
    }
}
