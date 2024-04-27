package com.buraksoft.halisaham_mobile.library.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buraksoft.halisaham_mobile.databinding.EventRecyclerRowBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.Holder> {
    private List<EventModel> events;

    public EventRecyclerAdapter(@NonNull List<EventModel> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(EventRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.eventName.setText(events.get(position).getTitle());
        holder.binding.eventExpirationDate.setText(events.get(position).getExpirationDate().toString());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public
    class Holder extends RecyclerView.ViewHolder{
        private EventRecyclerRowBinding binding;
        public Holder(@NonNull EventRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
