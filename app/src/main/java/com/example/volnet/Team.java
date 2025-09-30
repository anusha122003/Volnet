package com.example.volnet;

public class Team {
    private int id;
    private String name;
    private String logo;


    public Team(int id, String name,String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLogo() { return logo; }
}
