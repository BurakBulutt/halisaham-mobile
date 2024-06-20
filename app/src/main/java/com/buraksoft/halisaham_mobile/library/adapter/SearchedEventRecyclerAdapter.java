package com.buraksoft.halisaham_mobile.library.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buraksoft.halisaham_mobile.databinding.SearchedEventRecyclerRowBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;

import java.util.List;

public class SearchedEventRecyclerAdapter extends RecyclerView.Adapter<SearchedEventRecyclerAdapter.Holder> {
    private List<EventModel> events;
    private OnItemClickListener onItemClickListener;

    public SearchedEventRecyclerAdapter(@NonNull List<EventModel> events) {
        this.events = events;
    }

    public interface OnItemClickListener {
        void onItemClick(EventModel eventModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SearchedEventRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchedEventRecyclerAdapter.Holder(SearchedEventRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EventModel event = events.get(position);
        holder.binding.eventName.setText(events.get(position).getTitle());
        //holder.binding.eventExpirationDate.setText(events.get(position).getExpirationDate().toString()); //TODO DATE DÜZELTİLİNCE AKTİF EDİLCEK.
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(event);
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

    public static class Holder extends RecyclerView.ViewHolder{
        private final SearchedEventRecyclerRowBinding binding;
        public Holder(@NonNull SearchedEventRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
