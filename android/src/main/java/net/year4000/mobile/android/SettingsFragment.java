package net.year4000.mobile.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import net.year4000.mobile.R;

public class SettingsFragment extends Fragment {
    private TextView clearAvatars;
    private Activity activity;
    private SharedPreferences preferences;

    /** When fragment is created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** When fragment view is created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        activity = getActivity();
        preferences = activity.getSharedPreferences(Year4000.APP_NAME, Context.MODE_PRIVATE);
        PackageInfo packageInfo = null;

        try {
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView version = (TextView) layout.findViewById(R.id.version_number);
        if (packageInfo != null) {
            version.setText(packageInfo.versionName);
        }
        else {
            version.setText(activity.getResources().getString(R.string.unknown_version));
        }

        clearAvatars = (TextView) layout.findViewById(R.id.clear_avatar_cache);
        formatClearAvatars();

        Spinner avatarCacheSpinner = (Spinner) layout.findViewById(R.id.avatar_cache_spinner);
        avatarCacheSpinner.setSelection(getSpinnerSelection());
        avatarCacheSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAvatarCacheTime(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to be done
            }
        });

        return layout;
    }

    /** Returns the position of the spinner item that holds the current avatar cache time. */
    private int getSpinnerSelection() {
        switch (preferences.getString(Year4000.AVATAR_CACHE_TIME, "Daily")) {
            case "Daily":
                return 0;
            case "Weekly":
                return 1;
            case "Monthly":
                return 2;
        }
        return 0;
    }

    /** Sets the avatar cache time preferences attribute by position of spinner item. */
    private void setAvatarCacheTime(int position) {
        switch (position) {
            case 0:
                preferences.edit().putString(Year4000.AVATAR_CACHE_TIME, "Daily").apply();
                break;
            case 1:
                preferences.edit().putString(Year4000.AVATAR_CACHE_TIME, "Weekly").apply();
                break;
            case 2:
                preferences.edit().putString(Year4000.AVATAR_CACHE_TIME, "Monthly").apply();
                break;
            default:
                preferences.edit().putString(Year4000.AVATAR_CACHE_TIME, "Daily").apply();
                break;
        }
    }

    /** Sets up attributes for the clear avatar cache button. */
    private void formatClearAvatars() {
        if (!Year4000.isHeadsClear()) {
            clearAvatars.setBackgroundColor(Color.parseColor("#1e6dc8"));
            clearAvatars.setText(getResources().getString(R.string.clear_avatar));
            clearAvatars.setClickable(true);
            clearAvatars.setOnClickListener(v -> {
                Year4000.clearHeadsCache();
                Toast.makeText(activity, getResources().getString(R.string.cleared_avatar), Toast.LENGTH_LONG).show();
                formatClearAvatars();
            });
        } else {
            clearAvatars.setBackgroundColor(Color.parseColor("#575757"));
            clearAvatars.setText(getResources().getString(R.string.no_avatars));
            clearAvatars.setClickable(false);
        }
    }
}
