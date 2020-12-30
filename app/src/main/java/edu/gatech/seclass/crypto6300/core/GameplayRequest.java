package edu.gatech.seclass.crypto6300.core;

public class GameplayRequest {

    private String playerName;
    private Cryptogram cryptogram;
    private int bet;
    private int pendingAttempts;
    private GameStatus gameStatus;

    public GameplayRequest(String playerName, Cryptogram cryptogram, int bet) {
        this.playerName = playerName;
        this.cryptogram = cryptogram;
        this.bet = bet;
        this.pendingAttempts = 5;
        this.gameStatus = GameStatus.ACTIVE;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Cryptogram getCryptogram() {
        return cryptogram;
    }

    public void setCryptogram(Cryptogram cryptogram) {
        this.cryptogram = cryptogram;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getPendingAttempts() {
        return pendingAttempts;
    }

    public void setPendingAttempts(int pendingAttempts) {
        this.pendingAttempts = pendingAttempts;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
