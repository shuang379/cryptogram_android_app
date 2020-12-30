package edu.gatech.seclass.crypto6300.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import edu.gatech.seclass.crypto6300.persistence.DBAccess;
import edu.gatech.seclass.crypto6300.persistence.DBAccessProvider;

public class Game {

    private static Game game;

    private Game(Context context) {
        this.dbAccess = DBAccessProvider.getDBAccess();
    }

    public static Game getGame(Context context) {
        if (game == null) {
            game = new Game(context);
        }

        return game;
    }

    private final DBAccess dbAccess;

    public GameplayRequest startCryptogramAttempt(Player player, int bet) {

        Cryptogram cryptogram = getRandomCryptogram(player);

        if (cryptogram == null) {
            return null;
        }

        GameplayRequest gameplayRequest = new GameplayRequest(player.getUsername(), cryptogram, bet);

        player.setCurrentGameplay(gameplayRequest);
        player.setNumAttemptedCryptograms(player.getNumAttemptedCryptograms()+1);
        player.getAttemptedCryptograms().add(cryptogram.getTitle());

        dbAccess.saveCurrentGameplay(gameplayRequest);
        dbAccess.updateUser(player);
        //dbAccess.updateCryptogram(cryptogram);

        return gameplayRequest;
    }

    public void finishGameplay(GameplayRequest gameplayRequest) {

        if (gameplayRequest.getGameStatus() == GameStatus.WON) {
            Player player = dbAccess.getPlayerByUsername(gameplayRequest.getPlayerName());

            if (gameplayRequest.getPendingAttempts() > 2) {
                player.addToScore(gameplayRequest.getBet());
            }

            player.setCurrentGameplay(null);
            Cryptogram crypto = gameplayRequest.getCryptogram();
            crypto.setNumOfGames(crypto.getNumOfGames() + 1);
            crypto.setNumOfWins(crypto.getNumOfWins()+1);

            dbAccess.updateUser(player);
            dbAccess.saveCurrentGameplay(gameplayRequest);
            dbAccess.updateCryptogram(crypto);

        } else {
            Player player = dbAccess.getPlayerByUsername(gameplayRequest.getPlayerName());
            player.addToScore(gameplayRequest.getBet() * (-1));
            player.setCurrentGameplay(null);
            Cryptogram crypto = gameplayRequest.getCryptogram();
            crypto.setNumOfGames(crypto.getNumOfGames() + 1);
            dbAccess.updateUser(player);
            dbAccess.saveCurrentGameplay(gameplayRequest);
            dbAccess.updateCryptogram(crypto);
        }
    }

    private Cryptogram getRandomCryptogram(Player player) {
        List<Cryptogram> allCryptograms = dbAccess.getCryptograms();

        List<Cryptogram> eligibleCryptograms = new ArrayList<>();
        for (Cryptogram cryptogram : allCryptograms) {
            if (cryptogram.isDisabled()
                    || cryptogram.getCreatedBy().equals(player.getUsername())
                    || player.getAttemptedCryptograms().contains(cryptogram.getTitle())) {
                continue;
            } else {
                eligibleCryptograms.add(cryptogram);
            }
        }

        if (eligibleCryptograms.size() == 0) {
            return null;
        }

        Random rand = new Random();
        return eligibleCryptograms.get(rand.nextInt(eligibleCryptograms.size()));
    }

    public GameplayRequest checkSolution(GameplayRequest gameplayRequest, String potentialSolution) {
        if (gameplayRequest.getCryptogram().getSolution().equals(potentialSolution)) {
            gameplayRequest.setGameStatus(GameStatus.WON);
        } else {
            gameplayRequest.setPendingAttempts(gameplayRequest.getPendingAttempts() - 1);
            if (gameplayRequest.getPendingAttempts() == 0) {
                gameplayRequest.setGameStatus(GameStatus.LOST);
            }
        }

        dbAccess.saveCurrentGameplay(gameplayRequest);
        Player player = dbAccess.getPlayerByUsername(gameplayRequest.getPlayerName());
        player.setCurrentGameplay(gameplayRequest);
        dbAccess.updateUser(player);

        return gameplayRequest;
    }

    public List<PlayerScore> viewPlayerScores(){
        List<PlayerScore> psList= new ArrayList<>();
        for (Player player : dbAccess.getPlayers()) {
            PlayerScore ps = new PlayerScore(player.getUsername(), player.getScore(), player.getNumAttemptedCryptograms());
            psList.add(ps);
        }

        Collections.sort(psList);

        return psList;
    }

        public List<Statistics> viewCryptogramStatistics() {
            List<Statistics> staList= new ArrayList<>();
            for (Cryptogram c : dbAccess.getCryptograms()) {
                String percentWin;
                if (c.getNumOfGames()==0){
                    percentWin = "N/A";
                }
                else {
                    percentWin = String.valueOf(c.getNumOfWins()*100/c.getNumOfGames())+"%";
                }
                Statistics s = new Statistics(c.getTitle(),c.getCreatedBy(), c.getNumOfGames(),percentWin, c.getCreatedDate());
                staList.add(s);
            }
            Collections.sort(staList);
            return staList;
    }

}
