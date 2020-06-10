package com.uptimemonitor.models;

import java.util.Map;

/**
 * Class Log is used to store the value of a status check in a resource/domain
 * @author filip
 */
public class Log {

    private Resource resource;
    private Map<Integer, Boolean> portStatus;
    
    public Log(Resource resource, Map portStatus) {
        this.resource = resource;
        this.portStatus = portStatus;
    }
    
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Map<Integer, Boolean> getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(Map<Integer, Boolean> portStatus) {
        this.portStatus = portStatus;
    }
    
    @Override
    public String toString() {
        
        StringBuilder log = new StringBuilder();
        log.append(String.format("%20s", resource.getDomain()));
        log.append(String.format("%18s", resource.getIp()));
        
        //iterates over the map to create the report
        //in this case shows the port number and its status
        //to show the protocol we could use an object insted of just the port integer
        portStatus.forEach((Integer port, Boolean status) -> {
            log.append("\t\t").append(port).append(": ");
            log.append(status ? "UP" : "DOWN!");
        });

        return log.toString();
        
    }
}
