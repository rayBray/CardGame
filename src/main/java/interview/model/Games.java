package interview.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Games {
    private final String id;
    private List<CardGame> cardGame = new ArrayList<>();


    public Games(String player, String gameTitle) {
        this.id = UUID.randomUUID().toString();
        cardGame.add(new CardGame(gameTitle, player));

    }

    public String getId() {
        return id;
    }

    public List<CardGame> getCardGame() {
        return cardGame;
    }

    public void addCardGame(CardGame cardGame) {
        this.cardGame.add(cardGame);
    }
}
