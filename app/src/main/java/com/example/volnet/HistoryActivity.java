package com.example.volnet;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volnet.adapter.MatchHistoryAdapter;
import com.example.volnet.app.model.Match1; // <-- Using YOUR confirmed class name: Match1
import com.example.volnet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private MatchHistoryAdapter adapter;
    // DECLARATIONS: Use Match1
    private List<Match1> matchList;
    private List<Match1> originalMatchList;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Find Views
        ImageView backButton = findViewById(R.id.back_arrow);
        ImageView sortButton = findViewById(R.id.sort_button);
        historyRecyclerView = findViewById(R.id.history_recycler_view);
        searchBox = findViewById(R.id.search_box);

        // Dummy Data: Initialize both lists
        originalMatchList = createDummyMatchList();
        matchList = new ArrayList<>(originalMatchList);

        // RecyclerView Setup: Use the 4-parameter constructor (Delete, Export handlers)
        // NOTE: You must update MatchHistoryAdapter.java to accept List<Match1> and Consumer<Match1>
        adapter = new MatchHistoryAdapter(
                this,
                matchList,
                this::showDeleteConfirmation, // Handler for Delete
                this::performExportAction    // Handler for Export
        );
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(adapter);

        // Attach Listeners
        backButton.setOnClickListener(v -> finish());
        sortButton.setOnClickListener(this::showSortMenu);

        // --- OnTouchListener (unchanged) ---
        searchBox.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                // Check for tap on the RIGHT (Date Picker) icon
                if (searchBox.getCompoundDrawables()[2] != null) {
                    if (event.getRawX() >= (searchBox.getRight() - searchBox.getCompoundDrawables()[2].getBounds().width() - searchBox.getTotalPaddingEnd()))
                    {
                        showDatePicker();
                        v.performClick(); // FIX 1: Add this call
                        return true;
                    }
                }

                // Check for tap on the LEFT (Enable Typing) icon
                if (searchBox.getCompoundDrawables()[0] != null) {
                    if (event.getRawX() <= (searchBox.getCompoundDrawables()[0].getBounds().width() + searchBox.getTotalPaddingStart()))
                    {
                        searchBox.requestFocus();
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
                        v.performClick(); // FIX 2: Add this call
                        return true;
                    }
                }
            }
            // Allow default behavior for typing in the middle of the EditText
            return false;
        });

        setupSearchListener();
    }

    // METHOD: Returns List<Match1>
    private List<Match1> createDummyMatchList() {
        List<Match1> matches = new ArrayList<>();
        // NOTE: The Match1 constructor must be able to accept these parameters
        matches.add(new Match1("Team A", "Team B", "Oct 01, 2025", "7:30 PM", "Team A", "25-20, 22-25, 15-12"));
        matches.add(new Match1("Oman", "Arabia", "Sep 28, 2025", "11:00 AM", "Arabia", "20-25, 25-18, 10-15"));
        matches.add(new Match1("Russia", "New Zealand", "Sep 25, 2025", "6:00 PM", "Russia", "25-23, 25-19, 25-20"));
        matches.add(new Match1("Africa", "Oman", "Sep 20, 2025", "12:00 PM", "Oman", "25-15, 25-10"));
        return matches;
    }

    // ------------------------------------
    // SEARCH & DATE PICKER IMPLEMENTATION
    // ------------------------------------

    private void setupSearchListener() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String query) {
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

        if (lowerCaseQuery.isEmpty()) {
            matchList.clear();
            matchList.addAll(originalMatchList);
        } else {
            // FILTERING: Use Match1
            List<Match1> filteredList = new ArrayList<>();
            for (Match1 match : originalMatchList) {
                if (match.getDate().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        match.getTime().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        match.getTeamAName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        match.getTeamBName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery))
                {
                    filteredList.add(match);
                }
            }
            matchList.clear();
            matchList.addAll(filteredList);
        }
        adapter.notifyDataSetChanged();
    }

    // Date Picker Dialog (unchanged)
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat matchDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    String formattedDate = matchDateFormat.format(selectedDate.getTime());
                    searchBox.setText(formattedDate);
                    searchBox.setSelection(formattedDate.length());
                    Toast.makeText(this, "Searching for matches on: " + formattedDate, Toast.LENGTH_SHORT).show();
                },
                year, month, day);
        datePickerDialog.show();
    }

    // ------------------------------------
    // SORT IMPLEMENTATION
    // ------------------------------------

    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu_sort_options, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            performSort((String) item.getTitle());
            return true;
        });

        popup.show();
    }

    private void performSort(String sortType) {
        // COMPARATOR: Use Match1
        Comparator<Match1> comparator;

        switch (sortType.trim()) {
            case "A-Z":
                comparator = (m1, m2) -> m1.getTeamAName().compareToIgnoreCase(m2.getTeamAName());
                break;
            case "Z-A":
                comparator = (m1, m2) -> m2.getTeamAName().compareToIgnoreCase(m1.getTeamAName());
                break;
            case "Date Ascending":
                comparator = new DateComparator(true);
                break;
            case "Date Descending":
                comparator = new DateComparator(false);
                break;
            default:
                Toast.makeText(this, "Unknown sort option: " + sortType, Toast.LENGTH_SHORT).show();
                return;
        }

        Collections.sort(matchList, comparator);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Sorting applied: " + sortType, Toast.LENGTH_SHORT).show();
    }

    // Date Comparator for sorting - Use Match1
    private static class DateComparator implements Comparator<Match1> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.US);
        private final boolean ascending;

        public DateComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Match1 m1, Match1 m2) {
            try {
                Date date1 = dateFormat.parse(m1.getDate() + " " + m1.getTime());
                Date date2 = dateFormat.parse(m2.getDate() + " " + m2.getTime());
                return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    // ------------------------------------
    // EXPORT & DELETE IMPLEMENTATION
    // ------------------------------------

    // Export Action - Use Match1
    public void performExportAction(Match1 match) {
        Toast.makeText(this,
                "Exporting match details for " + match.getTeamAName() + " vs " + match.getTeamBName(),
                Toast.LENGTH_LONG).show();
        // TODO: Implement file generation (PDF/CSV) and sharing logic here.
    }

    // Delete Confirmation - Use Match1 in the logic
    private void showDeleteConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // FIX: Corrected the layout name spelling
        View customLayout = getLayoutInflater().inflate(R.layout.dailog_custom_delete_confirmation, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        Button deleteButton = customLayout.findViewById(R.id.dialog_delete_button);
        Button cancelButton = customLayout.findViewById(R.id.dialog_cancel_button);
        ImageView closeButton = customLayout.findViewById(R.id.dialog_close_button);

        deleteButton.setOnClickListener(v -> {
            Match1 matchToDelete = matchList.get(position);

            matchList.remove(position);
            originalMatchList.remove(matchToDelete);

            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Match Deleted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}