package com.lunarekatze.hippotodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {

    //private ArrayAdapter<String> mAdapter;
    private List<String> mData;
    //private String[] mDataset;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    MyAdapter(Context context, List<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_todo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String task = mData.get(position);
        holder.textView.setText(task);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
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

    public void clear() {
        //int size = data.size();
        mData.clear();
        //notifyItemRangeRemoved(0, size);
    }

    // Create new views (invoked by the layout manager)
/*    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
    int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }*/
    // Replace the contents of a view (invoked by the layout manager)
    // Return the size of your dataset (invoked by the layout manager)

    public void addAll (List<String> data) {
        //int size = data.size();
        mData.addAll(data);
        //notifyItemRangeRemoved(0, size);
    }
}
