package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateDeleteContactActivity extends AppCompatActivity {
    private EditText edContactName, edContactPhone;
    private Button btnUpdate, btnDelete;
    private DataBaseHelper databaseHelper;
    private int contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_delete_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edContactName = findViewById(R.id.edContactName);
        edContactPhone = findViewById(R.id.edContactPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        databaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        contactId = intent.getIntExtra("contactId", -1);
        String contactName = intent.getStringExtra("contactName");
        String contactPhone = intent.getStringExtra("contactPhone");

        edContactName.setText(contactName);
        edContactPhone.setText(contactPhone);
        btnUpdate.setOnClickListener(v -> {
            String updatedName = edContactName.getText().toString();
            String updatedPhone = edContactPhone.getText().toString();
            databaseHelper.updateContact(contactId, updatedName, updatedPhone);
            Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
            finish();
        });
        btnDelete.setOnClickListener(v -> {
            databaseHelper.deleteContact(contactId);
            Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}