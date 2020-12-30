package edu.gatech.seclass.crypto6300.core;

public class PlayerScore implements Comparable<PlayerScore>{

    private String username;
    private int score;
    private int numAttemptedCryptogram;

    public PlayerScore(String username, int score, int numAttemptedCryptogram) {
        this.username = username;
        this.score = score;
        this.numAttemptedCryptogram = numAttemptedCryptogram;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumAttemptedCryptogram() {
        return numAttemptedCryptogram;
    }

    public void setNumAttemptedCryptogram(int numAttemptedCryptogram) {
        this.numAttemptedCryptogram = numAttemptedCryptogram;
    }

    @Override
    public int compareTo(PlayerScore score) {
        if(this.getScore() < score.getScore()){
            return 1;
        }else if(this.getScore() > score.getScore()){
            return -1;
        }
        return 0;
    }
}
