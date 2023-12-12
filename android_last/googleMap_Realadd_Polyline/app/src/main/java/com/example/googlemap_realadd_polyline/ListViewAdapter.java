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

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemsList=new ArrayList<ListViewItem>();
    Geocoder geocoder;
    private Context mContext;
    public ListViewAdapter(Context context) {
        mContext = context;
        geocoder = new Geocoder(mContext);
    }

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
            convertView=inflater.inflate(R.layout.listview_item,parent,false);
        }

        TextView textView=(TextView) convertView.findViewById(R.id.textView);
        TextView textView2=(TextView) convertView.findViewById(R.id.textView2);
        TextView textView3=(TextView) convertView.findViewById(R.id.textView3);
        TextView textView4=(TextView) convertView.findViewById(R.id.textView4);
        TextView textView5=(TextView)convertView.findViewById(R.id.textView5);

        ListViewItem listViewItem=listViewItemsList.get(position);



        textView.setText(String.valueOf(listViewItem.getNo()));
        textView2.setText(String.valueOf(listViewItem.getLat()));
        textView3.setText(String.valueOf(listViewItem.getLog()));
        textView4.setText(listViewItem.getDate());

        List<Address> address=null;;

        try{
            address=geocoder.getFromLocation(listViewItem.getLat(),listViewItem.getLog(),5);
            textView5.setText(address.get(0).getAddressLine(0).toString());
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return convertView;
    }

    public void addItem(Integer no,Double lat,Double log,String date)
    {
        ListViewItem item=new ListViewItem();

        item.setNo(no);
        item.setLat(lat);
        item.setLog(log);
        item.setDate(date);

        listViewItemsList.add(item);
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
