package com.example.personaldiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private final List<DiaryEntry> entries;
    private final Context context;
    private final OnEntryActionListener actionListener;

    public DiaryAdapter(Context context, List<DiaryEntry> entries, OnEntryActionListener actionListener) {
        this.context = context;
        this.entries = entries;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary_entry, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        DiaryEntry entry = entries.get(position);
        holder.titleTextView.setText(entry.getTitle());
        holder.contentTextView.setText(entry.getContent());
        holder.timestampTextView.setText(entry.getTimestamp());
        // Загрузка изображения с помощью Glide
        if (entry.getImagePath() != null) {
            Glide.with(context)
                    .load(entry.getImagePath()) // Путь к изображению
                    .placeholder(R.drawable.placeholder_image) // Изображение по умолчанию
                    .into(holder.entryImageView); // Устанавливаем в ImageView
        } else {
            holder.entryImageView.setImageResource(R.drawable.placeholder_image); // Плейсхолдер
        }

        holder.editButton.setOnClickListener(v -> actionListener.onEdit(entry));
        holder.deleteButton.setOnClickListener(v -> actionListener.onDelete(entry));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, timestampTextView;

        ImageView entryImageView;
        Button editButton, deleteButton;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            entryImageView = itemView.findViewById(R.id.entryImageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnEntryActionListener {
        void onEdit(DiaryEntry entry);
        void onDelete(DiaryEntry entry);
    }
}
