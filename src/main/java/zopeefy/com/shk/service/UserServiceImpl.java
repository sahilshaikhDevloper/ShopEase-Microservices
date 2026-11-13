package zopeefy.com.shk.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zopeefy.com.shk.dto.LoginRequest;
import zopeefy.com.shk.dto.LoginResponse;
import zopeefy.com.shk.entity.UserEntity;
import zopeefy.com.shk.entity.UserModule;
import zopeefy.com.shk.repository.UserRepositoryInterface;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserServiceRepository {

    private final JwtTokenService jwtTokenService;

    private final UserRepositoryInterface userRepositoryInterface;
    public final PasswordEncoder passwordEncoder;



    public UserServiceImpl(JwtTokenService jwtTokenService, UserRepositoryInterface userRepositoryInterface, PasswordEncoder passwordEncoder) {
        this.jwtTokenService = jwtTokenService;
        this.userRepositoryInterface = userRepositoryInterface;
        this.passwordEncoder = passwordEncoder;


        log.info("started application bean created");
    }


    // CREATE USER
    @Override
    public UserModule createUser(UserModule user) {

        UserEntity entity = new UserEntity();

        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setRole(user.getRole());

        entity.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepositoryInterface.save(entity);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        UserEntity savedUser = userRepositoryInterface.save(entity);

        log.info("post api call");
        return convertToModule(savedUser);
    }

    // GET ALL USERS
    @Override
    public List<UserModule> getAllUsers() {

        log.info("get all user api call");
        return userRepositoryInterface.findAll()
                .stream()
                .map(this::convertToModule)
                .toList();
    }

    // GET USER BY ID
    @Override
    public UserModule getUserById(Long id){

        UserEntity user = userRepositoryInterface.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with ID: " + id)
                );
        log.info("get user by id api call");

        return convertToModule(user);
    }

    // DELETE USER
    @Override
    public void deleteUser(Long id) {

        if (!userRepositoryInterface.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        userRepositoryInterface.deleteById(id);
        log.info("delete user by id api call");
    }

    @Override
    public UserModule UpdateUser(Long id, UserModule module) {


        UserEntity entity = userRepositoryInterface.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with ID: " + id)
                );
        if (module.getFirstName() != null) {
            entity.setFirstName(module.getFirstName());
        }

        if (module.getLastName() != null) {
            entity.setLastName(module.getLastName());
        }

        if (module.getEmail() != null) {

            if (userRepositoryInterface.existsByEmailAndIdNot(
                    module.getEmail(), id)) {
                throw new RuntimeException("Email already exists");
            }

            entity.setEmail(module.getEmail());
        }

        // 5. Update Password with BCrypt
        if (module.getPassword() != null &&
                !module.getPassword().isBlank()) {

            entity.setPassword(
                    passwordEncoder.encode(module.getPassword())
            );
        }

        // 6. Update Phone Number
        if (module.getPhoneNumber() != null) {
            entity.setPhoneNumber(module.getPhoneNumber());
        }

        // 7. Save Updated User
        UserEntity updatedUser = userRepositoryInterface.save(entity);
        log.info("update api call");

        return convertToModule(updatedUser);
    }

    @Override
    public LoginResponse Login(LoginRequest loginRequest) {
        UserEntity entity = userRepositoryInterface.findByEmail(loginRequest.email).
                orElseThrow(()->new RuntimeException("email not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(),entity.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenService.GenerateToken(entity.getEmail());


        UserModule module = convertToModule(entity);

        log.info("login api call");

        return new LoginResponse(module ,token);
    }


    // ENTITY TO MODULE CONVERSION
    private UserModule convertToModule(UserEntity user) {

        UserModule module = new UserModule();

        module.setId(user.getId());
        module.setFirstName(user.getFirstName());
        module.setLastName(user.getLastName());
        module.setEmail(user.getEmail());
        module.setPhoneNumber(user.getPhoneNumber());
        module.setRole(user.getRole());
        module.setIsActive(user.getIsActive());
        module.setCreatedAt(user.getCreatedAt());
        module.setUpdatedAt(user.getUpdatedAt());

//        module.setPassword(null);

        return module;
    }
}
