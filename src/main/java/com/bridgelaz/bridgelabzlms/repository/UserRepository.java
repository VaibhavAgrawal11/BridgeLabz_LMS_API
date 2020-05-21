package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from UserDetails u where u.email = ?1")
    public User findByEmail(String emailAddress);
}