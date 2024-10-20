package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity {

    EditText ednom, edmdp;
    private Button btnval;

    TextView signupRedirect;
    Intent intent;
    DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         signupRedirect = findViewById(R.id.tvSignupRedirect);


        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        edmdp=findViewById(R.id.edmdp_auth);
        ednom=findViewById(R.id.ednom_auth);
        btnval=findViewById(R.id.btnval_auth);
        databaseHelper = new DataBaseHelper(this);

        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ednom.getText().toString();
                String password = edmdp.getText().toString();

                if (validateInputs(name, password)) {
                    if (checkUserCredentials(name, password)) {
                        int userId = getUserIdFromPreferences();


                        Intent intent = new Intent(MainActivity.this, Accueil.class);
                        intent.putExtra("userId", userId);

                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "Bienvenue " + name, Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validateInputs(String name, String password) {
        return !(name.isEmpty() || password.isEmpty());
    }

    private boolean checkUserCredentials(String name, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_USERS + " WHERE " +
                DataBaseHelper.COLUMN_NAME + "=? AND " + DataBaseHelper.COLUMN_PASSWORD + "=?", new String[]{name, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID));

            saveUserIdToPreferences(userId);

            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }
    private void saveUserIdToPreferences(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }
    private int getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }
}