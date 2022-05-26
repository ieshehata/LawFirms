package com.app.lawfirms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawfirms.android.R;
import com.lawfirms.android.activities.admin.TipEditorActivity;
import com.lawfirms.android.models.TipModel;
import com.lawfirms.android.utils.SharedData;

import java.util.ArrayList;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {
    private ArrayList<TipModel> mData = new ArrayList<>();
    private Context context;
    private TipListener mListener;
    public TipAdapter(ArrayList<TipModel> data, TipListener listener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_tip, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.content.setText(mData.get(position).getText());

        holder.delete.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<TipModel> getData() {
        return this.mData;
    }

    public void updateData(ArrayList<TipModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title, content;
        ImageButton delete;
        ViewHolder(View itemView, TipListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            delete = itemView.findViewById(R.id.delete);

            view.setOnClickListener(v -> {
                SharedData.currentTip = mData.get(getAdapterPosition());
                Intent intent = new Intent(context, TipEditorActivity.class);
                context.startActivity(intent);
            });

            delete.setOnClickListener(v -> {
                listener.delete(getBindingAdapterPosition());
            });
        }
    }

    public interface TipListener {
        void delete(int position);
    }
}
