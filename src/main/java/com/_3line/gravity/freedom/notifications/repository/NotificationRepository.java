package com._3line.gravity.freedom.notifications.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.notifications.models.Notification;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends AppCommonRepository<Notification, Long> {

    Page<Notification> findByAgentNameAndIsClosedOrderByCreatedOnDesc(String agentName,Boolean isClosed,Pageable pageDetails);

    Notification findByAgentNameAndIsClosedAndId(String agentUsername,Boolean isClosed,Long id);
//
//    Page<Notification> findByStatus(NotificationStatus pattern, Pageable pageDetails);
}
