package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.HiredCandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiredCandidateRepository extends JpaRepository<HiredCandidateModel, Integer> {
}
