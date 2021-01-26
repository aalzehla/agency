package com._3line.gravity.core.verification.service;

import com._3line.gravity.core.verification.dtos.VerificationDto;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VerificationService {
    /**
     * This fetches the {@link VerificationDto} object with the id {@code id}
     *
     * @param id the id of the record in the db
     * @return The {@link VerificationDto} object identified by {@code id} or null if none is found
     */
    VerificationDto getVerification(Long id);

    String verify(VerificationDto verificationDto);

    /**
     * This will decline a Verification request.
     *
     * @param verification  The {@link VerificationDto} object
     */
    String decline(VerificationDto verification) throws VerificationException;



    String cancel(Long id) throws VerificationException;


    int getTotalNumberForVerification();


    long getTotalNumberPending();


    Page<VerificationDto> getPendingForUser(Pageable pageable);


    Page<VerificationDto> getInitiatedByUser(Pageable pageable);

    Page<VerificationDto> getApprovableActions(Pageable pageable,String opCode,String initiator,String from,String to);
}
