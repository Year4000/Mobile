package net.year4000.android.servers;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Server {
    /** The name of the server */
    private String name;

    /** The group that this server is with */
    private Group group;

    /** The ping status */
    private StatusResponse status;

    @Data
    @AllArgsConstructor
    public class Group {
        private String name;
        private String display;
    }

    @Data
    @AllArgsConstructor
    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;
        private int time;
    }

    @Data
    @AllArgsConstructor
    public class Players {
        private int max;
        private int online;
        private List<Player> sample;
    }

    @Data
    @AllArgsConstructor
    public class Player {
        private String name;
        private String id;
    }

    @Data
    @AllArgsConstructor
    public class Version {
        private String name;
        private String protocol;
    }
}
