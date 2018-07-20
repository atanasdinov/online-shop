package project.Service;

import project.model.entities.Role;

import java.util.List;

public interface RoleService {
    //List<Role> all();
    Role get(String name);
    void insert(Role role);
}
