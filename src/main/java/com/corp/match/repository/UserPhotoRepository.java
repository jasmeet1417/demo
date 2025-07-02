package com.corp.match.repository;

import com.corp.match.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    List<UserPhoto> findByUserIdOrderByPositionAsc(Long userId);
}
