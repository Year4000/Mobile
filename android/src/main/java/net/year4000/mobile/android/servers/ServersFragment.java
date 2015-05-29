package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.year4000.mobile.R;
import net.year4000.mobile.android.MainActivity;

public class ServersFragment extends android.support.v4.app.Fragment {
    private SwipeRefreshLayout swipeView;

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /** When fragment view is created */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_servers, container, false);
        final Activity activity = getActivity();

        swipeView = (SwipeRefreshLayout) layout.findViewById(R.id.swipe);
        ((MainActivity) activity).initializeSwipeView(swipeView);

        return layout;
    }

}
