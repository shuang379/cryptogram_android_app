package edu.gatech.seclass.crypto6300.core;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Cryptogram{

    private String title;
    private String solution;
    private String encodedPhrase;
    private String hint;
    private Date createdDate;
    private String createdBy;

    private boolean isDisabled;
    private int numOfGames;
    private int numOfWins;


    public Cryptogram(String title, String solution, String encodedPhrase, String hint, String createdBy) {
        this.title = title;
        this.solution = solution;
        this.encodedPhrase = encodedPhrase;
        this.hint = hint;
        this.createdDate = Calendar.getInstance().getTime();
        this.createdBy = createdBy;
        this.isDisabled = false;
        this.numOfGames = 0;
        this.numOfWins = 0;
    }

    public String getTitle() {
        return title;
    }

    public String getSolution() {
        return solution;
    }

    public String getEncodedPhrase() {
        return encodedPhrase;
    }

    public String getHint() {
        return hint;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cryptogram that = (Cryptogram) o;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setEncodedPhrase(String encodedPhrase) {
        this.encodedPhrase = encodedPhrase;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getNumOfGames() {
        return numOfGames;
    }

    public void setNumOfGames(int numOfGames) {
        this.numOfGames = numOfGames;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public void setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
    }

}
