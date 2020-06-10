package com.uptimemonitor;

import com.uptimemonitor.models.Resource;
import com.uptimemonitor.services.check.ServiceRunner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import com.uptimemonitor.services.check.ServiceRunnable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author filipe
 */
public class Main {

    static final int CHECK_INTERVAL = 5;   // Check every X seconds
    static final int NUM_THREADS = 3;      // Number of threads
    static final int[] PORTS = {80, 443};  // Ports of services to monitor

    public static void main(String[] args) {
        
        // Thread pool to process domains
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

        // Executor to run the check and run reports on a service
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1); //always 1
    
        //service that runs the tasks
        ServiceRunnable service = new ServiceRunner("0001112222"); //constructor accepts an optional phone number for notifications
        
        ses.scheduleAtFixedRate(() -> {
            try{
                service.runAll(threadPool, PORTS); //checks all domains in the Constants for given ports
            }catch(Exception ex){
                //Exception can happen in the runAll() so we need to catch it here, catching outside of the lambda doesnt work
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception running tasks, stoping scheduler...", ex);            
                threadPool.shutdown();
                ses.shutdown();                                 
            }
        }, 0, CHECK_INTERVAL, TimeUnit.SECONDS);     
            
        //check only one resource/domain
        //service.runOne(new Resource("google.com", PORTS));
            
    }
   
}
