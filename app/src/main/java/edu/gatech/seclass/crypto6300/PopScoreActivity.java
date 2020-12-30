package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.seclass.crypto6300.core.GameStatus;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class PopScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblScore, lblResultComment;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_score);

        btnOK = findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(this);

        lblScore = findViewById(R.id.lbl_score);
        lblResultComment = findViewById(R.id.lbl_result_comment);

        DBAccess db =  DBAccessProvider.getDBAccess();
        GameplayRequest currentGameplay = db.getCurrentGameplayRequest();

        if (currentGameplay.getGameStatus() == GameStatus.WON) {
            String message = "Congratulations !!";
            lblResultComment.setText(message);

            Player player = db.getPlayerByUsername(currentGameplay.getPlayerName());

            String scoreMessage = "You score is now : " + player.getScore();
            lblScore.setText(scoreMessage);
        } else {
            String message = "Hard Luck !!";
            lblResultComment.setText(message);

            Player player = db.getPlayerByUsername(currentGameplay.getPlayerName());
            String scoreMessage = "You score is now : " + player.getScore();
            lblScore.setText(scoreMessage);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                startActivity(new Intent(PopScoreActivity.this,
                        MainMenuPlayerActivity.class));
                break;
        }
    }
}
