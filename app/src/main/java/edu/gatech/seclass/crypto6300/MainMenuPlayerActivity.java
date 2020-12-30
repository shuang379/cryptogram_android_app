package edu.gatech.seclass.crypto6300;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.crypto6300.core.GameStatus;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class MainMenuPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private DBAccess dbAccess;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_player);

        dbAccess = DBAccessProvider.getDBAccess();

        TextView helloMessage = findViewById(R.id.helloID_player);
        String message = String.format("Hello, %s!", dbAccess.getCurrentUser());
        helloMessage.setText(message);

        Button createButton = findViewById(R.id.btn_create);
        Button solveButton = findViewById(R.id.btn_solve);
        Button viewScoreButton = findViewById(R.id.btn_view_scores);
        Button logoutButton = findViewById(R.id.btn_logout);

        createButton.setOnClickListener(this);
        solveButton.setOnClickListener(this);
        viewScoreButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                startActivity(new Intent(MainMenuPlayerActivity.this,
                        CreateCryptogramActivity.class));
                break;
            case R.id.btn_solve:

                Player player = dbAccess.getPlayerByUsername(dbAccess.getCurrentUser());
                GameplayRequest playerCurrentGameplay = player.getCurrentGameplay();

                if (playerCurrentGameplay != null && playerCurrentGameplay.getGameStatus() == GameStatus.ACTIVE) {

                    dbAccess.saveCurrentGameplay(playerCurrentGameplay);

                    startActivity(new Intent(MainMenuPlayerActivity.this,
                            SolveCryptogramActivity.class));

                } else {
                    Intent popup = new Intent(getApplicationContext(), PopActivity.class);
                    startActivity(popup);
                }

                break;
            case R.id.btn_view_scores:
                startActivity(new Intent(MainMenuPlayerActivity.this,
                        ViewScoresActivity.class));
                break;
            case R.id.btn_logout:
                startActivity(new Intent(MainMenuPlayerActivity.this,
                        MainActivity.class));
                break;
        }
    }
}
