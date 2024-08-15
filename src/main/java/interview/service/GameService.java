package interview.service;

import interview.model.Card;
import interview.model.CardPlayer;
import interview.model.Deck;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GameService {
    private final List<String> suitOrder = Arrays.asList("Hearts", "Spades", "Clubs", "Diamonds");
    private final List<String> rankOrder = Arrays.asList("King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2", "Ace");


    public String getCardCounts(Deck deck) {
        Map<String, Map<String, Long>>  cardCounts = countCardsBySuits(deck);
        StringBuilder result = new StringBuilder();

        for (String suit : suitOrder) {
            if (cardCounts.containsKey(suit)) {
                result.append(suit).append(": ");
                Map<String, Long> rankCounts = cardCounts.get(suit);
                for (String rank : rankOrder) {
                    if (rankCounts.containsKey(rank)) {
                        result.append(rankCounts.get(rank)).append(" ").append(rank).append(", ");
                    }
                }
                if (result.length() > 2) {
                    result.setLength(result.length() - 2);
                }
                result.append("\n");
            }
        }

        return result.toString();
    }

    public String getUndealtCardCounts(Deck deck) {
        Map<String, Long> totalCardCounts = deck.getCards().stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Map<String, Long> undealtCardCounts = deck.getCards().stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        // Prepare output sorted by suit
        StringBuilder result = new StringBuilder();
        for (String suit : this.suitOrder) {
            long totalCount = totalCardCounts.getOrDefault(suit, 0L);
            long undealtCount = undealtCardCounts.getOrDefault(suit, 0L);
            result.append(undealtCount).append(" ").append(suit).append("\n");
        }

        return result.toString();
    }

    public String rankingOfPlayers(List<CardPlayer> players){
        return players.stream()
                .sorted(Comparator.comparingInt(CardPlayer::getScore).reversed()) // Correct usage of Comparator
                .map(player -> player.getName() + ": " + player.getScore()) // Format player and score
                .collect(Collectors.joining(", "));

    }


    private static Map<String, Map<String, Long>>  countCardsBySuits(Deck deck){
        return deck.getCards().stream()
                .collect(Collectors.groupingBy(
                        Card::getSuit,
                        Collectors.groupingBy(
                                Card::getRank,
                                Collectors.counting()
                        )
                ));
    }

}



