package com.KonDuckJoa.kuleumbridge.Taste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.KonDuckJoa.kuleumbridge.R;

import java.util.ArrayList;

/* 맛집 리스트 어댑터 */

public class TastePlaceListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<TastePlaceListData> tastePlaceListDataArray;
    private int count;

    public TastePlaceListAdapter(ArrayList<TastePlaceListData> tastePlaceListDataArray)
    {
        this.tastePlaceListDataArray = tastePlaceListDataArray;
        count = this.tastePlaceListDataArray.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView ==null)
        {
            final Context context = parent.getContext();

            if(layoutInflater ==null)
            {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            convertView = layoutInflater.inflate(R.layout.taste_list_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.list_name);
        TextView textViewAddress = convertView.findViewById(R.id.list_address);
        TextView textViewOneLineComment = convertView.findViewById(R.id.list_mention);

        textViewName.setText(tastePlaceListDataArray.get(position).name);
        textViewAddress.setText(tastePlaceListDataArray.get(position).address);
        textViewOneLineComment.setText(tastePlaceListDataArray.get(position).oneLineComment);

        return convertView;
    }
}



