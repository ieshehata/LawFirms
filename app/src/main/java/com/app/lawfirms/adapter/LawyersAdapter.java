package com.app.lawfirms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lawfirms.R;
import com.app.lawfirms.models.UserModel;
import com.app.lawfirms.utils.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LawyersAdapter extends RecyclerView.Adapter<LawyersAdapter.ViewHolder> {
    private ArrayList<UserModel> mData = new ArrayList<>();
    private LawyerListener mLawyerListener;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");

    public LawyersAdapter(ArrayList<UserModel> data, LawyerListener lawyerListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mLawyerListener = lawyerListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_lawyer, parent, false);
        return new ViewHolder(view, mLawyerListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.email.setText(mData.get(position).getEmail());
        holder.phone.setText(mData.get(position).getPhone());
        if(mData.get(position).getState() == 1) { //Active
            holder.blockButton.setVisibility(View.VISIBLE);
            holder.unblockButton.setVisibility(View.GONE);
        }else if(mData.get(position).getState() == -1) { //Blocked
            holder.blockButton.setVisibility(View.GONE);
            holder.unblockButton.setVisibility(View.VISIBLE);
        }
        if(SharedData.userType == 1) {
            holder.actionButtons.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }else {
            holder.actionButtons.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<UserModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(UserModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<UserModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        LinearLayout actionButtons;
        TextView name, email,  phone;
        Button unblockButton, blockButton;
        ImageButton delete;
        LawyerListener listener;
        ViewHolder(View itemView, LawyerListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            actionButtons = itemView.findViewById(R.id.action_buttons);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            delete = itemView.findViewById(R.id.delete);
            unblockButton = itemView.findViewById(R.id.unblock);
            blockButton = itemView.findViewById(R.id.block);
            this.listener = listener;

            view.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });

            unblockButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), false);
            });

            blockButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), true);
            });

            delete.setOnClickListener(v -> listener.deleteItem(getAdapterPosition()));
        }
    }

    public interface LawyerListener {
        void response(int position, boolean isBlocking);
        void view(int position);
        void deleteItem(int position);
    }
}