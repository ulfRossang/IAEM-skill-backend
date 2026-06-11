package se.handelsbanken.iaem.seeding.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Startar H2:s webbkonsol på port 8082 vid appstart.
 * Tillgänglig på http://localhost:8082
 * Anslut med: jdbc:h2:mem:epatraktor;MODE=MySQL  (user: sa, password: tom)
 */
@ApplicationScoped
public class H2Console {

    private static final System.Logger LOGGER = System.getLogger(H2Console.class.getName());

    Server server;

    void start(@Observes @Initialized(ApplicationScoped.class) Object event) {
        try {
            server = Server.createWebServer("-webPort", "8082", "-webAllowOthers").start();
            LOGGER.log(System.Logger.Level.INFO, "H2 Console: http://localhost:8082");
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.WARNING, "H2 Console kunde inte startas: " + e.getMessage());
        }
    }

    void stop(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        if (server != null) server.stop();
    }
}
