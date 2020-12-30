package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class ViewCryptogramActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView showTitle, showEncrypted, showSolution, showHint, showDisable;
    private Bundle extras;
    private Button disableButton, returnButton;
    private Cryptogram crypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cryptogram);

        disableButton = findViewById(R.id.disable_viewCrypt);
        returnButton = findViewById(R.id.return_viewCrypt);

        disableButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        showTitle = findViewById(R.id.title_viewCrypt);
        showEncrypted = findViewById(R.id.showEncrypted_viewCrypt);
        showSolution = findViewById(R.id.showSolution_viewCrypt);
        showHint = findViewById(R.id.showHint_viewCrypt);
        showDisable = findViewById(R.id.showDisable_viewCrypt);

        extras = getIntent().getExtras();

        if (extras != null){
            String title = extras.getString("title");
            DBAccess dbAccess = DBAccessProvider.getDBAccess();
            crypt = dbAccess.getCryptogramByTitle(title);

            showTitle.setText(title);
            showEncrypted.setText(crypt.getEncodedPhrase());
            showSolution.setText(crypt.getSolution());
            showHint.setText(crypt.getHint());

            if(crypt.isDisabled()){
                showDisable.setText("Disabled");
            }else if (!crypt.isDisabled()){
                showDisable.setText("Active");
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.return_viewCrypt:
                startActivity(new Intent(ViewCryptogramActivity.this, ViewStatisticsActivity.class));
                break;
            case R.id.disable_viewCrypt:
                if (crypt.isDisabled()) {
                    Toast.makeText(ViewCryptogramActivity.this, "The cryptogram has already been disabled", Toast.LENGTH_SHORT).show();
                } else {
                    Intent popup = new Intent(getApplicationContext(), PenalActivity.class);
                    popup.putExtra("CRYPT_TO_PENALIZE", crypt.getTitle());
                    startActivity(popup);
                }
                break;
        }
    }
}
