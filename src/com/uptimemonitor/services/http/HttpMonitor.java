package com.uptimemonitor.services.http;

import com.uptimemonitor.models.Resource;
import com.uptimemonitor.models.Log;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class HttpMonitor implements HttpMonitorable{

    private final int httpTimeout;
    private final Executor threadExecutor;
    
    /**
     * Default constructor
     */
    public HttpMonitor(){
        this.httpTimeout = 1000;
        this.threadExecutor = ForkJoinPool.commonPool();
    }
    
    public HttpMonitor(Executor executor){
        this.httpTimeout = 1000; //default value
        this.threadExecutor = executor;
    }

    HttpMonitor(Executor executor, int timeout){
        this.httpTimeout = timeout;
        this.threadExecutor = executor;
    }
    
    /**
     * Given a list of resources, check them and return a Stream of CompletableFutures
     * @param resources
     * @return Stream of CompletableFuture
     */
    @Override
    public Stream<CompletableFuture<Log>> checkAll(List<Resource> resources){
        return resources.stream().map(this::checkAsync);
    }
    
    /**
     * Checks one single resource
     * @param resource
     * @return CompletableFuture
     */
    @Override
    public CompletableFuture<Log> check(Resource resource){
        return checkAsync(resource);
    }
    
    
    /**
     * Checks if resource is up
     * @param resource
     * @return boolean
     */
    private boolean isReachable(Resource resource, int port) {
        
        // Ping the provided IP and port
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(resource.getIp(), port), httpTimeout); //uses IP
            return true;
        } catch (IOException ex) {
            //Logger.getLogger(HttpMonitor.class.getName()).log(Level.WARNING, "Error reaching resource", ex);
            return false;
        }
    }
    
    /**
     * Returns a list of CompletableFuture of type Log with the Resource and status (isUp) of that resource
     * @param resource
     * @return list of CompletableFuture of type Log
     */
    private CompletableFuture<Log> checkAsync(Resource resource) {        
        return CompletableFuture.supplyAsync(() -> {
            /*
            To be able to fetch the status of many ports and save it to the log,
            we use a hashmap mapping the status of each port that was passed in the Resource ports array
            */
            Map<Integer, Boolean> portStatus = new HashMap();
            for(int port : resource.getPorts()){
                portStatus.put(port, isReachable(resource, port));
            }
            return new Log(resource, portStatus);
        }, threadExecutor);
    }
    
}
