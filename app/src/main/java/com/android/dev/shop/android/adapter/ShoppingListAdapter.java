package com.android.dev.shop.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.dev.shop.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-31.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter {
    private ArrayList<String> items;
    private Context context;

    public ShoppingListAdapter(Context context, ArrayList<String > items){
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_list_item,null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListViewHolder holde = (ListViewHolder) holder;
        holde.onBind(position);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView1);
        }

        public void onBind(int position){
            this.textView.setText(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(items==null||items.size()==0)
            return 0;
        return items.size();
    }
}
