package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.crypto6300.core.Admin;
import edu.gatech.seclass.crypto6300.core.Game;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.exception.EntityAlreadyExistsException;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class UserRegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String ADMIN_USERNAME = "admin";

    private EditText txtUsername;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        txtUsername = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);

        Button submitButton = findViewById(R.id.submitButtonID_registration);
        Button cancelButton = findViewById(R.id.cancelButtonID_registration);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submitButtonID_registration:

                String username = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();

                // TODO: Validate and set error if invalid input
                DBAccess dbAccess = DBAccessProvider.getDBAccess();

                try {
                    if (ADMIN_USERNAME.equalsIgnoreCase(username)) {
                        Admin admin = new Admin(username, email);
                        dbAccess.saveUser(admin);

                        Toast.makeText(UserRegistrationActivity.this, "Admin created",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserRegistrationActivity.this,
                                MainActivity.class));
                    } else {
                        if (username.isEmpty()) {
                            Toast.makeText(UserRegistrationActivity.this, "Invalid registration",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Player player = new Player(username, email);
                        dbAccess.saveUser(player);

                        Toast.makeText(UserRegistrationActivity.this, String.format("User %s created", username),
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserRegistrationActivity.this,
                                MainActivity.class));
                    }

                    dbAccess.saveCurrentUser(username);

                } catch (EntityAlreadyExistsException e) {
                    Toast.makeText(UserRegistrationActivity.this, "User already exists",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                break;

            case R.id.cancelButtonID_registration:
                startActivity(new Intent(UserRegistrationActivity.this,
                        MainActivity.class));
                break;
        }
    }
}
