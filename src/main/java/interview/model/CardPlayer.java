package interview.model;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardPlayer {

    @NotNull
    private String name;
    private static String id;
    private List<Card> hand = new ArrayList<>();
    private int score = 0;
    private String gameId;


    public CardPlayer() {}

    public CardPlayer(String name, String gameId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getId() {
        return id;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void appendToHand(Card card) {
        score = card.getValue() + score;
        hand.add(card);
    }

    public int getScore(){
        return score;
    }

    @Override
    public String toString() {
        return "CardPlayer{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", gameId=" + gameId +
                '}';
    }
}
