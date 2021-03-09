package com.lunarekatze.hippotodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyAdapter(Context context, List<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_todo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String task = mData.get(position);
        holder.textView.setText(task);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        MyViewHolder (View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.task_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if (mClickListener != null) mClickListener.onItemClick (view, getAdapterPosition());
        }
    }

    String getItem (int id){
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void clear() {
        mData.clear();
    }

    void addAll (List<String> data) {
        mData.addAll(data);
    }
}
