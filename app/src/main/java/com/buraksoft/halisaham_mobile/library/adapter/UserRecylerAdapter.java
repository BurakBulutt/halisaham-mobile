package com.buraksoft.halisaham_mobile.library.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buraksoft.halisaham_mobile.databinding.UserRecylerRowBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.model.UserModel;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRecylerAdapter extends RecyclerView.Adapter<UserRecylerAdapter.Holder> {
    private List<UserModel> userModelList;
    private List<UserProfileModel> userProfileModelList;
    private Boolean isAdmin;
    private String eventId;
    private OnItemClickListener onItemClickListener;


    public UserRecylerAdapter(@NonNull List<UserModel> userModelList,String eventId) {
        this.userModelList = userModelList;
        this.isAdmin = Boolean.FALSE;
        this.userProfileModelList = new ArrayList<>();
        this.eventId = eventId;
    }

    public interface OnItemClickListener {
        void onItemClick(String eventId,UserModel userModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(UserRecylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UserModel userModel = userModelList.get(position);
        holder.binding.nameText.setText(userModel.getName());
        holder.binding.removePersonButton.setVisibility(View.GONE);

        if (Objects.equals(userProfileModelList.isEmpty(),Boolean.FALSE)){
            UserProfileModel userProfileModel = userProfileModelList.stream()
                    .filter(userProfileModel1 -> userProfileModel1.getUserModel().getId().equals(userModel.getId()))
                    .findFirst()
                    .get();
            if (userProfileModel.getPhoto() != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(userProfileModel.getPhoto(),0,userProfileModel.getPhoto().length);
                holder.binding.photoImage.setImageBitmap(bitmap);
            }
        }

        if (isAdmin) {
            holder.binding.removePersonButton.setVisibility(View.VISIBLE);
            holder.binding.removePersonButton.setOnClickListener(v -> {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(eventId,userModel);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<UserModel> users,List<UserProfileModel> userProfileModelList){
        this.userModelList = users;
        this.userProfileModelList = userProfileModelList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<UserModel> users){
        this.userModelList = users;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateView(Boolean isAdmin){
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        private final UserRecylerRowBinding binding;
        public Holder(@NonNull UserRecylerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
