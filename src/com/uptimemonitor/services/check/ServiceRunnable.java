package com.uptimemonitor.services.check;

import com.uptimemonitor.models.Resource;
import java.util.concurrent.Executor;


public interface ServiceRunnable {
    public void runOne(Resource resource);
    public void runAll();
    public void runAll(Executor executor, int[] ports);
}
