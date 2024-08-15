package interview.controller;

import interview.model.CardPlayer;
import interview.repository.CardGameRepository;
import interview.service.GameService;

import interview.model.CardGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/card_game")
public class CardGameController {

    @Autowired
    private CardGameRepository cardGameRepository;

    @PostMapping("/{gameId}/add_deck")
    public ResponseEntity<String> addDeck(@PathVariable String gameId) {
        CardGame cardGame = cardGameRepository.findById(gameId);
        if(gameId == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        cardGame.getDeck().addCards();
        return new ResponseEntity<>("Deck added, current number of cards in deck: "+ cardGame.getDeck().getCards().size(), HttpStatus.OK);
    }

    @PutMapping("/{gameId}/shuffle")
    public ResponseEntity<String> shuffleDeck(@PathVariable String gameId) {
        CardGame cardGame = cardGameRepository.findById(gameId);
        if(gameId == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        cardGame.getDeck().shuffle();
        return new ResponseEntity<>("Deck shuffled", HttpStatus.OK);
    }

    @GetMapping("/{gameId}/remaining_cards")
    public ResponseEntity<String> remainingCards(@PathVariable String gameId) {
        CardGame cardGame = cardGameRepository.findById(gameId);
        if(cardGame == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        GameService gameService = new GameService();
        String undealtCardCounts = gameService.getCardCounts(cardGame.getDeck());
        return new ResponseEntity<>("Remaining cards:\n"+undealtCardCounts, HttpStatus.OK);

    }

    @GetMapping("/{gameId}/suit_count")
    public ResponseEntity<String> getSuitCount(@PathVariable String gameId) {
        CardGame cardGame = cardGameRepository.findById(gameId);
        if(gameId == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        GameService gameService = new GameService();
        return new ResponseEntity<>(gameService.getUndealtCardCounts(cardGame.getDeck()), HttpStatus.OK);
    }

    @GetMapping("/{gameId}/player_rank")
    public ResponseEntity<String> getPlayerRanks(@PathVariable String gameId) {
        CardGame cardGame = cardGameRepository.findById(gameId);
        if(cardGame == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        String ListPlayers = cardGame.getCardPlayers().stream()
                .sorted(Comparator.comparingInt(CardPlayer::getScore).reversed()) // Correct usage of Comparator
                .map(player -> player.getName() + ": " + player.getScore()) // Format player and score
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(ListPlayers, HttpStatus.OK);
    }
}