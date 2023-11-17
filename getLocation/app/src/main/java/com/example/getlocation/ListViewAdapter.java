package com.example.getlocation;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    // 날짜 위도 경도를 담을 리스트
    private ArrayList<ListViewItem> listViewItems = new ArrayList<>();

    //생성자
    public ListViewAdapter(){

    }


    @Override
    public int getCount() {
        // listViewItems에 몇개의 list가 있는지 확인하는 메서드
        return listViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        // position에 있는 ListViewItem 하나를 반환하는 메서드
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView==null){
            // LayoutInflater를 사용하여 listview_item 레이아웃을 인플레이트하여 converView에 할당.
            // 이 레이아웃은 각 리스트 아이템을 표시하는 데 사용된다.

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
        }

        TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
        TextView textView6 = (TextView) convertView.findViewById(R.id.textView6);
        TextView textView7 = (TextView) convertView.findViewById(R.id.textView7);

        ListViewItem listViewItem = listViewItems.get(position);
        textView5.setText(listViewItem.getDate());
        textView6.setText(String.valueOf(listViewItem.getLat()));
        textView7.setText(String.valueOf(listViewItem.getLog()));


        return convertView;
    }

    public void addItem(String date,Double lat, Double log){
        ListViewItem item = new ListViewItem();

        item.setDate(date);
        item.setLat(lat);
        item.setLog(log);

        listViewItems.add(item);
    }

    public void addArray(ArrayList arrayList){
        listViewItems = arrayList;

    }

    public void clearItem(){
        listViewItems.clear();
    }
}
