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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EditEntryActivity extends AppCompatActivity {

    private DiaryDatabaseHelper dbHelper;
    private int entryId; // ID записи для редактирования
    private String imagePath;        // Исходное изображение из базы данных
    private String updatedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        dbHelper = new DiaryDatabaseHelper(this);

        EditText titleEditText = findViewById(R.id.editTitleEditText);
        EditText contentEditText = findViewById(R.id.editContentEditText);
        ImageView currentImageView = findViewById(R.id.currentImageView);
        Button changeImageButton = findViewById(R.id.changeImageButton);
        Button saveButton = findViewById(R.id.saveChangesButton);


        // Получаем данные из Intent
        entryId = getIntent().getIntExtra("entryId", -1);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String imagePath = getIntent().getStringExtra("imagePath");
        Glide.with(this).load(imagePath).into(currentImageView);

        // Устанавливаем текущие значения в EditText
        titleEditText.setText(title);
        contentEditText.setText(content);
        saveButton.setOnClickListener(v -> {
            String updatedTitle = titleEditText.getText().toString();
            String updatedContent = contentEditText.getText().toString();

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            updateDiaryEntry(entryId, updatedTitle, updatedContent, updatedImagePath != null ? updatedImagePath : imagePath); // Исправлено имя метода
        });
    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            updatedImagePath = getRealPathFromURI(selectedImageUri);
            ImageView currentImageView = findViewById(R.id.currentImageView);
            currentImageView.setImageURI(selectedImageUri);
        }
    }


    private void updateDiaryEntry(int id, String title, String content, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("imagePath", imagePath);

        db.update("diary_entries", values, "id=?", new String[]{String.valueOf(id)});
        Toast.makeText(this, "Запись обновлена", Toast.LENGTH_SHORT).show();
    }
}
