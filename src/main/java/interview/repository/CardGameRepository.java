package interview.repository;

import interview.model.CardGame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CardGameRepository {
    private final Map<String, CardGame> games = new HashMap<>();

    public void save(CardGame cardGame) {
        games.put(cardGame.getId(), cardGame);
    }

    public CardGame findById(String id) {
        return games.get(id);
    }
}
