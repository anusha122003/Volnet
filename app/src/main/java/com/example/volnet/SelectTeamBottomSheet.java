package com.example.volnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class SelectTeamBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private TeamAdapter adapter;
    private List<Team> teamList;

    public interface TeamSelectionListener {
        void onTeamSelected(Team team);
    }

    private TeamSelectionListener selectionListener;

    public SelectTeamBottomSheet(List<Team> teams, TeamSelectionListener listener) {
        this.teamList = teams;
        this.selectionListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_select_team, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTeams);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TeamAdapter(teamList, team -> {
            selectionListener.onTeamSelected(team);
            dismiss();
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
