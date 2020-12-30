package edu.gatech.seclass.crypto6300.persistence;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.MainActivity;
import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.core.PlayerScore;
import edu.gatech.seclass.crypto6300.core.Statistics;
import edu.gatech.seclass.crypto6300.core.User;
import edu.gatech.seclass.crypto6300.exception.EntityAlreadyExistsException;
import edu.gatech.seclass.crypto6300.exception.EntityNotFoundException;
import edu.gatech.seclass.crypto6300.util.JsonDeserializerWithInheritance;

import static android.content.Context.MODE_PRIVATE;

public class PersistentDBAccess implements DBAccess {

    private static final String KEY_CRYPTOGRAMS = "cryptograms";
    private static final String KEY_USERS = "users";
    private static final String KEY_CURRENT_USER = "currentUser";
    private static final String KEY_CURRENT_GAMEPLAY = "currentGameplay";

    private final SharedPreferences sp;

    private Gson gson;

    public PersistentDBAccess() {
        this.sp = MainActivity.getContextOfApplication().getSharedPreferences("crypto6300.database", MODE_PRIVATE);

        this.gson = initGson();

        if (sp.getString(KEY_CRYPTOGRAMS, "").equals("")) {
            sp.edit().putString(KEY_CRYPTOGRAMS, gson.toJson(new ArrayList<Cryptogram>())).apply();
        }

        if (sp.getString(KEY_USERS, "").equals("")) {
            sp.edit().putString(KEY_USERS, gson.toJson(new ArrayList<User>())).apply();
        }
    }

    private Gson initGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(User.class, new JsonDeserializerWithInheritance<User>());
        return builder.create();
    }

    @Override
    public void saveCryptogram(Cryptogram cryptogram) {
        String serialisedCryptogrms = sp.getString(KEY_CRYPTOGRAMS, "");
        List<Cryptogram> cryptograms;
        if (serialisedCryptogrms == null || serialisedCryptogrms.isEmpty()) {
            cryptograms = new ArrayList<>();
        } else {
            Type cryptogramListType = new TypeToken<ArrayList<Cryptogram>>(){}.getType();
            cryptograms = gson.fromJson(serialisedCryptogrms, cryptogramListType);
        }

        if (cryptograms.contains(cryptogram)) {
            throw new EntityAlreadyExistsException("Cryptogram already exists");
        }
        cryptograms.add(cryptogram);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_CRYPTOGRAMS, gson.toJson(cryptograms));
        edit.apply();
//        sp.edit().putString(KEY_CRYPTOGRAMS, gson.toJson(cryptograms)).apply();
    }

    @Override
    public void saveUser(User user) {
        String serialisedUsers = sp.getString(KEY_USERS, "");
        List<User> users;
        if (serialisedUsers == null || serialisedUsers.isEmpty()) {
            users = new ArrayList<>();
        } else {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(serialisedUsers, userListType);
        }

        if (users.contains(user)) {
            throw new EntityAlreadyExistsException("User already exists");
        }
        users.add(user);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_USERS, gson.toJson(users));
        edit.apply();

//        sp.edit().putString(KEY_USERS, gson.toJson(users)).apply();
    }

    @Override
    public void saveScore(PlayerScore score) {

    }

    @Override
    public void saveStatistics(Statistics statistics) {

    }

    @Override
    public void updateUser(User user) {
        String serialisedUsers = sp.getString(KEY_USERS, "");
        List<User> users;
        if (serialisedUsers == null || serialisedUsers.isEmpty()) {
            users = new ArrayList<>();
        } else {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(serialisedUsers, userListType);
        }

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

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_USERS, gson.toJson(users));
        edit.apply();

        // sp.edit().putString(KEY_USERS, gson.toJson(users)).apply();
    }

    @Override
    public void updateCryptogram(Cryptogram cryptogram) {
        String serialisedCryptogrms = sp.getString(KEY_CRYPTOGRAMS, "");
        List<Cryptogram> cryptograms;
        if (serialisedCryptogrms == null || serialisedCryptogrms.isEmpty()) {
            cryptograms = new ArrayList<>();
        } else {
            Type cryptogramListType = new TypeToken<ArrayList<Cryptogram>>(){}.getType();
            cryptograms = gson.fromJson(serialisedCryptogrms, cryptogramListType);
        }

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

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_CRYPTOGRAMS, gson.toJson(cryptograms));
        edit.apply();

        // sp.edit().putString(KEY_CRYPTOGRAMS, gson.toJson(cryptograms)).apply();
    }

    @Override
    public Cryptogram getCryptogramByTitle(String title) {
        String serialisedCryptogrms = sp.getString(KEY_CRYPTOGRAMS, "");
        List<Cryptogram> cryptograms;
        if (serialisedCryptogrms == null || serialisedCryptogrms.isEmpty()) {
            cryptograms = new ArrayList<>();
        } else {
            Type cryptogramListType = new TypeToken<ArrayList<Cryptogram>>(){}.getType();
            cryptograms = gson.fromJson(serialisedCryptogrms,cryptogramListType);
        }

        for (Cryptogram cryptogram : cryptograms) {
            if (cryptogram.getTitle().equals(title)) {
                return cryptogram;
            }
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String serialisedUsers = sp.getString(KEY_USERS, "");
        List<User> users;
        if (serialisedUsers == null || serialisedUsers.isEmpty()) {
            users = new ArrayList<>();
        } else {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(serialisedUsers, userListType);
        }

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public Player getPlayerByUsername(String username) {
        String serialisedUsers = sp.getString(KEY_USERS, "");
        List<User> users;
        if (serialisedUsers == null || serialisedUsers.isEmpty()) {
            users = new ArrayList<>();
        } else {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(serialisedUsers, userListType);
        }

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return (Player) user;
            }
        }

        return null;
    }

    @Override
    public List<Cryptogram> getCryptograms() {
        String serialisedCryptogrms = sp.getString(KEY_CRYPTOGRAMS, "");
        List<Cryptogram> cryptograms;
        if (serialisedCryptogrms == null || serialisedCryptogrms.isEmpty()) {
            cryptograms = new ArrayList<>();
        } else {
            Type cryptogramListType = new TypeToken<ArrayList<Cryptogram>>(){}.getType();
            cryptograms = gson.fromJson(serialisedCryptogrms, cryptogramListType);
        }

        return cryptograms;
    }

    @Override
    public List<Player> getPlayers() {
        String serialisedUsers = sp.getString(KEY_USERS, "");
        List<User> users;
        if (serialisedUsers == null || serialisedUsers.isEmpty()) {
            users = new ArrayList<>();
        } else {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(serialisedUsers, userListType);
        }

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
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_CURRENT_USER, username);
        edit.apply();

//        sp.edit().putString("currentUser", username).apply();
    }

    @Override
    public String getCurrentUser() {
        return sp.getString(KEY_CURRENT_USER, null);
    }

    @Override
    public void saveCurrentGameplay(GameplayRequest gameplayRequest) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_CURRENT_GAMEPLAY, gson.toJson(gameplayRequest));
        edit.apply();
//        sp.edit().putString("currentGameplay", gson.toJson(gameplayRequest)).apply();
    }

    @Override
    public GameplayRequest getCurrentGameplayRequest() {
        String gameplayString = sp.getString(KEY_CURRENT_GAMEPLAY, null);
        return gson.fromJson(gameplayString, GameplayRequest.class);
    }

    @Override
    public void clear() {
        sp.edit().putString(KEY_CRYPTOGRAMS, gson.toJson(new ArrayList<Cryptogram>())).apply();
        sp.edit().putString(KEY_USERS, gson.toJson(new ArrayList<User>())).apply();
        sp.edit().putString(KEY_CURRENT_USER, "").apply();
        sp.edit().putString(KEY_CURRENT_GAMEPLAY, "").apply();
    }
}
