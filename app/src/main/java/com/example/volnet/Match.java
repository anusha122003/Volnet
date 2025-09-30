package com.example.volnet;

// Match.java
public class Match {
    private int id;
    private String teamAName, teamALogo;
    private String teamBName, teamBLogo;
    private String date, time;
    private int scoreA, scoreB, setNumber;
    private int timeoutA, timeoutB;
    private String winner;

    public Match(int id, String teamAName, String teamALogo,
                 String teamBName, String teamBLogo,
                 String date, String time,
                 int scoreA, int scoreB, int setNumber,
                 int timeoutA, int timeoutB, String winner) {
        this.id = id;
        this.teamAName = teamAName;
        this.teamALogo = teamALogo;
        this.teamBName = teamBName;
        this.teamBLogo = teamBLogo;
        this.date = date;
        this.time = time;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.setNumber = setNumber;
        this.timeoutA = timeoutA;
        this.timeoutB = timeoutB;
        this.winner = winner;
    }

    public int getId() { return id; }
    public String getTeamAName() { return teamAName; }
    public String getTeamALogo() { return teamALogo; }
    public String getTeamBName() { return teamBName; }
    public String getTeamBLogo() { return teamBLogo; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getScoreA() { return scoreA; }
    public int getScoreB() { return scoreB; }
    public int getSetNumber() { return setNumber; }
    public int getTimeoutA() { return timeoutA; }
    public int getTimeoutB() { return timeoutB; }
    public String getWinner() { return winner; }
}
