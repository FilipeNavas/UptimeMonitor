# 📟 Uptime Monitor

This application is an **uptime monitor** to monitor third-party API integrations.

To start this app, run **Main**.

- HttpMonitor: responsible for running the checks on the resources and returning a CompletableFuture<Log> or a Stream of CompletableFuture<Log>
- ServicerRunner: responsible for running the tasks on one or more resources
- Log and Resource: model classes to represent a resource/domain and log of the resource after a check
- Main: call the ServiceRunner methods

To test the send SMS feature, provide an extra port in the PORTS constant to fail - such as 8080.