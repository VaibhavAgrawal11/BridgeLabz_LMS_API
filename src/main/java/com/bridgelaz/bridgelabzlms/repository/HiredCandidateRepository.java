package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HiredCandidateRepository extends JpaRepository<HiredCandidateModel, Integer> {
    @Query("select u from Hired_Candidate u where u.id = ?1")
    public Optional<HiredCandidateModel> findById(Integer Id);
}
