package com.bridgelaz.bridgelabzlms.repository;

import com.bridgelaz.bridgelabzlms.models.CandidateBankDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateBankDetailsRepository extends JpaRepository<CandidateBankDetailsModel, Integer> {
}
