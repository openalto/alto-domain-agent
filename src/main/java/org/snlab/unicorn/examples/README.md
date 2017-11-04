# Some Test Examples

## Websocket Client Example with ODL Notification Stream

Make sure you have started an OpenDaylight controller and enabled `odl-netconf-notifications-impl` feature.

After `mvn clean package` in the top level of the project, go to `target/unicorn-server/WEB-INF` directory and run the following command:

``` bash
java -classpath 'lib/*:classes/.' org.snlab.unicorn.examples.SimpleNotificationClient \
    http://localhost:8181 \
    '{"input":{"path":"/opendaylight-inventory:nodes","datastore":"OPERATIONAL","scope":"SUBTREE"}}'
```

You can see the data change notifications once the subtree of `/operational/opendaylight-inventory:nodes` changed.