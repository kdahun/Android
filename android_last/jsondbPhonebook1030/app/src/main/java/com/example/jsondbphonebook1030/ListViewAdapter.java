package com.example.jsondbphonebook1030;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItems = new ArrayList<>();
    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public ListViewItem getItem(int position) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
        }

        TextView textView1=(TextView) convertView.findViewById(R.id.textView3);
        TextView textView2=(TextView) convertView.findViewById(R.id.textView4);
        TextView textView3=(TextView) convertView.findViewById(R.id.textView5);

        ListViewItem listViewItem = listViewItems.get(position);

        textView1.setText(String.valueOf(listViewItem.getNo()));
        textView2.setText(listViewItem.getName());
        textView3.setText(listViewItem.getTel());


        return convertView;
    }

    public void addItem(Integer no,String name,String tel){
        ListViewItem item = new ListViewItem();
        item.setNo(no);
        item.setName(name);
        item.setTel(tel);
        listViewItems.add(item);
    }

    public void clearItem(){
        listViewItems.clear();
    }

    public void deleteItem(int position){
        listViewItems.remove(position);
    }

    public void updateItem(int position,ListViewItem item){
        listViewItems.set(position-1,item);
    }
}
