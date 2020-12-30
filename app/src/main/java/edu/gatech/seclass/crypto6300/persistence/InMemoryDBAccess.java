package edu.gatech.seclass.crypto6300.persistence;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.core.PlayerScore;
import edu.gatech.seclass.crypto6300.core.Statistics;
import edu.gatech.seclass.crypto6300.core.User;
import edu.gatech.seclass.crypto6300.exception.EntityAlreadyExistsException;
import edu.gatech.seclass.crypto6300.exception.EntityNotFoundException;

public class InMemoryDBAccess implements DBAccess {

    private List<User> users;
    private List<Cryptogram> cryptograms;
    private List<PlayerScore> scores;
    private List<Statistics> allStatistics;
    private String currentUser;
    private GameplayRequest currentGameplayRequest;

    public InMemoryDBAccess() {
        users = new ArrayList<>();
        cryptograms = new ArrayList<>();
        scores = new ArrayList<>();
        allStatistics = new ArrayList<>();
    }

    @Override
    public void saveCryptogram(Cryptogram cryptogram) {
        if (cryptograms.contains(cryptogram)) {
            throw new EntityAlreadyExistsException("Cryptogram already exists");
        }
        cryptograms.add(cryptogram);
    }

    @Override
    public void saveUser(User user) {
        if (users.contains(user)) {
            throw new EntityAlreadyExistsException("User already exists");
        }
        users.add(user);
    }

    @Override
    public void saveScore(PlayerScore score) {
        if (scores.contains(score)) {
            throw new EntityAlreadyExistsException("User already exists");
        }
        scores.add(score);
    }

    @Override
    public void saveStatistics(Statistics statistics){
        if(allStatistics.contains(statistics)){
            throw new EntityAlreadyExistsException("Record already exists");
        }
        allStatistics.add(statistics);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = null;
        for (User u : users) {
            if (u.equals(user)) {
                existingUser = u;
                break;
            }
        }

        if (existingUser == null) {
            throw new EntityNotFoundException("User does not exist");
        }

        Player existingPlayer = (Player) existingUser;
        Player updatedPlayer = (Player) user;
        existingPlayer.setScore(updatedPlayer.getScore());
        existingPlayer.setAttemptedCryptograms(updatedPlayer.getAttemptedCryptograms());
        existingPlayer.setCurrentGameplay(updatedPlayer.getCurrentGameplay());
        existingPlayer.setNumAttemptedCryptograms(updatedPlayer.getNumAttemptedCryptograms());
    }

    public void updateCryptogram(Cryptogram cryptogram) {
        Cryptogram existingCrypto = null;
        for (Cryptogram c : cryptograms) {
            if (c.equals(cryptogram)) {
                existingCrypto = c;
                break;
            }
        }

        if (existingCrypto == null) {
            throw new EntityNotFoundException("Cryptogram does not exist");
        }

        existingCrypto.setNumOfGames(cryptogram.getNumOfGames());
        existingCrypto.setNumOfWins(cryptogram.getNumOfWins());
        existingCrypto.setDisabled(cryptogram.isDisabled());
    }

    @Override
    public Cryptogram getCryptogramByTitle(final String title) {
        for (Cryptogram cryptogram : cryptograms) {
            if (cryptogram.getTitle().equals(title)) {
                return cryptogram;
            }
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public Player getPlayerByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return (Player) user;
            }
        }

        return null;
    }

    @Override
    public List<Cryptogram> getCryptograms() {

        return cryptograms;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Player) {
                Player player = (Player) u;
                players.add(player);
            }
        }

        return players;
    }

    @Override
    public void saveCurrentUser(String username) {
        currentUser = username;
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void saveCurrentGameplay(GameplayRequest request) {
        currentGameplayRequest = request;
    }

    @Override
    public GameplayRequest getCurrentGameplayRequest() {
        return currentGameplayRequest;
    }

    @Override
    public void clear() {
        users = new ArrayList<>();
        cryptograms = new ArrayList<>();
        scores = new ArrayList<>();
        allStatistics = new ArrayList<>();
    }
}
