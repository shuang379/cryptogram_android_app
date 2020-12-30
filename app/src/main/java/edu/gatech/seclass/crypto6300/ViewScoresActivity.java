package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.Adapter.scoreAdapter;
import edu.gatech.seclass.crypto6300.core.Game;
import edu.gatech.seclass.crypto6300.core.PlayerScore;

public class ViewScoresActivity extends AppCompatActivity {

    private Button returnButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        recyclerView = findViewById(R.id.recycler_scores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Game game = Game.getGame(this);

        List scores = game.viewPlayerScores();
        List<PlayerScore> scoreItems = new ArrayList<>(scores);

        adapter = new scoreAdapter(this, scoreItems);
        recyclerView.setAdapter(adapter);

        returnButton = findViewById(R.id.returnButtonID_score);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewScoresActivity.this,
                        MainMenuPlayerActivity.class));
            }
        });
    }
}
