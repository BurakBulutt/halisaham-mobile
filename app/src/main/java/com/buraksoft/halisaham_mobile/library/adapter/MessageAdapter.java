package com.buraksoft.halisaham_mobile.library.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.model.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;
    private List<MessageModel> messageList;
    private Context context;
    private String loggedInUser;

    public MessageAdapter(Context context, List<MessageModel> messageList, String loggedInUser) {
        this.context = context;
        this.messageList = messageList;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getUser().getEmail().equals(loggedInUser)) {
            return VIEW_TYPE_RIGHT;
        } else {
            return VIEW_TYPE_LEFT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_RIGHT) {
            View view = inflater.inflate(R.layout.message_row_user, parent, false);
            return new RightViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.message_row_other, parent, false);
            return new LeftViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).bind(message);
        } else {
            ((LeftViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<MessageModel> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvMessageContent;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
        }

        public void bind(MessageModel message) {
            tvUserName.setText(message.getUser().getName() + " " + message.getUser().getSurname());
            tvMessageContent.setText(message.getMessage());
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageContent;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
        }

        public void bind(MessageModel message) {
            tvMessageContent.setText(message.getMessage());
        }
    }
}
