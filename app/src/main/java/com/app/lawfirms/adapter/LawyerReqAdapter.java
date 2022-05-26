package com.app.lawfirms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lawfirms.R;
import com.app.lawfirms.models.LawyersReqModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LawyerReqAdapter extends RecyclerView.Adapter<LawyerReqAdapter.ViewHolder> {
    private final ArrayList<LawyersReqModel> mData = new ArrayList<>();
    private final LawyerReqListener mListener;
    private Context context;
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
    @SuppressLint("SimpleDateFormat")

    public LawyerReqAdapter(ArrayList<LawyersReqModel> data, LawyerReqListener listener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_lawyer_request, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint({"UseCompatTextViewDrawableApis", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getLawyer().getName());
        holder.email.setText(mData.get(position).getLawyer().getEmail());
        holder.phone.setText(mData.get(position).getLawyer().getPhone());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<LawyersReqModel> getData() {
        return this.mData;
    }

    public void updateData(ArrayList<LawyersReqModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView date, name, email,  phone;
        Button acceptButton, rejectButton, show;
        LawyerReqListener mListener;
        ViewHolder(View itemView, LawyerReqListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            acceptButton = itemView.findViewById(R.id.accept);
            rejectButton = itemView.findViewById(R.id.reject);
            show = itemView.findViewById(R.id.show);

            this.mListener = listener;

            view.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });

            show.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });

            acceptButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), true);
            });

            rejectButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), false);
            });
        }
    }

    public interface LawyerReqListener {
        void response(int position, boolean isAccepted);
        void view(int position);
    }
}