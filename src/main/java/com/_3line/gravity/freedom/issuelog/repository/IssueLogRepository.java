package com._3line.gravity.freedom.issuelog.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.models.IssueStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IssueLogRepository extends AppCommonRepository<IssueLog, Long> {

    Page<IssueLog> findAllByOrderByIdDesc(Pageable pageable);
//    Page<IssueLog> findAllByOrderByTreatedDateDesc(Pageable pageable);
//    Page<IssueLog> findByStatusContainsAndAgentNameContainsAndTreatedByContainsOrderByCreatedOnDesc(String a,String t,String s,Pageable pageable);
//    Page<IssueLog> findByAgentNameContainsAndStatusAndCategoryContainsAndCreatedOnBetweenOrderByCreatedOnDesc
//            (String agentName, IssueStatus status,String category, Date from, Date to, Pageable pageable);
//
//    Page<IssueLog> findByAgentNameContainsAndCategoryContainsAndCreatedOnBetweenOrderByCreatedOnDesc
//            (String agentName,String category, Date from, Date to, Pageable pageable);
//
//    Page<IssueLog> findAllByOrderByIdDesc(Example<IssueLog> var1, Pageable pageable);
//    Page<IssueLog> findAllByOrderByIdDesc(String issueStatus, Pageable pageable);
//
//    IssueLog findIssueLogById(Long id);

    IssueLog findIssueLogByNotification_Id(Long id);

    IssueLog findByDisputeLog_Id(Long id);

    IssueLog findByAgent_UsernameAndIdAndIsRead(String agentName,Long id,Boolean isRead);

    IssueLog findByAgent_UsernameAndId(String agentName,Long id);

    List<IssueLog> findByAgent_UsernameAndIsRead(String agentName,Boolean isRead);

    List<IssueLog> findByAgent_Username(String agentName);

    List<IssueLog> findByOrderByCreatedOnDesc();

    Page<IssueLog> findByStatusOrderByCreatedOnDesc(IssueStatus pattern, Pageable pageDetails);
}
