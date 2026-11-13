package zopeefy.com.shk.service;

import zopeefy.com.shk.ExceptionHandler.GlobalExceptionHandler;
import zopeefy.com.shk.dto.LoginRequest;
import zopeefy.com.shk.dto.LoginResponse;
import zopeefy.com.shk.entity.UserModule;

import java.util.List;

public interface UserServiceRepository {

    UserModule createUser(UserModule user);

    List<UserModule> getAllUsers();

    UserModule getUserById(Long id);

    void deleteUser(Long id);

    UserModule UpdateUser(Long id, UserModule module);

    LoginResponse Login(LoginRequest loginRequest) throws GlobalExceptionHandler;
}
