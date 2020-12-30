package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.crypto6300.core.Admin;
import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.core.PlayerScore;
import edu.gatech.seclass.crypto6300.core.Statistics;
import edu.gatech.seclass.crypto6300.core.User;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private Button registerButton;

    private static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextOfApplication = getApplicationContext();

        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_signup);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        DBAccess db = DBAccessProvider.getDBAccess();
        User user = db.getUserByUsername("admin");
        if (user == null) {
            db.clear();
            preFillDB();
        }
    }

    private void preFillDB() {
        DBAccess dbAccess = DBAccessProvider.getDBAccess();

        Player player = new Player("player", "player@sdp.com");
        dbAccess.saveUser(player);
        Player player2 = new Player("player2", "player2@sdp.com");
        dbAccess.saveUser(player2);
        Player player3 = new Player("player3", "player3@sdp.com");
        dbAccess.saveUser(player3);

        Admin admin = new Admin("admin", "admin@sdp.com");
        dbAccess.saveUser(admin);

        String encoded1 = "Spa";
        String solution1 = "Cat";
        String hint1 = "Animal";

        String encoded2 = "qpnfhsbobuf";
        String solution2 = "pomegranate";
        String hint2 = "Fruit";

        String encoded3 = "H knud bqxosnfqzl!";
        String solution3 = "I love cryptogram!";
        String hint3 = "Do you love me?";

        Cryptogram cryptogram1 = new Cryptogram("Cryptogram1", solution1, encoded1, hint1, player.getUsername());
        dbAccess.saveCryptogram(cryptogram1);
        Cryptogram cryptogram2 = new Cryptogram("Cryptogram2", solution2, encoded2, hint2, player2.getUsername());
        dbAccess.saveCryptogram(cryptogram2);
        Cryptogram cryptogram3 = new Cryptogram("Cryptogram3", solution3, encoded3, hint3, player3.getUsername());
        dbAccess.saveCryptogram(cryptogram3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Intent loginIntent = new Intent(getBaseContext(), UserLoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.btn_signup:
                Intent registrationIntent = new Intent(getBaseContext(), UserRegistrationActivity.class);
                startActivity(registrationIntent);
                break;
        }
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
