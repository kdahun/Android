package com.example.logindb_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemsList=new ArrayList<ListViewItem>();
    public ListViewAdapter() {
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

        TextView textView6=(TextView) convertView.findViewById(R.id.textView5);
        TextView textView7=(TextView) convertView.findViewById(R.id.textView6);
        TextView textView8=(TextView) convertView.findViewById(R.id.textView7);

        ListViewItem listViewItem=listViewItemsList.get(position);
        textView6.setText(String.valueOf(listViewItem.getNo()));
        textView7.setText(listViewItem.getId());
        textView8.setText(listViewItem.getPw());
        return convertView;
    }

    public void addItem(Integer no,String name,String tel)
    {
        ListViewItem item=new ListViewItem();

        item.setNo(no);
        item.setId(name);
        item.setPw(tel);
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
