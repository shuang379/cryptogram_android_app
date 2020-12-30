package edu.gatech.seclass.crypto6300.core;

import java.util.Date;

public class Statistics implements Comparable<Statistics>{

    private String title;
    private String createdBy;
    private int numGames;
    private String percentWin;
    private Date createdDate;

    public Statistics(String cryptogram, String player, int i, String i1, Date date) {
        this.title = cryptogram;
        this.createdBy = player;
        this.numGames = i;
        this.percentWin = i1;
        this.createdDate = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getNumGames() {
        return numGames;
    }

    public void setNumGames(int numGames) {
        this.numGames = numGames;
    }

    public String getPercentWin() {
        return percentWin;
    }

    public void setPercentWin(String percentWin) {
        this.percentWin = percentWin;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int compareTo(Statistics statistics) {
        if(this.getCreatedDate().after(statistics.getCreatedDate())){
            return -1;
        }else if(this.getCreatedDate().before(statistics.getCreatedDate())){
            return 1;
        }
        return 0;
    }
}
