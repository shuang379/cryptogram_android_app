package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button viewButton, logoutButton;
    private TextView helloMessage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_admin);

        helloMessage = findViewById(R.id.helloID_admin);
        helloMessage.setText("Hello, Administrator!");

        viewButton = findViewById(R.id.btn_stats);
        logoutButton = findViewById(R.id.btn_logout);

        viewButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_stats:
                startActivity(new Intent(MainMenuAdminActivity.this,
                        ViewStatisticsActivity.class));
                break;
            case R.id.btn_logout:
                startActivity(new Intent(MainMenuAdminActivity.this,
                        MainActivity.class));
                break;
        }
    }
}
