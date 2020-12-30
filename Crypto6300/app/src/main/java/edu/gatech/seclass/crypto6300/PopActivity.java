package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.Game;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class PopActivity extends AppCompatActivity implements View.OnClickListener{

    private Button submitButton;
    private Button cancelButton;
    private TextView txtBet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        submitButton = findViewById(R.id.btn_submit_popup);
        cancelButton = findViewById(R.id.btn_cancel_popup);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        txtBet = findViewById(R.id.txt_bet);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit_popup:

                String betString = txtBet.getText().toString();
                int bet;
                try {
                    bet = Integer.parseInt(betString);
                } catch (Exception e) {
                    txtBet.setError("Only integer values allowed");
                    return;
                }

                DBAccess db = DBAccessProvider.getDBAccess();
                Player player = db.getPlayerByUsername(db.getCurrentUser());

                if (bet < 1 || bet > Math.min(player.getScore(), 10)) {
                    String errorText = String.format("Minimum Bet : 1 | Maximum Bet : %s",
                            Math.min(player.getScore(), 10));
                    txtBet.setError(errorText);
                    return;
                }

                GameplayRequest gameplayRequest = player.startCryptogramAttempt(bet);

                if (gameplayRequest == null) {
                    Toast.makeText(PopActivity.this, "No eligible cryptogram found",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PopActivity.this,
                            MainMenuPlayerActivity.class));
                    return;
                }

                startActivity(new Intent(PopActivity.this,
                        SolveCryptogramActivity.class));
                break;
            case R.id.btn_cancel_popup:
                startActivity(new Intent(PopActivity.this, MainMenuPlayerActivity.class));
                break;
        }
    }
}
