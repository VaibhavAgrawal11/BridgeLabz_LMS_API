package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.FellowshipCandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FellowshipCandidateRepository extends JpaRepository<FellowshipCandidateModel, Integer> {
    @Query("select u from FellowshipCandidate u where u.id = ?1")
    public FellowshipCandidateModel findById(int id);
}
