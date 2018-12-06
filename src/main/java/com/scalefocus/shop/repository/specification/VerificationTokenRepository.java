package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.VerificationToken;


/**
 * <b>This interface declares manipulations over the {@link VerificationToken} data in the database.</b>
 */
public interface VerificationTokenRepository {

    /**
     * Add the {@link VerificationToken}.
     *
     * @param token the token to be saved.
     */
    void addToken(VerificationToken token);

    /**
     * Get the {@link VerificationToken}.
     *
     * @param token the criteria to be found.
     */
    VerificationToken getToken(String token);

    /**
     * Delete the {@link VerificationToken}.
     *
     * @param id the criteria to be found.
     */
    void deleteToken(long id);
}
