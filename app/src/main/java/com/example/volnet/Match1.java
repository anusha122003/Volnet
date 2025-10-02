package com.example.volnet.app.model;

/**
 * Model class representing a single completed Volleyball Match record.
 * This class encapsulates all necessary data for history display.
 */
public class Match1 {

    private String teamAName;
    private String teamBName;
    private String date;
    private String time;
    private String winnerName;
    private String finalScore;

    // Constructor
    public Match1(String teamAName, String teamBName, String date, String time, String winnerName, String finalScore) {
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.date = date;
        this.time = time;
        this.winnerName = winnerName;
        this.finalScore = finalScore;
    }

    // Getter Methods
    public String getTeamAName() {
        return teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getFinalScore() {
        return finalScore;
    }
}

