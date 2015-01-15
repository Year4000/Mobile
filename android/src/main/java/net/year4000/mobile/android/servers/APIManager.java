package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.Getter;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@Getter
public class APIManager {
    private static APIManager inst;
    public static final Gson GSON = new Gson();
    private static final String API = "https://api.year4000.net/servers";
    private Map<String, Server> servers = new ConcurrentSkipListMap<String, Server>();
    private Map<String, String> groups = new HashMap<String, String>();

    private APIManager() {
        pullAPI();
    }

    public static APIManager get() {
        if (inst == null) {
            inst = new APIManager();
        }

        return inst;
    }

    /** Get the data from the website */
    public static Reader getAPI() {
        try {
            InputStream url = new URI(API).toURL().openStream();
            return new InputStreamReader(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Pull in the API data from the servers */
    public boolean pullAPI() {
        try {
            servers = GSON.fromJson(getAPI(), new TypeToken<Map<String, Server>>(){}.getType());
            grabGroups();
            return true;
        } catch (Exception e) {
            // todo log but do not throw an exception to tomcat...
            return false;
        }
    }

    /** Generate the groups */
    private void grabGroups() {
        groups = new HashMap<String, String>();

        for (Server server : servers.values()) {
            Server.Group group = server.getGroup();
            if (!groups.containsKey(group.getName())) {
                groups.put(group.getName(), group.getDisplay());
            }
        }
    }

    /** Grab the set of servers with the group name */
    public List<Server> getServersByGroupName(String name) {
        List<Server> newList = new ArrayList<Server>();

        for (Server server : servers.values()) {
            if (server.getGroup().getName().equals(name)) {
                newList.add(server);
            }
        }

        return newList;
    }
}
