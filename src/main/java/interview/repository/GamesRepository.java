package interview.repository;

import interview.model.Games;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GamesRepository {
    private final Map<String, Games> games = new HashMap<>();

    public void save(Games game) {
        this.games.put(game.getId(), game);
    }

    public Games findById(String id) {
        return this.games.get(id);
    }

    public Games removeGame(String id){
        return games.remove(id);
    }

    public Map<String, Games> getGames() {
        return games;
    }
}
