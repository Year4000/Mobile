package net.year4000.mobile.android.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.year4000.mobile.R;
import net.year4000.mobile.android.Year4000;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private Context context;
    private View layout;

    /** When fragment is created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = Year4000.appContext;
    }

    /** When fragment view is created */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_news, container, false);

        populateListView();

        return layout;
    }

    public void populateListView() {
        ListView listView = (ListView) layout.findViewById(R.id.list_view);

        List<NewsBlogItem> listItems = getListItems();
        NewsBlogItem listContents[] = new NewsBlogItem[listItems.size()];

        listItems.toArray(listContents);
        listView.setAdapter(getListAdapter(listContents));
    }

    /** Creates the list of blog list items */
    private List<NewsBlogItem> getListItems() {
        List<NewsBlogItem> listItems = new ArrayList<>();

        // This is where we will make the call to get the blogs from api/DB.
        // added a sample to see it in UI
        NewsBlogItem item = new NewsBlogItem("News Blog Coming Soon!", "2015-05-28",
                "This is a sample blog item, news blogs coming soon!");
        listItems.add(item);

        item = new NewsBlogItem("Test Blog", "2015-05-28",
                "Here's an unusually long line of text, which serves only the purpose of " +
                        "seeing how an actual full length blog content may look when the blogs are " +
                        "fully implemented.");
        listItems.add(item);

        return listItems;

    }

    /** Returns adapter for blog list view */
    private ArrayAdapter<NewsBlogItem> getListAdapter(final NewsBlogItem item[]) {

        return new ArrayAdapter<NewsBlogItem>(context, R.layout.news_list_item, item) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.news_list_item, null);
                }

                final TextView title = (TextView) convertView.findViewById(R.id.blog_title);
                title.setText(item[position].getTitle());

                final TextView date = (TextView) convertView.findViewById(R.id.blog_date);
                date.setText(item[position].getDate());

                final TextView content = (TextView) convertView.findViewById(R.id.blog_content);
                content.setText(item[position].getContent());

                return convertView;
            }
        };
    }
}
