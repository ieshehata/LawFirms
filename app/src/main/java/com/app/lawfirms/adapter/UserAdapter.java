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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private final ArrayList<UserModel> mData = new ArrayList<>();
    private final UserListener mUserListener;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");


    public UserAdapter(ArrayList<UserModel> data, UserListener userListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mUserListener = userListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view, mUserListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.owenerName.setText(mData.get(position).getName());
        holder.email.setText(mData.get(position).getEmail());
        holder.phone.setText(mData.get(position).getPhone());

        if(mData.get(position).getState() == 1) { //Active
            holder.blockButton.setVisibility(View.VISIBLE);
            holder.unblockButton.setVisibility(View.GONE);
        }else if(mData.get(position).getState() == -1) { //Blocked
            holder.blockButton.setVisibility(View.GONE);
            holder.unblockButton.setVisibility(View.VISIBLE);
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
        TextView owenerName ,email, phone;
        Button unblockButton, blockButton;
        ImageButton delete;
        LinearLayout actionButtons;

        UserListener listener;
        ViewHolder(View itemView, UserListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            owenerName = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            delete = itemView.findViewById(R.id.delete);

            unblockButton = itemView.findViewById(R.id.unblock);
            blockButton = itemView.findViewById(R.id.block);

            this.listener = listener;

            unblockButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), false);
            });

            blockButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), true);
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteItem(getAdapterPosition());
                }
            });

            view.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });
        }
    }
    public interface UserListener {
        void response(int position, boolean isBlocking);
        void deleteItem(int position);
        void view(int position);


    }
}
