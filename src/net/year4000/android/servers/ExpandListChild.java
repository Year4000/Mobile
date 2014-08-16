package net.year4000.android.servers;

import lombok.Data;

@Data
public class ExpandListChild {
    private String name;
    private String tag;

    public ExpandListChild(Server server) {
        name = server.getName();
        tag = server.getGroup().getDisplay();
    }
}