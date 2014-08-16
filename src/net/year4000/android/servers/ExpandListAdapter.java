package net.year4000.android.servers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.year4000.android.R;

import java.util.List;

public class ExpandListAdapter extends BaseExpandableListAdapter {
    public static final String EXTRA_NAME = "name";
    private Context context;
    private List<ExpandListGroup> groups;

    public ExpandListAdapter(Context context, List<ExpandListGroup> groups) {
        this.context = context;
        this.groups = groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        List<ExpandListChild> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        final ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandlist_child_item, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.serverListChild);
        tv.setText(child.getName() + " " + child.updatePlayerCount());
        tv.setTag(child.getTag());
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(context.getApplicationContext(), DisplayServer.class);
                i.putExtra(EXTRA_NAME, child.getName());
                context.startActivity(i);
            }
        });

        return view;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getItems().size();

    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.expandlist_group_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.servListGroup);
        tv.setText(group.getName() + " " + group.updatePlayerCount());

        return view;
    }

    public boolean hasStableIds() {

        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {

        return true;
    }
}


