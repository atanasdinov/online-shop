package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.Comment;
import com.scalefocus.shop.model.entity.Product;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link Comment} data in the database.</b>
 */
public interface CommentRepository {

    /**
     * Create a {@link Comment}.
     *
     * @param message the content to be stored.
     * @param userId  the author.
     */
    void createComment(String message, long userId);

    /**
     * Get all comments from certain {@link Product}.
     *
     * @param productId the criteria to be found.
     * @return all comment data.
     */
    List<Comment> getAllComments(long productId);

    /**
     * Get the last {@link Comment}.
     *
     * @return last created comment.
     */
    Comment getLastComment();

    /**
     * Deleting a {@link Comment}.
     *
     * @param commentId the criteria to be found.
     */
    void deleteComment(long commentId);

}
