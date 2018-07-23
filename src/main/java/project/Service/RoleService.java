package project.service;

import project.model.entities.Role;

public interface RoleService {
    //List<Role> all();
    Role get(String name);
    void insert(Role role);
}
