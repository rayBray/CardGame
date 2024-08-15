package interview.repository;

import interview.model.CardPlayer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CardPlayerRepository {
    private final Map<String, CardPlayer> cardPlayer = new HashMap<>();

    public void save(CardPlayer newCardPlayer) {
        cardPlayer.put(CardPlayer.getId(), newCardPlayer);
    }

    public CardPlayer findById(String id) {
        return cardPlayer.get(id);
    }

    public CardPlayer removeCardPlayer(String id){
        return cardPlayer.remove(id);
    }

}
