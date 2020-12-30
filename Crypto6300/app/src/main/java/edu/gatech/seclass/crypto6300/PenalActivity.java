package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class PenalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    private Button cancelButton;
    private TextView txtPenal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penal);

        submitButton = findViewById(R.id.btn_submit_penal);
        cancelButton = findViewById(R.id.btn_cancel_penal);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        txtPenal = findViewById(R.id.txt_penal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit_penal:

                String penaltyString = txtPenal.getText().toString();
                int penalty;
                try {
                    penalty = Integer.parseInt(penaltyString);
                } catch (Exception e) {
                    txtPenal.setError("Penalty must be an integer");
                    return;
                }

                if (penalty > 10) {
                    txtPenal.setError("Penalty cannot be greater than 10");
                    return;
                }

                String cryptToPenalizeTitle = getIntent().getStringExtra("CRYPT_TO_PENALIZE");
                Cryptogram cryptToPenalize = DBAccessProvider.getDBAccess().getCryptogramByTitle(cryptToPenalizeTitle);

                String creator = cryptToPenalize.getCreatedBy();
                Player player = DBAccessProvider.getDBAccess().getPlayerByUsername(creator);

                if (penalty > player.getScore()) {
                    String message = "Penalty cannot be greater than player's score (" + player.getScore() + ")";
                    txtPenal.setError(message);
                    return;
                }

                cryptToPenalize.setDisabled(true);
                player.addToScore(-1 * penalty);

                DBAccessProvider.getDBAccess().updateCryptogram(cryptToPenalize);
                DBAccessProvider.getDBAccess().updateUser(player);

                Toast.makeText(PenalActivity.this, "The cryptogram is disabled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PenalActivity.this, MainMenuAdminActivity.class));

                break;
            case R.id.btn_cancel_penal:
                startActivity(new Intent(PenalActivity.this, MainMenuAdminActivity.class));
                break;
        }
    }
}
