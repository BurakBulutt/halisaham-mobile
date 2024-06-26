package com.buraksoft.halisaham_mobile.library.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buraksoft.halisaham_mobile.databinding.EventRecyclerRowBinding;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.EventModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.Holder> {
    private List<EventModel> events;
    private OnItemClickListener listener;


    public EventRecyclerAdapter(@NonNull List<EventModel> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(EventRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EventModel event = events.get(position);
        if (event.getExpirationDate() != null){
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = new Date(event.getExpirationDate());
            String dateString = format.format(date);
            holder.binding.eventExpirationDate.setText(dateString);
        }else{
            holder.binding.eventExpirationDate.setText("-");
        }
        holder.binding.eventName.setText(event.getTitle());
        holder.binding.areaName.setText(event.getArea().getName());
        if (event.getArea().getPhoto() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(event.getArea().getPhoto(),0,event.getArea().getPhoto().length);
            holder.binding.areaImage.setImageBitmap(bitmap);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(event);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<EventModel> eventModelList){
        this.events = eventModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface OnItemClickListener {
        void onItemClick(EventModel eventModel);
    }

    public void setOnItemClickListener(EventRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder{
        private final EventRecyclerRowBinding binding;
        public Holder(@NonNull EventRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
