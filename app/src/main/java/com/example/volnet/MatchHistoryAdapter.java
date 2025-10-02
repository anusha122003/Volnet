package com.example.volnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volnet.R;
// FIX 1: Changed import to use your confirmed model class, Match1
import com.example.volnet.app.model.Match1;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

// FIX 2 & 3: Updated all references from Match to Match1
public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.MatchViewHolder> {

    private final List<Match1> matchList;
    private final Consumer<Integer> onDeleteClickListener;
    private final Consumer<Match1> onExportClickListener; // Use Match1 here
    private int expandedPosition = -1;

    public MatchHistoryAdapter(
            Context context,
            List<Match1> matchList, // Use List<Match1> here
            Consumer<Integer> onDeleteClickListener,
            Consumer<Match1> onExportClickListener // Use Consumer<Match1> here
    ) {
        this.matchList = matchList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onExportClickListener = onExportClickListener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        // Use Match1 here
        Match1 match = matchList.get(position);

        // --- HEADER BINDING ---
        String teamA = match.getTeamAName();
        String teamB = match.getTeamBName();
        String winnerName = match.getWinnerName();

        holder.teamAName.setText(teamA);
        holder.teamBName.setText(teamB);
        holder.dateTime.setText(String.format(Locale.getDefault(), "%s | %s", match.getDate(), match.getTime()));

        holder.teamATrophy.setVisibility(teamA.equals(winnerName) ? View.VISIBLE : View.GONE);
        holder.teamBTrophy.setVisibility(teamB.equals(winnerName) ? View.VISIBLE : View.GONE);

        // --- EXPANSION LOGIC ---
        final boolean isExpanded = position == expandedPosition;
        holder.matchDetailsLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // --- EXPANDED DETAILS BINDING ---
        if (isExpanded) {
            holder.expandedMatchDateTime.setText(String.format(Locale.getDefault(), "%s, %s", match.getDate(), match.getTime()));
            holder.expandedTeamsText.setText(String.format(Locale.getDefault(), "%s vs %s", teamA, teamB));

            holder.expandedWinnerText.setText(String.format("Winner: %s", winnerName));

            // Using match.getFinalScore()
            holder.expandedFinalScore.setText(String.format("Final Score: %s", match.getFinalScore()));

            // Placeholder/Dummy data for timeouts
            holder.expandedTimeoutsText.setText("Timeouts: Team A → 2 used | Team B → 1 used");
        }

        // --- CLICK LISTENERS ---

        // 1. Header Click Listener (Toggles Expansion)
        holder.matchHeaderLayout.setOnClickListener(v -> {
            int previousExpanded = expandedPosition;
            if (isExpanded) {
                expandedPosition = -1; // Collapse current item
            } else {
                expandedPosition = holder.getAdapterPosition(); // Expand this item
            }

            // Notify adapter to redraw relevant items for smooth expansion/collapse
            if (previousExpanded != -1) notifyItemChanged(previousExpanded);
            notifyItemChanged(holder.getAdapterPosition());
        });

        // 2. Delete Button (In Header)
        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.accept(holder.getAdapterPosition());
            }
        });

        // 3. Export Button (Inside the expanded layout)
        holder.exportButton.setOnClickListener(v -> {
            if (onExportClickListener != null) {
                // Passes the Match1 object
                onExportClickListener.accept(match);
            }
        });

        // 4. More Options Button (In Header) - Also toggles expansion
        holder.moreOptionsButton.setOnClickListener(v -> {
            holder.matchHeaderLayout.performClick();
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    // ViewHolder class is generic and doesn't need the Match1 type
    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        // ... (all view declarations remain the same) ...

        // Header Views
        final View matchHeaderLayout;
        final TextView teamAName;
        final TextView teamBName;
        final TextView dateTime;
        final ImageView teamATrophy;
        final ImageView teamBTrophy;
        final ImageView deleteButton;
        final ImageView moreOptionsButton;

        // Expanded Details Views
        final LinearLayout matchDetailsLayout;
        final TextView expandedMatchDateTime;
        final TextView expandedTeamsText;
        final TextView expandedWinnerText;
        final TextView expandedFinalScore;
        final TextView expandedTimeoutsText;
        final Button exportButton;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            // Header
            matchHeaderLayout = itemView.findViewById(R.id.match_header_layout);
            teamAName = itemView.findViewById(R.id.team_a_name);
            teamBName = itemView.findViewById(R.id.team_b_name);
            dateTime = itemView.findViewById(R.id.match_date_time);
            teamATrophy = itemView.findViewById(R.id.team_a_trophy);
            teamBTrophy = itemView.findViewById(R.id.team_b_trophy);
            deleteButton = itemView.findViewById(R.id.delete_match_button);
            moreOptionsButton = itemView.findViewById(R.id.more_options_button);

            // Details (Expanded) - Must match the item_history_match.xml IDs
            matchDetailsLayout = itemView.findViewById(R.id.match_details_layout);
            expandedMatchDateTime = itemView.findViewById(R.id.expanded_match_date_time);
            expandedTeamsText = itemView.findViewById(R.id.expanded_teams_text);
            expandedWinnerText = itemView.findViewById(R.id.expanded_winner_text);
            expandedFinalScore = itemView.findViewById(R.id.expanded_final_score_text);
            expandedTimeoutsText = itemView.findViewById(R.id.expanded_timeouts_text);
            exportButton = itemView.findViewById(R.id.export_button);
        }
    }
}