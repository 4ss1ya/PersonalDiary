package com.example.personaldiary;

import android.content.Context;
import android.content.Intent;

public class NavigationHelper {
    public static void navigateToAddEntry(Context context) {
        Intent intent = new Intent(context, AddEntryActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToViewEntries(Context context) {
        Intent intent = new Intent(context, ViewEntriesActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToEditEntry(Context context, int entryId, String title, String content) {
        Intent intent = new Intent(context, EditEntryActivity.class);
        intent.putExtra("entryId", entryId);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }
}
