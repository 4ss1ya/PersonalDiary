package com.example.personaldiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewEntriesActivity extends AppCompatActivity {

    private DiaryDatabaseHelper dbHelper;
    private DiaryAdapter adapter;
    private List<DiaryEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);

        dbHelper = new DiaryDatabaseHelper(this);
        entries = loadDiaryEntries(); // Загружаем данные из базы

        RecyclerView recyclerView = findViewById(R.id.diaryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаём адаптер
        adapter = new DiaryAdapter(this, entries, new DiaryAdapter.OnEntryActionListener() {

            @Override
            public void onEdit(DiaryEntry entry) {
                // Логика редактирования
                NavigationHelper.navigateToEditEntry(ViewEntriesActivity.this, entry.getId(), entry.getTitle(), entry.getContent());
            }

            @Override
            public void onDelete(DiaryEntry entry) {
                // Логика удаления
                deleteDiaryEntry(entry.getId());
                entries.remove(entry);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        setupBackButton();
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backButton); // Найти кнопку "Назад" по ID
        backButton.setOnClickListener(view -> finish()); // Закрыть текущую активность
    }

    private List<DiaryEntry> loadDiaryEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM diary_entries ORDER BY timestamp DESC", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"));

            entries.add(new DiaryEntry(id, title, content,imagePath, timestamp));
        }
        cursor.close();
        return entries;
    }

    private void deleteDiaryEntry(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("diary_entries", "id=?", new String[]{String.valueOf(id)});
    }
}
