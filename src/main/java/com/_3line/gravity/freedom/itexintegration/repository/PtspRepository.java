package com._3line.gravity.freedom.itexintegration.repository;

import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PtspRepository extends JpaRepository<PtspModel, Long> {



    List<PtspModel> findByRrnContainsAndTerminalIdContainsAndCreatedOnBetweenOrderByIdDesc(String rrn,String terminalId, Date from,Date to);

//    @Query(value = "exec spGetPtspByIsVerifiedAndRrnAndTerminalIdAndCreatedOnBetween :is_verified,:rrn_in,:terminal_id,:from_date,:to_date", nativeQuery = true)
    List<PtspModel> findByIsVerifiedAndRrnContainsAndTerminalIdContainsAndCreatedOnBetweenOrderByIdDesc(Boolean isVerified,
                                                                                                        String rrn,
                                                                                                        String terminalId,
                                                                                                        Date from,
                                                                                                        Date to);

    @Query(value = "select top(2) * from ptsp_model where rrn =:rrn and terminal_id =:terminal_id", nativeQuery = true)
    List<PtspModel> findByRrnAndTerminalId(@Param("rrn") String rrn,
                                           @Param("terminal_id") String terminal_id);


//    @Query(value = "exec spGetTopPtspByIsVerifiedAndTerminalId :is_verified,:terminal_id", nativeQuery = true)
    PtspModel findFirstByTerminalIdAndIsVerified(boolean verified,
                                                 String terminalId);
}