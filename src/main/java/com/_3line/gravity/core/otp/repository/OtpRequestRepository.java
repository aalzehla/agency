package com._3line.gravity.core.otp.repository;

import com._3line.gravity.core.otp.model.OtpRequest;
import com._3line.gravity.core.repository.AppCommonRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpRequestRepository extends AppCommonRepository<OtpRequest, Long> {

    OtpRequest findFirstByAgentIdOrderByGeneratedOnDesc(String username);

    OtpRequest findFirstByAgentIdAndVerifiedOrderByGeneratedOnDesc (String agentId,boolean isVerified);

    OtpRequest findFirstByAgentIdAndOtpAndVerified(String username, String otp,boolean isVerified);



}
