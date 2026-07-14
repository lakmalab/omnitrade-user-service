package com.omnitrade.user_service.repository;

import com.omnitrade.user_service.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, UUID>  {
    boolean existsByEmail(String email);
}
