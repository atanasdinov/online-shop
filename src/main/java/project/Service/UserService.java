package project.service;

import project.model.DTOS.UserDTO;


public interface UserService {
    void register(UserDTO userDTO);
    UserDTO get(String username);

}
