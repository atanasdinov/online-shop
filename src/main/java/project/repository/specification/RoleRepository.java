package project.repository.specification;

import project.model.entities.Role;

import java.util.List;


public interface RoleRepository {

    Role findRole(String roleName);

    void persist(Role role);

    List<Role> getAllRoles();
}
