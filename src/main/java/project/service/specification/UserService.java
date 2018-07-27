package project.service.specification;

import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.User;

import javax.servlet.http.HttpServletResponse;


public interface UserService {

    void register(UserRegisterDTO userRegisterDTO);

    boolean login(UserLoginDTO userLoginDTO, HttpServletResponse response);

    User checkToken(String token);

    String addToken(User user);

    User getUser(String name);
}
