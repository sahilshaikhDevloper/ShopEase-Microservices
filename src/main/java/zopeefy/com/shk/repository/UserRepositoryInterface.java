package zopeefy.com.shk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zopeefy.com.shk.entity.UserEntity;
import zopeefy.com.shk.entity.UserModule;

import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<UserEntity , Long> {

    boolean existsByEmailAndIdNot(String email, Long id);
    Optional <UserEntity> findByEmail(String email);
}
