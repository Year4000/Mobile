package net.year4000.android;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServersListDeserializer implements JsonDeserializer<ServersList> {

        @Override
        public ServersList deserialize(JsonElement element, Type type,
                                       JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            List<Server> servers = new ArrayList<Server>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Server server = context.deserialize(entry.getValue(), Server.class);
                servers.add(server);
            }
            return new ServersList(servers);
        }

}

