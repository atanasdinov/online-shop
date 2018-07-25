package project.Service;

import project.model.entities.Role;

public interface RoleService {
    List<Role> getAll();
    Role get(String name);

    void insert(Role role);
}
