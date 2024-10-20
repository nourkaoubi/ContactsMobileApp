package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Accueil extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private FloatingActionButton floatingActionButton;
    private DataBaseHelper databaseHelper;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        //String name = intent.getStringExtra("name");
        currentUserId=userId;
        // Set up RecyclerView and FloatingActionButton
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        databaseHelper = new DataBaseHelper(this);

        // Set up RecyclerView
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(contactList);
        recyclerViewContacts.setAdapter(contactAdapter);

        fetchContactsForUser(userId);

        floatingActionButton.setOnClickListener(v -> {
            Intent addContactIntent = new Intent(Accueil.this, AddContactActivity.class);
            addContactIntent.putExtra("userId", currentUserId);
            startActivityForResult(addContactIntent, 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fetchContactsForUser(currentUserId);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchContactsForUser(currentUserId);
    }
    private void fetchContactsForUser(int userId) {
       contactList.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_CONTACTS + " WHERE " + DataBaseHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                // Get column indices
                int contactIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_CONTACT_ID);
                int contactNameIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_CONTACT_NAME);
                int contactPhoneIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_CONTACT_PHONE);

                // Ensure the column indices are valid
                if (contactIdIndex >= 0 && contactNameIndex >= 0 && contactPhoneIndex >= 0) {
                    // Fetch the data
                    int contactId = cursor.getInt(contactIdIndex);
                    String contactName = cursor.getString(contactNameIndex);
                    String contactPhone = cursor.getString(contactPhoneIndex);

                    // Add the contact to the list
                    contactList.add(new Contact( contactName,contactId, contactPhone));
                }
            } while (cursor.moveToNext());
        }
       // else { Toast.makeText(this, "No contacts found for this user !", Toast.LENGTH_SHORT).show();}

        cursor.close();
        db.close();

        contactAdapter.notifyDataSetChanged();
        if (contactList.isEmpty()) {
            Toast.makeText(this, "No contacts found for this user", Toast.LENGTH_SHORT).show();
        }
    }
}
