package com.example.kuleumbridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TastePlaceListAdapter extends BaseAdapter {


    LayoutInflater layoutInflater = null;
    private ArrayList<TastePlaceListData> listViewData = null;
    private int count = 0;

    public TastePlaceListAdapter(ArrayList<TastePlaceListData> listData)
    {
        listViewData = listData;
        count = listViewData.size();
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            final Context context = parent.getContext();
            if(layoutInflater ==null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }
            convertView = layoutInflater.inflate(R.layout.taste_list_content, parent, false);
        }



        TextView name = convertView.findViewById(R.id.list_name);
        TextView address = convertView.findViewById(R.id.list_address);
        TextView mention = convertView.findViewById(R.id.list_mention);


        name.setText(listViewData.get(position).name);
        address.setText(listViewData.get(position).address);
        mention.setText(listViewData.get(position).mention);

        return convertView;
        }
    }



