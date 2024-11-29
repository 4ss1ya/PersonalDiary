package com.example.personaldiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEntryActivity extends AppCompatActivity {

    private DiaryDatabaseHelper dbHelper;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(v -> pickImage());
        ImageView selectedImageView = findViewById(R.id.entryImageView);
        Button saveButton = findViewById(R.id.addEntryButton);

        addImageButton.setOnClickListener(v -> pickImage());

        saveButton.setOnClickListener(v -> {
            String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
            String content = ((EditText) findViewById(R.id.contentEditText)).getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            saveEntry(title, content, imagePath);
        });

        dbHelper = new DiaryDatabaseHelper(this);
        setupViewEntriesButton();
        setupAddEntryButton();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromURI(selectedImageUri);
            ImageView selectedImageView = findViewById(R.id.entryImageView);
            selectedImageView.setImageURI(selectedImageUri);
            this.imagePath = imagePath;
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
        return null;
    }

    private void saveEntry(String title, String content, String imagePath) {
        SQLiteDatabase db = new DiaryDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("imagePath", imagePath);
        values.put("timestamp", System.currentTimeMillis());

        db.insert("diary_entries", null, values);
        Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupViewEntriesButton() {
        Button viewEntriesButton = findViewById(R.id.viewEntriesButton);
        viewEntriesButton.setOnClickListener(view -> NavigationHelper.navigateToViewEntries(AddEntryActivity.this));
    }

    private void setupAddEntryButton() {
        Button addEntryButton = findViewById(R.id.addEntryButton);
        addEntryButton.setOnClickListener(view -> {
            EditText titleEditText = findViewById(R.id.titleEditText);
            EditText contentEditText = findViewById(R.id.contentEditText);

            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            addDiaryEntry(title, content);
        });
    }
    private void addDiaryEntry(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO diary_entries (title, content, timestamp) VALUES (?, ?, datetime('now'))",
                new Object[]{title, content});
        Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
    }
}