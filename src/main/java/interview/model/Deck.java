package interview.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;

public class Deck {

    private List<Card> Cards;
    Random rand = new Random();


    public Deck(){
        this.addCards();
    }

    public List<Card> getCards() {
        return Cards;
    }

    public Card takeCard(){
        if(this.Cards.isEmpty() ){
            throw new IllegalArgumentException("No more cards in deck!");
        }
        return this.Cards.remove(0);
    }

    public void addCards(){
        if( this.Cards == null){
            this.Cards = new ArrayList<>();
        }
        for( int i=0; i < 52; i++){
            int suitIndex = i / 13;
            int rankIndex = i % 13;
            Card curr = new Card(Card.SUITS[suitIndex], Card.RANKS[rankIndex], i );
            this.Cards.add(curr);
        }

    }

    public void shuffle(){
        int halfLength = (int) (this.Cards.size() / 2);
        for( int i=0; i<halfLength; i++){
            int ranIndex = rand.nextInt(halfLength);
            this.switchCards(ranIndex);
        }
    }

    private void switchCards(int index){
        int swi = index+index-1;
        if(index ==0){
            swi = this.Cards.size()-1;
        }
        Card temp = Cards.get(index);
        Cards.set(index, Cards.get(swi));
        Cards.set(swi, temp);
    }

    @Override
    public String toString() {
        return this.Cards.stream()
                .map(card -> card.getSuit() + " " + card.getRank())
                .collect(Collectors.joining(", "));
    }

}
