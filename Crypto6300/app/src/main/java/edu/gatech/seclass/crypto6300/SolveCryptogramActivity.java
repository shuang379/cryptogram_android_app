package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gatech.seclass.crypto6300.core.Game;
import edu.gatech.seclass.crypto6300.core.GameStatus;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;
import edu.gatech.seclass.crypto6300.util.ParsingUtil;

public class SolveCryptogramActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    private Button cancelButton;
    private Button buttonParseSolve;
    private Button buttonViewPotentialSolution;

    private TextView textCryptoTitle;
    private TextView textRemAttempts;
    private TextView textEncodedPhrase;
    private TextView textUniqueSolve;
    private TextView textPotentialSoln;
    private TextView textEncodedMap;
    private TextView labelScore;
    private TextView labelHint;

    private String validatedConversionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_cryptogram);

        submitButton = findViewById(R.id.submitButtonID_solve);
        cancelButton = findViewById(R.id.cancelButtonID_solve);
        buttonParseSolve = findViewById(R.id.btn_parseSolve);
        buttonViewPotentialSolution = findViewById(R.id.btn_viewPotentialSln);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        buttonParseSolve.setOnClickListener(this);
        buttonViewPotentialSolution.setOnClickListener(this);

        textCryptoTitle = findViewById(R.id.txt_cryptoTitle);
        textRemAttempts = findViewById(R.id.txt_remAttempts);
        textEncodedPhrase = findViewById(R.id.txt_encodedPhrase);
        textUniqueSolve = findViewById(R.id.txt_uniqueSolve);
        textPotentialSoln = findViewById(R.id.txt_potentialSoln);
        textEncodedMap = findViewById(R.id.txt_encodedMap);
        labelScore = findViewById(R.id.lbl_score);
        labelHint = findViewById(R.id.lbl_hintSolve);

        DBAccess dbAccess = DBAccessProvider.getDBAccess();
        GameplayRequest currentGameplay = dbAccess.getCurrentGameplayRequest();
        if (currentGameplay == null) {
            throw new RuntimeException("Fatal Error. No current gameplay");
        }

        textCryptoTitle.setText(currentGameplay.getCryptogram().getTitle());
        String remAttempts = "Remaining Attempts : " + currentGameplay.getPendingAttempts();
        textRemAttempts.setText(remAttempts);
        textEncodedPhrase.setText(currentGameplay.getCryptogram().getEncodedPhrase());

        Player player = dbAccess.getPlayerByUsername(currentGameplay.getPlayerName());
        labelScore.setText(String.valueOf(player.getScore()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_parseSolve:
                String encodedPhrase = textEncodedPhrase.getText().toString();

                List<Character> uniqueCharsList = ParsingUtil.getUniqueCharsString(encodedPhrase);

                StringBuilder sb = new StringBuilder();
                for (char c : uniqueCharsList) {
                    sb.append(c);
                    sb.append(",");
                }

                sb.deleteCharAt(sb.length() - 1);

                textUniqueSolve.setText(sb.toString());
                break;

            case R.id.btn_viewPotentialSln:
                String encodedPhrase1 = textEncodedPhrase.getText().toString();

                Map<Character, Character> map = validateAndGetConversionMap(encodedPhrase1);
                if (map == null) return;

                // Convert
                StringBuilder encryptedPhrase = new StringBuilder();
                for (Character c : encodedPhrase1.toCharArray()) {
                    if (Character.isLetter(c)) {
                        if (Character.isUpperCase(c)) {
                            encryptedPhrase.append(map.get(c));
                        } else {
                            encryptedPhrase.append(Character.toLowerCase(map.get(Character.toUpperCase(c))));
                        }
                    } else {
                        encryptedPhrase.append(c);
                    }
                }

                textPotentialSoln.setText(encryptedPhrase.toString());

                validatedConversionKey = textEncodedMap.getText().toString();

                break;

            case R.id.submitButtonID_solve:

                String currentConversionKey = textEncodedMap.getText().toString();
                String potentialSolution = textPotentialSoln.getText().toString();

                if (validatedConversionKey == null || validatedConversionKey.length() == 0
                        || !validatedConversionKey.equals(currentConversionKey)) {
                    Toast.makeText(SolveCryptogramActivity.this, "Solution has not yet been validated",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Game game = Game.getGame(this);
                DBAccess dbAccess = DBAccessProvider.getDBAccess();

                GameplayRequest resultGameplay = game.checkSolution(dbAccess.getCurrentGameplayRequest(), potentialSolution);
                //dbAccess.saveCurrentGameplay(resultGameplay);

                int currentScore = dbAccess.getPlayerByUsername(resultGameplay.getPlayerName()).getScore();
                System.out.println(currentScore);
                labelScore.setText(String.valueOf(currentScore));

                if (resultGameplay.getGameStatus() == GameStatus.WON) {

                    game.finishGameplay(resultGameplay);

                    Toast.makeText(SolveCryptogramActivity.this, "Correct solution",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SolveCryptogramActivity.this,
                            PopScoreActivity.class));

                } else if (resultGameplay.getGameStatus() == GameStatus.LOST) {

                    game.finishGameplay(resultGameplay);

                    Toast.makeText(SolveCryptogramActivity.this, "All attempts exhausted. Game lost",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SolveCryptogramActivity.this,
                            PopScoreActivity.class));

                } else {
                    Toast.makeText(SolveCryptogramActivity.this, "Wrong solution",
                            Toast.LENGTH_SHORT).show();

                    String remAttempts = "Remaining Attempts : " + resultGameplay.getPendingAttempts();
                    textRemAttempts.setText(remAttempts);

                    if (resultGameplay.getPendingAttempts() <= 2) {
                        labelHint.setText(resultGameplay.getCryptogram().getHint());
                    }
                }

                break;
            case R.id.cancelButtonID_solve:
                startActivity(new Intent(SolveCryptogramActivity.this,
                        MainMenuPlayerActivity.class));
                break;
        }
    }

    private Map<Character, Character> validateAndGetConversionMap(String conversionString) {

        String conversionKeyTo = textEncodedMap.getText().toString();

        if (conversionKeyTo.length() == 0) {
            textEncodedMap.setError("Uncoded letter string cannot be empty");
            return null;
        }

        List<Character> uniqueChars = ParsingUtil.getUniqueCharsString(conversionString);
        String[] encryptCharsSplit = conversionKeyTo.split(",");


        List<Character> encodedCharList = new ArrayList<>();
        for (String c : encryptCharsSplit) {
            if (c.length() == 1 && Character.isLetter(c.charAt(0))) {
                encodedCharList.add(c.charAt(0));
            } else {
                textEncodedMap.setError("Uncoded letter string must contain letters separated by comma");
                return null;
            }
        }

        if (encodedCharList.size() != uniqueChars.size()) {
            textEncodedMap.setError("There must be one uncoded character for each unique character");
            return null;
        }

        Set<Character> encodedSet = new LinkedHashSet<>(encodedCharList);
        if (encodedSet.size() != uniqueChars.size()) {
            textEncodedMap.setError("There must be one uncoded character for each unique character");
            return null;
        }

        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < encodedCharList.size(); i++) {
            if (Character.toUpperCase(uniqueChars.get(i)) == Character.toUpperCase(encodedCharList.get(i))) {
                textEncodedMap.setError("Uncoded character cannot be the same as original character");
                return null;
            }
            map.put(Character.toUpperCase(uniqueChars.get(i)), Character.toUpperCase(encodedCharList.get(i)));
        }
        return map;
    }
}
