package project.Service;

import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;


public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);
    UserLoginDTO login(UserLoginDTO userLoginDTO);

}
