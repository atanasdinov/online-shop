package project.repositories;

import project.model.entities.Role;

public interface RoleRepository {
    Role findRole(String name);

    void persist(Role role);
}
