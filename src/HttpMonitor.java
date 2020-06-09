import java.io.*;
import java.net.*;
import java.util.*;

class HttpMonitor {

  public static StringBuilder log = new StringBuilder();

  private static boolean isReachable(String host, int port) {
    // Ping the provided IP and port
    try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(host, port), 1000);
        return true;
    } catch (IOException e) {
        return false;
    }
  }

  public static void check(String domain) {
    String ip = null;

    // Get the domain's IP so we can ping it
    try {
      ip = InetAddress.getByName(domain).getHostAddress();
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("Invalid domain provided");
    }

    // Are HTTP and HTTPS reachable?
    boolean http = isReachable(domain, 80);
    boolean https = isReachable(domain, 90);
    
    // Log result to console
    log.append(String.format("%20s", domain));
    log.append(String.format("%18s", ip));
    log.append("\t\tHTTP: ");
    log.append(http ? "UP" : "DOWN!");
    log.append(" | HTTPS: ");
    log.append(https ? "UP" : "DOWN!");
    log.append("\n");
  }

}