package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.core.User;
import edu.gatech.seclass.crypto6300.exception.EntityAlreadyExistsException;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;
import edu.gatech.seclass.crypto6300.util.ParsingUtil;

public class CreateCryptogramActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textTitle, textSolution, textEncoded;
    private TextView textHint, labelUnique, labelResult;
    private Button createButton, cancelButton, parseButton, viewButton;
    private String validatedSolution, validatedPhrase;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cryptogram);

        createButton = findViewById(R.id.btn_create);
        cancelButton = findViewById(R.id.btn_submit_popup);
        parseButton = findViewById(R.id.btn_parse);
        viewButton = findViewById(R.id.btn_viewphrase);

        textTitle = findViewById(R.id.txt_title);
        textSolution = findViewById(R.id.txt_solution);
        labelUnique = findViewById(R.id.lbl_unique);
        labelResult = findViewById(R.id.lbl_result);
        textEncoded = findViewById(R.id.txt_encoded);
        textHint = findViewById(R.id.txt_hint);

        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        parseButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);

        sp = getSharedPreferences("login", MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_parse:

                String title = textTitle.getText().toString();
                String solution1 = textSolution.getText().toString();

                if (title.length() == 0 || solution1.length() == 0) {
                    Toast.makeText(CreateCryptogramActivity.this,
                            "Please enter title and solution", Toast.LENGTH_LONG).show();
                } else {
                    /*Toast.makeText(CreateCryptogramActivity.this,
                            "valid input", Toast.LENGTH_SHORT).show();*/
                    List<Character> uniqueCharsList = ParsingUtil.getUniqueCharsString(solution1);

                    StringBuilder sb = new StringBuilder();
                    for (char c : uniqueCharsList) {
                        sb.append(c);
                        sb.append(",");
                    }

                    sb.deleteCharAt(sb.length() - 1);

                    labelUnique.setText(sb.toString());
                }

                break;

            case R.id.btn_viewphrase:
                /*Toast.makeText(CreateCryptogramActivity.this, "view button clicked",
                        Toast.LENGTH_SHORT).show();*/

                String solution = textSolution.getText().toString();

                Map<Character, Character> map = validateAndGetConversionMap(solution);
                if (map == null) return;

                // Convert
                StringBuilder encryptedPhrase = new StringBuilder();
                for (Character c : solution.toCharArray()) {
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

                labelResult.setText(encryptedPhrase.toString());

                validatedPhrase = encryptedPhrase.toString();
                validatedSolution = solution;

                break;
            case R.id.btn_create:

                String hint = textHint.getText().toString();
                if (hint.length() == 0) {
                    textHint.setError("Hint cannot be empty");
                    return;
                }

                String title1 = textTitle.getText().toString();
                if (title1.length() == 0) {
                    textTitle.setError("Title cannot be empty");
                    return;
                }

                String currentSolution = textSolution.getText().toString();
                String currentEncodedPhrase = labelResult.getText().toString();

                if ((validatedSolution == null || validatedSolution.length() == 0)
                        || (validatedPhrase == null || validatedPhrase.length() == 0)
                        || !validatedSolution.equals(currentSolution)
                        || !validatedPhrase.equals(currentEncodedPhrase)) {

                    Toast.makeText(CreateCryptogramActivity.this,
                            "Solution has not yet been validated. Click buttons to validate.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Toast.makeText(this, "create clicked", Toast.LENGTH_SHORT).show();

                try {
                    handleCreateCryptogram(title1, currentSolution, currentEncodedPhrase, hint);
                } catch (EntityAlreadyExistsException e) {
                    Toast.makeText(CreateCryptogramActivity.this, "Cryptogram with this name already exists",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(CreateCryptogramActivity.this, "Successfully created cryptogram with unique title",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateCryptogramActivity.this,
                        MainMenuPlayerActivity.class));
                break;
            case R.id.btn_submit_popup:
                startActivity(new Intent(CreateCryptogramActivity.this,
                        MainMenuPlayerActivity.class));
                break;
        }
    }

    private Map<Character, Character> validateAndGetConversionMap(String conversionString) {

        String encryptChars = textEncoded.getText().toString();

        if (encryptChars.length() == 0) {
            textEncoded.setError("Encoded letter string cannot be empty");
            return null;
        }

        List<Character> uniqueChars = ParsingUtil.getUniqueCharsString(conversionString);
        String[] encryptCharsSplit = encryptChars.split(",");


        List<Character> encodedCharList = new ArrayList<>();
        for (String c : encryptCharsSplit) {
            if (c.length() == 1 && Character.isLetter(c.charAt(0))) {
                encodedCharList.add(c.charAt(0));
            } else {
                textEncoded.setError("Encoded letter string must contain letters separated by comma");
                return null;
            }
        }

        if (encodedCharList.size() != uniqueChars.size()) {
            textEncoded.setError("There must be one encoded character for each unique character");
            return null;
        }

        Set<Character> encodedSet = new LinkedHashSet<>(encodedCharList);
        if (encodedSet.size() != uniqueChars.size()) {
            textEncoded.setError("There must be one encoded character for each unique character");
            return null;
        }

        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < encodedCharList.size(); i++) {
            if (Character.toUpperCase(uniqueChars.get(i)) == Character.toUpperCase(encodedCharList.get(i))) {
                textEncoded.setError("Encoded character cannot be the same as original character");
                return null;
            }
            map.put(Character.toUpperCase(uniqueChars.get(i)), Character.toUpperCase(encodedCharList.get(i)));
        }
        return map;
    }

    private void handleCreateCryptogram(String title, String solution, String encodedPhase, String hint) {
        // String createdBy = sp.getString("username", "");
        DBAccess dbAccess = DBAccessProvider.getDBAccess();
        String createdBy = dbAccess.getCurrentUser();

        Player player = dbAccess.getPlayerByUsername(createdBy);
        player.addToScore(5);
        dbAccess.updateUser(player);

        Cryptogram cryptogram = new Cryptogram(title, solution, encodedPhase, hint, createdBy);

        dbAccess.saveCryptogram(cryptogram);

        User user =  dbAccess.getUserByUsername(createdBy);
        if (!(user instanceof Player)) {
            throw new RuntimeException("Fatal Error");
        }
    }
}
