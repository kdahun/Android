package com.example.googlemap_realadd_polyline;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter2 extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemsList=new ArrayList<ListViewItem>();

    @Override
    public int getCount() {
        return listViewItemsList.size();
    }

    @Override
    public ListViewItem getItem(int position) {
        return listViewItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context=parent.getContext();
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listview_menu,parent,false);
        }

        TextView textView6=(TextView)convertView.findViewById(R.id.textView6);

        ListViewItem listViewItem=listViewItemsList.get(position);


        textView6.setText(listViewItem.getDate());



        return convertView;
    }

    public void addItem(Integer no,Double lat,Double log,String date)
    {
        ListViewItem item=new ListViewItem();

        boolean sw = true;
        for(int i=0;i<listViewItemsList.size();i++){
            if(date.equals(listViewItemsList.get(i).getDate())){
                sw=false;
            }
        }
        if(sw) {
            item.setNo(no);
            item.setLat(lat);
            item.setLog(log);
            item.setDate(date);
            listViewItemsList.add(item);
        }
    }

    public void updateItem(int position,ListViewItem item)
    {
        listViewItemsList.set(position-1,item);
    }

    public  void deleteItem(int position)
    {
        listViewItemsList.remove(position);
    }

    public void clearItem()
    {
        listViewItemsList.clear();
    }
}
