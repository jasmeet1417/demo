package com.corp.match.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corp.match.entity.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByPersonalEmail(String personalEmail);
}
