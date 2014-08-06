package net.year4000.android;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Server {


    @SerializedName("name")
    public String name;
    @SerializedName("group")
    public JsonObject group; // name + display
    @SerializedName("status")
    public JsonObject status; // description + players(max + online)
    @SerializedName("version")
    public JsonObject vers; // name + protocol
    @SerializedName("favicon")
    public String favicon; // data

    public Server() {}
}
