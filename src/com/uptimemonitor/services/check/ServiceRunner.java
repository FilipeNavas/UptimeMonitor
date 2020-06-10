package com.uptimemonitor.services.check;

import com.uptimemonitor.Constants;
import com.uptimemonitor.models.Log;
import com.uptimemonitor.models.Resource;
import com.uptimemonitor.notifications.Notification;
import com.uptimemonitor.notifications.NotificationService;
import com.uptimemonitor.services.http.HttpMonitor;
import com.uptimemonitor.services.http.HttpMonitorable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ServiceRunner implements ServiceRunnable{
    
    private final String phoneNumberDevOps;

    public ServiceRunner() {
        this.phoneNumberDevOps = "";
    }
    
    public ServiceRunner(String notificationPhoneNumber){
        this.phoneNumberDevOps = notificationPhoneNumber;
    }

    //checks just one resource
    @Override
    public void runOne(Resource resource){
        HttpMonitorable monitor = new HttpMonitor();
        try {
            monitor.check(resource)
                    .thenAccept(log -> consumer(log))
                    .get();
        } catch (ExecutionException | InterruptedException ex) {
            Logger.getLogger(ServiceRunner.class.getName()).log(Level.SEVERE, "Error checking resource", ex);
            throw new RuntimeException("Error checking resource", ex);
        }
    }
    
    //overloaded method without an executor parameter and default ports
    @Override
    public void runAll(){
        runAll(null, new int[]{80, 443});
    }
        
    //checks all resources in the Constants class
    @Override
    public void runAll(Executor executor, int[] ports){
        
        //service to check websites status
        HttpMonitorable monitor = new HttpMonitor();
        if(executor != null) //if not null, gets an instance with the given executor
            monitor = new HttpMonitor(executor);
                
        //check all domains and get a list of CompletableFuture
        try{
            List<Resource> resources = getResources(ports); //generates resources with ports to check
            List<CompletableFuture> tasks = monitor.checkAll(resources)
                    .map(future -> future.thenAccept(log -> consumer(log)))
                    .collect(Collectors.toList());

            //run all, then show a message
            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    System.out.println();
                    // Clear previously displayed report
                    System.out.print("\033[H\033[2J");
                    System.out.flush();                    
                });
        }catch(IllegalArgumentException ex){
            Logger.getLogger(ServiceRunner.class.getName()).log(Level.SEVERE, "Error preparing tasks", ex);
            throw ex;
        }
    }
    
    /**
     * Consumer of the monitor check/checkAll future. It accepts a Log
     * @param log 
     */
    private void consumer(Log log){
        System.out.println(log); //prints the log on the console
        
        //if a phone number was provided we send the SMS
        if(!phoneNumberDevOps.isEmpty())
            log.getPortStatus().forEach((Integer port, Boolean status) -> {
                if(!status){
                    Notification notification = new NotificationService();
                    notification.sendSms(phoneNumberDevOps, log);
                }
            });
    }
    
    
    /**
     * Generate a list of Resource(s) from the Constants
     * @param ports int[] ports
     * @return List
     */
    private static List<Resource> getResources(int[] ports) {
        List<Resource> resources = new ArrayList();
        try{
            //all domains will be added here
            for(String domain : Constants.DOMAINS ){
                resources.add(new Resource(domain, ports));
            }
        }catch(IllegalArgumentException ex){
            Logger.getLogger(ServiceRunner.class.getName()).log(Level.SEVERE, "Error creating resources/domains", ex);
            throw ex;
        }
        return resources;
    }

}
