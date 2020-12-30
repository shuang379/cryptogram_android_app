package edu.gatech.seclass.crypto6300;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.Adapter.statsAdapter;
import edu.gatech.seclass.crypto6300.core.Game;
import edu.gatech.seclass.crypto6300.core.PlayerScore;
import edu.gatech.seclass.crypto6300.core.Statistics;
import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class ViewStatisticsActivity extends AppCompatActivity {

    private Button returnButton;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        recyclerView = findViewById(R.id.recycler_stats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Game game = Game.getGame(this);

        List stats = game.viewCryptogramStatistics();
        List<Statistics> allStatistics = new ArrayList<>(stats);

        adapter = new statsAdapter(this, allStatistics);
        recyclerView.setAdapter(adapter);

        returnButton = findViewById(R.id.return_statistics);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewStatisticsActivity.this,
                        MainMenuAdminActivity.class));
            }
        });
    }
}
