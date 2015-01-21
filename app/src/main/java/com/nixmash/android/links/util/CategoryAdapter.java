package com.nixmash.android.links.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nixmash.android.links.R;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private String[] mDataset;
    private OnItemClickListener mListener;
    private int mPosition;

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    /**
     * Custom viewholder for our categories
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View v = vi.inflate(R.layout.drawer_list_item, parent, false);
        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        return new ViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(view, position);
            }
        });
        LinkUtils.selectMaterialDrawerCategory(holder.mTextView, position, mPosition);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static CategoryAdapter newInstance(String[] myDataset, OnItemClickListener listener, int position) {
        return new CategoryAdapter(myDataset, listener, position);
    }

    private CategoryAdapter(String[] myDataset, OnItemClickListener listener, int position) {
        mDataset = myDataset;
        mListener = listener;
        mPosition = position;
    }

}