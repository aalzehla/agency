package com._3line.gravity.core.verification.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.verification.models.Verification;
import com._3line.gravity.core.verification.models.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VerificationRepository extends AppCommonRepository<Verification , Long> {

    Verification findByCodeAndEntityId(String code , String entityId);
    Verification findByCodeAndEntityIdAndStatus(String code , Long entityId,VerificationStatus status);

    Verification findFirstByEntityNameAndStatus(String name, VerificationStatus status);

    Page<Verification> findByStatusAndInitiatedByOrderByInitiatedOnDesc(VerificationStatus status, String initiatedBy, Pageable pageable);
    Page<Verification> findByStatusOrderByInitiatedOnDesc(VerificationStatus status, Pageable pageable);

    Page<Verification> findByInitiatedByNotLikeAndStatusOrderByInitiatedOnDesc(String initiatedBy, VerificationStatus status, Pageable pageable);
    Page<Verification> findByInitiatedByNotLikeAndStatusAndCodeInOrderByInitiatedOnDesc(String initiatedBy, VerificationStatus status,List<String> code, Pageable pageable);

    Integer countByInitiatedByNotLikeAndStatus(String initiatedBy, VerificationStatus status) ;
    Page<Verification> findByOperationAndInitiatedByAndStatusOrderByInitiatedOnDesc(String operation, String initiatedBy, VerificationStatus status, Pageable pageable);

    List<Verification> findByCodeAndStatusAndInitiatedBy(String code,VerificationStatus status, String initiatedBy);
    List<Verification> findByStatusAndInitiatedBy(VerificationStatus status, String initiatedBy);

    List<Verification> findByStatus(VerificationStatus status);

    Page<Verification> findByStatusOrderByCreatedOnDesc(VerificationStatus status, Pageable pageable);

    Verification findFirstByEntityNameAndEntityIdAndStatus(String name, long id, VerificationStatus status);

    long countByInitiatedByAndStatus(String username, VerificationStatus status);
    //
    Page<Verification> findByInitiatedByAndStatusOrderByInitiatedOnDesc(String initiatedby, VerificationStatus status, Pageable pageable);

    @Query( "select v from Verification v where v.initiatedBy != :initiated and v.operation in :permissionlist and v.status ='PENDING'")
    List<Verification> findVerificationForUser(@Param("initiated") String initiatedBy, @Param("permissionlist") List<String> operation);


    @Query( "select v from Verification v where v.initiatedBy != :initiated and v.operation in :permissionlist and v.status ='PENDING'")
    Page<Verification> findVerificationForUsers(@Param("initiated") String initiatedBy, @Param("permissionlist") List<String> operation, Pageable pageable);

    Page<Verification> findByInitiatedBy(String initiatedBy, Pageable pageable) ;

    int countByInitiatedBy(String initiatedBy) ;


    @Query( "select v from Verification v where v.verifiedBy = :verified and v.status !='PENDING'")
    Page<Verification> findVerifiedOperationsForUser(@Param("verified") String verifiedBy, Pageable pageable);
}
