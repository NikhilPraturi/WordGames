package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28/1/18.
 */

public class AdapterRecyclerTwo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> data = new ArrayList<>();
    private ActivityGameTwo mContext;
    private boolean isSelect = false;
    public AdapterRecyclerTwo(ActivityGameTwo context, List<String> list) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {

        //return TYPE here -- check input type
        return 0;
    }

    public void add_data(final String str){

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                data.add(str);
                notifyDataSetChanged();

            }
        });

    }

    public void clear(){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.clear();
                notifyDataSetChanged();
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView text;


        public List<String> data = new ArrayList<>();
        public ViewHolder(View view) {

            super(view);
            mView = view;
            text = (TextView) view.findViewById(R.id.text);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_text_two, parent, false);
        //Log.d("LOG", "view holder created");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.d("LOG", "on bind view holder :" + String.valueOf(position));

        ((ViewHolder) holder).text.setText(data.get(position));


    }

    @Override
    public int getItemCount() {
        //Log.d("LOG", "getItemCount  " + String.valueOf(ratings.size()));
        return data.size();
    }


}