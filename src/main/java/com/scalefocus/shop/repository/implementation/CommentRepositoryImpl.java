package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Comment;
import com.scalefocus.shop.repository.specification.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public CommentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void createComment(String message, long userId) {
        em.createNativeQuery("insert into comments (message,user_id) values (:message,:userId)")
                .setParameter("message", message)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public List<Comment> getAllComments(long productId) {
        return (List<Comment>) em.createNativeQuery("select * from comments c left join product_comments cp on c.id=cp.comment_id where cp.product_id=:productId order by c.id", Comment.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public Comment getLastComment() {
        return (Comment) em.createNativeQuery("select * from comments order by id desc limit 1", Comment.class)
                .getSingleResult();
    }

    @Override
    public void deleteComment(long commentId) {
        em.createNativeQuery("delete from comments where id=:commentId")
                .setParameter("commentId", commentId)
                .executeUpdate();
    }

}
