package interview.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Constructors, getters, and setters
public class CardGame {
    private String title;
    private String id;
    private Deck deck;
    private List<CardPlayer> cardPlayers = new ArrayList<>();


    public CardGame(String id, String title, String player) {
        this.createGame(id, title, player);
    }

    public CardGame(String title, String player) {
        this.createGame(UUID.randomUUID().toString(), title, player);
    }

    private void createGame(String id, String title, String player){
        this.id = id;
        this.title = title;
        deck = new Deck();
        CardPlayer cardPlayer = new CardPlayer(player, id);
        cardPlayers.add(cardPlayer);
    }

    public Deck getDeck() {
        return deck;
    }

    public List<CardPlayer> getCardPlayers() {
        return cardPlayers;
    }

    public void addCardPlayer(CardPlayer cardPlayers) {
        this.cardPlayers.add(cardPlayers);
    }

    public void removeCardPlayer(CardPlayer cardPlayers) {
        this.cardPlayers.remove(cardPlayers);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


}
