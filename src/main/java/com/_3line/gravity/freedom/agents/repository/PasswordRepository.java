package com._3line.gravity.freedom.agents.repository;

import com._3line.gravity.freedom.agents.models.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findByUsername(String username);
}
