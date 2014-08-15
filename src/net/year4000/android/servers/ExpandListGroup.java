package net.year4000.android.servers;

import java.util.ArrayList;

public class ExpandListGroup {

    private String Name;
    private ArrayList<ExpandListChild> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<ExpandListChild> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ExpandListChild> Items, ServersList posts, String id) {
        Items = new ArrayList<ExpandListChild>();
        for (Server server : posts.servers) {
            if (server.group.get("name").toString().equals(id))
                if(!server.group.get("name").toString().startsWith(".")) {
                ExpandListChild child = new ExpandListChild();
                child.setName(server.name);
                child.setTag(null);
                Items.add(child);
            }
        }
        this.Items = Items;
    }

    public void setItems(ArrayList<ExpandListChild> Items) {
        this.Items = Items;
    }

}