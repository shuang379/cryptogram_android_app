package edu.gatech.seclass.crypto6300.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import edu.gatech.seclass.crypto6300.R;
import edu.gatech.seclass.crypto6300.ViewCryptogramActivity;
import edu.gatech.seclass.crypto6300.core.Statistics;

public class statsAdapter extends RecyclerView.Adapter<statsAdapter.ViewHolder> {

    private Context context;
    private List<Statistics> allStatistics;

    public statsAdapter(Context context, List allStatistics) {
        this.context = context;
        this.allStatistics = allStatistics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statsAdapter.ViewHolder holder, int position) {
        Statistics item = allStatistics.get(position);
        holder.title.setText(item.getTitle());
        holder.creator.setText("Created by: " + item.getCreatedBy());
        holder.numGames.setText("Number of completed games: " + String.valueOf(item.getNumGames()));
        holder.percentWin.setText("Percentage of wins: " + item.getPercentWin());

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        holder.date.setText("Created on: " + dateFormat.format(item.getCreatedDate()));
    }

    @Override
    public int getItemCount() {
        return allStatistics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public TextView creator;
        public TextView numGames;
        public TextView percentWin;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_statsItem);
            date = itemView.findViewById(R.id.date_statsItem);
            creator = itemView.findViewById(R.id.creator_statsItem);
            numGames = itemView.findViewById(R.id.numGames_statsItem);
            percentWin = itemView.findViewById(R.id.percentWin_statsItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Statistics item = allStatistics.get(position);

            Intent intent = new Intent(context, ViewCryptogramActivity.class);
            intent.putExtra("title", item.getTitle());
            context.startActivity(intent);
//            Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
