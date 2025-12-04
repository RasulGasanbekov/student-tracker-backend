package com.edutrack.backend.repository;

import com.edutrack.backend.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    List<UserScore> findByUserId(Long userId);

    @Query("SELECT us FROM UserScore us JOIN us.component c JOIN c.subject s WHERE us.user.id = :userId")
    List<UserScore> findByUserIdWithDetails(@Param("userId") Long userId);
}