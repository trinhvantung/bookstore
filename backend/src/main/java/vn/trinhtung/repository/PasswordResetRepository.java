package vn.trinhtung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.ForgotPassword;

@Repository
public interface PasswordResetRepository extends JpaRepository<ForgotPassword, String> {

}
