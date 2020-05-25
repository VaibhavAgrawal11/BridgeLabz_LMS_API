package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FellowshipCandidateRepository extends JpaRepository<FellowshipCandidateModel, Integer> {
    @Query("select u from FellowshipCandidate u where u.id = ?1")
    public Optional<FellowshipCandidateModel> findById(int id);
}
