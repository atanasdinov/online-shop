package project.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.entities.Role;
import project.repository.specification.RoleRepository;
import project.service.specification.RoleService;

import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.getAllRoles();
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findRole(name);
    }

    @Override
    public void insert(Role role) {
        if (role == null)
            return;

        roleRepository.persist(role);
    }
}