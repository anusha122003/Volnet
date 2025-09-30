package com.example.volnet;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

    private List<Team> teamList;
    private OnTeamClickListener listener;

    public TeamAdapter(List<Team> teamList, OnTeamClickListener listener) {
        this.teamList = teamList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.bind(team, listener);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        ImageView teamLogo;

        TeamViewHolder(View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.textTeamName);
            teamLogo = itemView.findViewById(R.id.imageTeamLogo);

        }

        void bind(Team team, OnTeamClickListener listener) {
            teamName.setText(team.getName());

            // If logo is stored as Uri string in DB
            if (team.getLogo() != null && !team.getLogo().isEmpty()) {
//                teamLogo.setImageURI(Uri.parse(team.getLogo()));
                Glide.with(teamLogo.getContext())
                        .load(Uri.parse(team.getLogo()))
                        .placeholder(R.drawable.logo)  // fallback image while loading
                        .error(R.drawable.logo)        // fallback image on error
                        .into(teamLogo);
            } else {
                teamLogo.setImageResource(R.drawable.logo); // fallback
            }

            itemView.setOnClickListener(v -> listener.onTeamClick(team));
        }
    }
}