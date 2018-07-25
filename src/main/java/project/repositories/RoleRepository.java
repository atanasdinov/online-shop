package project.repositories;

import project.model.entities.Role;

import java.util.List;

public interface RoleRepository {
    Role findRole(String name);
    void persist(Role role);
    List<Role> getAll();
}
