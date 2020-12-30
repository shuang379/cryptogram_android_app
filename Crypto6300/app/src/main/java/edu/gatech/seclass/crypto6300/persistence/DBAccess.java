package edu.gatech.seclass.crypto6300.persistence;

import java.util.List;

import edu.gatech.seclass.crypto6300.core.Cryptogram;
import edu.gatech.seclass.crypto6300.core.GameplayRequest;
import edu.gatech.seclass.crypto6300.core.Player;
import edu.gatech.seclass.crypto6300.core.PlayerScore;
import edu.gatech.seclass.crypto6300.core.Statistics;
import edu.gatech.seclass.crypto6300.core.User;

public interface DBAccess {

    void saveCryptogram(Cryptogram cryptogram);

    void saveUser(User user);

    void saveScore(PlayerScore score);

    void saveStatistics(Statistics statistics);

    void updateUser(User user);

    void updateCryptogram(Cryptogram cryptogram);

    Cryptogram getCryptogramByTitle(String title);

    User getUserByUsername(String username);

    Player getPlayerByUsername(String username);

    List<Cryptogram> getCryptograms();

    List<Player> getPlayers();

    void saveCurrentUser(String username);

    String getCurrentUser();

    void saveCurrentGameplay(GameplayRequest gameplayRequest);

    GameplayRequest getCurrentGameplayRequest();

    void clear();

}
