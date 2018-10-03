package es.source.code.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private GridView gridView;

    List<Map<String, Object>> navLists = new ArrayList<Map<String, Object>>();


    MyAdapter(Context context, GridView gridView, List<Map<String,Object>> navLists) {
        this.context = context;
        this.gridView = gridView;
        this.navLists = navLists;

    }

    @Override
    public Object getItem(int position) {
        return navLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return navLists.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.activity_main_screen_gridview_item, parent, false);
        ImageButton img_icon = convertView.findViewById(R.id.ibGridViewItemImage);
        Button txt_aName = convertView.findViewById(R.id.bGridViewItemButton);
        img_icon.setImageResource((Integer) navLists.get(position).get("icon"));
        txt_aName.setText((CharSequence) navLists.get(position).get("name"));
        return convertView;
    }

}
