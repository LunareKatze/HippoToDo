package com.lunarekatze.hippotodo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
    private List<String> mData;
    private List<String> mStatus;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyAdapter(Context context, List<String> data, List<String> statuses){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mStatus = statuses;
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

        String status = mStatus.get(position);
        if(status.equals("done")) {
            holder.checkBox.setChecked(true);
            holder.textView.setTextColor(Color.rgb(128, 128, 128));
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkBox.setChecked(false);
            holder.textView.setTextColor(Color.rgb(0, 0, 0));
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        CheckBox checkBox;

        MyViewHolder (View itemView){   //при отрисовке каждой строки списка эта штука выполняется и
            super(itemView);
            textView = itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.change_status);
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
        mStatus.clear();
    }

    void addAll (List<String> data, List<String> statuses) {
        mData.addAll(data);
        mStatus.addAll(statuses);
    }
}
