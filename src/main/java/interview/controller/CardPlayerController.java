package interview.controller;

import interview.repository.CardPlayerRepository;
import interview.repository.CardGameRepository;
import interview.model.Card;
import interview.model.CardPlayer;
import interview.model.CardGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/cardPlayer")
public class CardPlayerController {

    @Autowired
    private CardPlayerRepository cardPlayerRepository;

    @Autowired
    private CardGameRepository cardGameRepository;

    @GetMapping("{cardPlayerId}")
    public ResponseEntity<String> getCardPlayer(@PathVariable String cardPlayerId){
        CardPlayer cardPlayer = cardPlayerRepository.findById(cardPlayerId);
        if (cardPlayer != null) {
            return new ResponseEntity<>(cardPlayer.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("cardPlayer not found.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("{gameId}/{cardPlayerName}")
    public ResponseEntity<String> createCardPlayer(@PathVariable String gameId, @PathVariable String cardPlayerName) {
        CardPlayer cardPlayer = new CardPlayer(cardPlayerName , gameId);
        cardPlayerRepository.save(cardPlayer);
        CardGame cardGame = cardGameRepository.findById(gameId);
        cardGame.addCardPlayer(cardPlayer);
        return new ResponseEntity<>(String.format("Created %s", cardPlayer), HttpStatus.OK);
    }

    @DeleteMapping("{gameId}/{cardPlayerId}")
    public ResponseEntity<String> removeCardPlayer(@PathVariable String gameId,@PathVariable String cardPlayerId) {
        CardPlayer cardPlayer = cardPlayerRepository.removeCardPlayer(cardPlayerId);
        CardGame cardGame = cardGameRepository.findById(gameId);
        cardGame.removeCardPlayer(cardPlayer);
        return new ResponseEntity<>(String.format("Removed Card Player: \n  %s  ", cardPlayer), HttpStatus.OK);
    }

    @PostMapping("{cardPlayerId}/pickUpCard")
    public ResponseEntity<String> pickUpCard(@PathVariable String cardPlayerId){
        CardPlayer cardPlayer = cardPlayerRepository.findById(cardPlayerId);

        if (cardPlayer == null) {
            return new ResponseEntity<>("cardPlayer not found.", HttpStatus.NOT_FOUND);
        }
        CardGame cardGame = cardGameRepository.findById(cardPlayer.getGameId());
        if (cardGame == null) {
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }

        Card card = cardGame.getDeck().takeCard();
        cardPlayer.appendToHand(card);

        return  new ResponseEntity<>(String.format("You picked up a %s of %s",card.getRank(),card.getSuit()), HttpStatus.OK);
    }

    @GetMapping("{cardPlayerId}/currentHand")
    public ResponseEntity<String> getCardPlayerHand(@PathVariable String cardPlayerId){
        CardPlayer cardPlayer = cardPlayerRepository.findById(cardPlayerId);
        if (cardPlayer == null) {
            return new ResponseEntity<>("cardPlayer not found.", HttpStatus.NOT_FOUND);
        }
        if(cardPlayer.getHand().isEmpty()){
            return new ResponseEntity<>("cardPlayer has no cards", HttpStatus.BAD_REQUEST);
        }
        String cards = cardPlayer.getHand().stream()
                .map(Card::toString) // Convert each Card to a String using its toString method
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>("cardPlayer current hand: "+ cards, HttpStatus.OK);
    }

    @GetMapping("{cardPlayerId}/score")
    public ResponseEntity<String> getCardPlayerScore(@PathVariable String cardPlayerId){
        CardPlayer cardPlayer = cardPlayerRepository.findById(cardPlayerId);
        if (cardPlayer == null) {
            return new ResponseEntity<>("cardPlayer not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(String.format("Card Player %s score: %s",cardPlayer.getName(), cardPlayer.getScore()), HttpStatus.OK);
    }
}
