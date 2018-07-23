package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.entities.Role;
import project.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Role get(String name) {
        return this.roleRepository.findRole(name);
    }

    @Override
    public void insert(Role role) {
        if (role == null) return;
        this.roleRepository.persist(role);
    }
}