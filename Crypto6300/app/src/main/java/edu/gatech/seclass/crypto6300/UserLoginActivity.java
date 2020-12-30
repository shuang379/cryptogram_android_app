package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.crypto6300.core.User;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUsername;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        txtUsername = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);

        Button submitButton = findViewById(R.id.btn_submit);
        Button cancelButton = findViewById(R.id.btn_cancel);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                String username = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();

                // TODO: Validate and set error if invalid input
                DBAccess dbAccess = DBAccessProvider.getDBAccess();

                User retrievedUser = dbAccess.getUserByUsername(username);

                if (retrievedUser == null || !retrievedUser.getEmailAddress().equals(email)) {
                    Toast.makeText(UserLoginActivity.this, "Invalid login",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    dbAccess.saveCurrentUser(username);

                    if (username.equals("admin")){
                        Intent intent = new Intent(UserLoginActivity.this,
                                MainMenuAdminActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(UserLoginActivity.this,
                                MainMenuPlayerActivity.class);
                        intent.putExtra("name", username);
                        startActivity(intent);
                    }
                }

                break;

            case R.id.btn_cancel:
                startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                break;
        }
    }
}
