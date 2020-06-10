package com.uptimemonitor.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resource {
    
    private String domain;
    private int[] ports;
    private String ip;

    public Resource(String domain, int[] ports) {
        this.domain = domain;
        this.ports = ports;
        this.ip = getIp(domain); //gets ip from domain
    }
    
    //private method for getting the ip whenever a new resource is created
    private String getIp(String domain){
        // Get the domain's IP so we can ping it
        try {
            return ip = InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, "Invalid domain provided", ex);            
            throw new IllegalArgumentException("Invalid domain provided", ex);
        }       
    }
    
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int[] getPorts() {
        return ports;
    }

    public void setPort(int[] ports) {
        this.ports = ports;
    }

    public String getIp() {
        return ip;
    }

}
