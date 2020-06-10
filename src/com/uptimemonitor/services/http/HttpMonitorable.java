package com.uptimemonitor.services.http;

import com.uptimemonitor.models.Log;
import com.uptimemonitor.models.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


public interface HttpMonitorable {    
    /**
     * Checks many Resources and returns a Stream
     * @param resources
     * @return Stream of CompletableFuture of T Log
     */
    public Stream<CompletableFuture<Log>> checkAll(List<Resource> resources);
    
    /**
     * Checks one single Resource and return a CompletableFuture
     * @param resource
     * @return CompletableFuture of T Log
     */
    public CompletableFuture<Log> check(Resource resource);
}
