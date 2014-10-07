package net.year4000.mobile.android.servers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
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
        } else {
            headView = (ImageView) view;
        }

        headView.setImageBitmap(data[position]);
        headView.setClickable(true);

        headView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == headView.getId()) {
                    popupInit(headView);
                    playerInfo.setText(nameArray[position]);
                    headView.setBackgroundColor(Color.parseColor("#1e6dc8"));
                    playerInfoWindow.showAsDropDown(headView, -50, 0);
                } else {
                    playerInfoWindow.dismiss();
                }
                return false;
            }
        });

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

        playerInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                playerInfoWindow.dismiss();
                return false;
            }
        });

        layoutOfPopup.setBackgroundColor(Color.parseColor("#aa000000"));
        layoutOfPopup.addView(playerInfo);
        playerInfoWindow = new PopupWindow(layoutOfPopup, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        playerInfoWindow.setContentView(layoutOfPopup);
        playerInfoWindow.setBackgroundDrawable(new ShapeDrawable());
        playerInfoWindow.setOutsideTouchable(true);
        playerInfoWindow.setTouchable(true);

        playerInfoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                headview.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }
}