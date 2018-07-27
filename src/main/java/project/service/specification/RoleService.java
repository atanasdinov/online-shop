package project.service.specification;

import project.model.entities.Role;

import java.util.List;


public interface RoleService {

    List<Role> getAll();

    Role getRoleByName(String name);

    void insert(Role role);
}
