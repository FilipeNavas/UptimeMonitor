package com.uptimemonitor.notifications;

import com.uptimemonitor.models.Log;


public interface Notification {    
    public void sendSms(String phoneNumber, Log message);
}
