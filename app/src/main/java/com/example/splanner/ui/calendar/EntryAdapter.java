package com.example.splanner.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splanner.R;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private List<Entry> entryList;
    private OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(Entry entry);
    }

    public EntryAdapter(List<Entry> entryList, OnEntryClickListener onEntryClickListener) {
        this.entryList = entryList;
        this.onEntryClickListener = onEntryClickListener;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        holder.titleTextView.setText(entry.getTitle());
        holder.descriptionTextView.setText(entry.getDescription());
        holder.itemView.setOnClickListener(v -> onEntryClickListener.onEntryClick(entry));
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.entryTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.entryDescriptionTextView);
        }
    }
}
