package com._3line.gravity.core.notification;

import com._3line.gravity.core.usermgt.model.AbstractUser;

/**
 * @author FortunatusE
 * @date 11/14/2018
 */
public interface NotificationService {


    void sendUserCreationMessage(AbstractUser user , String password);

    void sendUserUpdateMessage(AbstractUser user);
}
