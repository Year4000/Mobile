package net.year4000.mobile.android.servers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class HeadsGridAdapter extends BaseAdapter {

    private Context context;
    private Bitmap[] data = null;
    private String[] nameArray;
    private TextView playerInfo;
    private LinearLayout layoutOfPopup;
    private PopupWindow playerInfoWindow;

    public HeadsGridAdapter(Context context, Bitmap[] data, String[] nameArray) {
        this.context = context;
        this.data = data;
        this.nameArray = nameArray;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup parent) {
        final ImageView headView;
        if (view == null) {
            playerInfo = new TextView(context);
            headView = new ImageView(context);
            headView.setImageBitmap(data[position]);
            headView.setClickable(true);


            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == headView.getId()) {
                        popupInit(headView);
                        playerInfo.setText(nameArray[position]);
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }

                        headView.setBackgroundColor(Color.parseColor("#1e6dc8"));
                        playerInfoWindow.dismiss();
                        playerInfoWindow.showAsDropDown(headView, 0, 0);
                    }
                    else {
                        playerInfoWindow.dismiss();
                    }
                }
            });

        } else {
            headView = (ImageView) view;
        }

        return headView;
    }

    public void popupInit(final View headview) {
        if (layoutOfPopup != null) {
            playerInfoWindow.dismiss();
            layoutOfPopup.removeAllViews();
        }

        layoutOfPopup = new LinearLayout(context);
        playerInfo.setPadding(5, 5, 5, 5);
        playerInfo.setTextSize(20);
        playerInfo.setClickable(true);
        playerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headview.setBackgroundColor(Color.TRANSPARENT);
                playerInfoWindow.dismiss();
            }
        });
        layoutOfPopup.setBackgroundColor(Color.parseColor("#aa000000"));
        layoutOfPopup.addView(playerInfo);
        playerInfoWindow = new PopupWindow(layoutOfPopup, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        playerInfoWindow.setContentView(layoutOfPopup);
    }
}
