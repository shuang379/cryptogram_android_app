package edu.gatech.seclass.crypto6300.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.gatech.seclass.crypto6300.R;
import edu.gatech.seclass.crypto6300.core.PlayerScore;

public class scoreAdapter extends RecyclerView.Adapter<scoreAdapter.ViewHolder> {

    private Context context;
    private List<PlayerScore> scoreItems;

    public scoreAdapter(Context context, List scoreItems) {
        this.context = context;
        this.scoreItems = scoreItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull scoreAdapter.ViewHolder holder, int position) {

        PlayerScore item = scoreItems.get(position);
        holder.name.setText(item.getUsername());
        holder.attempted.setText(String.valueOf(item.getNumAttemptedCryptogram()));
        holder.score.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return scoreItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView attempted;
        public TextView score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.username_score);
            attempted = itemView.findViewById(R.id.attempted_score);
            score = itemView.findViewById(R.id.score_score);
        }
    }
}
