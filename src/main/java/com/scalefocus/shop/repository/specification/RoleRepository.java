package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.Role;


/**
 * <b>This interface declares manipulations over the {@link Role} data in the database.</b>
 */
public interface RoleRepository {

    /**  Get a specific {@link Role}.
     *
     * @param roleName name of the role.
     * @return found role.
     */
    Role getRole(String roleName);

    /** Create a {@link Role}.
     *
     * @param role data to be inserted.
     */
    void createRole(Role role);

    /** This method checks if roles exist.
     *
     * @return true - if roles exist, false - if roles don't exist
     */
    boolean rolesExist();
}
