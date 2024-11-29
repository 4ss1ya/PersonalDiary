package com.example.personaldiary;

public class DiaryEntry {
    private final int id;
    private final String title;
    private final String content;
    private final String timestamp;
    private String imagePath;




    public DiaryEntry(int id, String title, String content, String timestamp,String imagePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
