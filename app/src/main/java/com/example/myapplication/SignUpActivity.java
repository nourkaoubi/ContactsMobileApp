package com.example.myapplication;

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

public class SignUpActivity extends AppCompatActivity {
TextView loginRedirect;
Intent intent;
EditText edNom;
EditText edEmail;
EditText edMdp;
Button btnSignup;
DataBaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginRedirect=findViewById(R.id.tvLoginRedirect);
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    edNom=findViewById(R.id.edSignupNom);
    edEmail=findViewById(R.id.edSignupEmail);
    edMdp=findViewById(R.id.edSignupPassword);
    btnSignup=findViewById(R.id.signupButton);
    databaseHelper = new DataBaseHelper(this);
    btnSignup.setOnClickListener(v -> {
            String name = edNom.getText().toString();
            String email = edEmail.getText().toString();
            String password = edMdp.getText().toString();

            if (validateInputs(name, email, password)) {

                saveUserData(name, email, password);


                intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(SignUpActivity.this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs correctement.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateInputs(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }


    private void saveUserData(String name, String email, String password) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String sql = "INSERT INTO " + DataBaseHelper.TABLE_USERS + " (" +
                DataBaseHelper.COLUMN_NAME + ", " +
                DataBaseHelper.COLUMN_EMAIL + ", " +
                DataBaseHelper.COLUMN_PASSWORD + ") VALUES (?, ?, ?)";
        db.execSQL(sql, new Object[]{name, email, password});

        db.close();
    }
}