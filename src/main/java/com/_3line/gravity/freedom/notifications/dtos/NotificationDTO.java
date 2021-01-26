package com._3line.gravity.freedom.notifications.dtos;

import com._3line.gravity.api.notifications.ImageTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationDTO {

    String agentName;
    String agentPhone;
    String message;
    String agentEmail;
    String messageType;
    String ticketId;
    Long id;
    Date createdOn;
    private Boolean isRead;
    private String category;
    private String image;
    private ImageTypes image_type = ImageTypes.BASE_64;
    private ComplaintDTO complaintDto;
    private NotificationContentDTO notificationDto;
    private DisputeResponseDTO disputeDTO;


}
