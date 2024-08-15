package interview.model;

import java.util.UUID;

public class Card {
    private final String id;
    private final int value;
    private final String suit;
    private final String rank;
    public static final String[] SUITS = {"Clubs", "Diamonds", "Hearts", "Spades"};
    public static final String[] RANKS = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public Card( String suit, String rank, int value) {
        this.id = UUID.randomUUID().toString();
        this.suit = suit;
        this.rank = rank;
        this.value = value+1;
    }

    public int getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

}
