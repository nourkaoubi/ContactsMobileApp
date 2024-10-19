package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class AddContactActivity extends AppCompatActivity {

    private EditText edContactName, edContactPhone;
    private TextView tvBackToList;
    private Button btnSaveContact;
    DataBaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edContactName =findViewById(R.id.edContactName);
        edContactPhone = findViewById(R.id.edContactPhone);
        btnSaveContact = findViewById(R.id.btnSaveContact);
        tvBackToList=findViewById(R.id.tvBackToList);

        databaseHelper = new DataBaseHelper(this);
tvBackToList.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(AddContactActivity.this,Accueil.class);
        startActivity(intent);
    }
});
        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = edContactName.getText().toString();
                String contactPhone = edContactPhone.getText().toString();
                int userId = getCurrentUserId();

                if (!contactName.isEmpty() && !contactPhone.isEmpty() && userId != -1) {
                    databaseHelper.insertContact(contactName, contactPhone, userId);
                    Toast.makeText(AddContactActivity.this, "Contact ajouté !", Toast.LENGTH_SHORT).show();
                    //Intent intent1=new Intent(AddContactActivity.this, Accueil.class);
                    //startActivity(intent1);
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddContactActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

}


