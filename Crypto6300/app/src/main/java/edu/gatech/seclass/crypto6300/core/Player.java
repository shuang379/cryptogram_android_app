package edu.gatech.seclass.crypto6300.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.MainActivity;

public class Player extends User {

    private int score;
    private int numAttemptedCryptograms;
    private List<String> attemptedCryptograms;
    private GameplayRequest currentGameplay;

    public Player() {
    }

    public Player(String username, String emailAddress) {
        super(username, emailAddress);
        attemptedCryptograms = new ArrayList<>();
        score = 20;
    }

    public int addToScore(int delta) {
        score+= delta;
        return score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumAttemptedCryptograms() {
        return numAttemptedCryptograms;
    }

    public void setNumAttemptedCryptograms(int numAttemptedCryptograms) {
        this.numAttemptedCryptograms = numAttemptedCryptograms;
    }

    public List<String> getAttemptedCryptograms() {
        return attemptedCryptograms;
    }

    public void setAttemptedCryptograms(List<String> attemptedCryptograms) {
        this.attemptedCryptograms = attemptedCryptograms;
    }

    public GameplayRequest getCurrentGameplay() {
        return currentGameplay;
    }

    public void setCurrentGameplay(GameplayRequest currentGameplay) {
        this.currentGameplay = currentGameplay;
    }

    public GameplayRequest startCryptogramAttempt(int bet) {

        return Game.getGame(MainActivity.getContextOfApplication()).startCryptogramAttempt(this, bet);
    }
}
