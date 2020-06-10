package com.uptimemonitor.notifications;

import com.uptimemonitor.models.Log;


public class NotificationService implements Notification{
    
    @Override
    public void sendSms(String phoneNumber, Log message){
        //in real world scneario uses an SMS service, such as Twilio, or make a call to a webservice/api
        System.out.println("Sent SMS to " + phoneNumber + " - Message: " + message.toString());
    }
}
