package net.year4000.android.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class ExpandListGroup {
    private String name;
    private List<ExpandListChild> items = new ArrayList<ExpandListChild>();

    public ExpandListGroup(Map.Entry<String, String> entry) {
        name = entry.getValue();

        for (Server server : APIManager.get().getServersByGroupName(entry.getValue())) {
            items.add(new ExpandListChild(server));
        }
    }

    public void setItems(List<ExpandListChild> items) {
        this.items = items;
    }
}
