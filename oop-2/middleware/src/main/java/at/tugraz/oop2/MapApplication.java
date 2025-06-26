package at.tugraz.oop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;


@SpringBootApplication
public class MapApplication {
    public static void main(String[] args) {
        var middle_port = System.getenv().getOrDefault("JMAP_MIDDLEWARE_PORT", "8010");
        int port_middle;
        try {
            port_middle = Integer.parseInt(middle_port);
        }
        catch (NumberFormatException e) {
            port_middle = 8010;
        }
        if(port_middle < 0 || port_middle > 65535)
        {
            port_middle = 8010;
        }
        var backend_port = System.getenv().getOrDefault("JMAP_BACKEND_TARGET", "localhost:8020");
        if (backend_port.isEmpty()) {
            backend_port = "localhost:8020";
        }

        MapLogger.middlewareStartup(port_middle, backend_port);

        var app = new SpringApplication(MapApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", port_middle));
        app.run();
        gRPCClient Client = new gRPCClient(backend_port);
    }
}
