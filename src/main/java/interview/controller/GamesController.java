package interview.controller;

import interview.model.Games;
import interview.repository.CardGameRepository;
import interview.repository.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private GamesRepository gamesRepository;
    @Autowired
    private CardGameRepository cardGameRepository;

    @PostMapping("{gameTitle}/{player}")
    public ResponseEntity<String> createGame(@PathVariable String player, @PathVariable String gameTitle) {
        Games games = new Games(player, gameTitle);
        gamesRepository.save(games);
        cardGameRepository.save(games.getCardGame().get(games.getCardGame().size()-1));
        return new ResponseEntity<>(String.format("Created %s game: %s",gameTitle, games.getCardGame().get(0).getId()), HttpStatus.OK);
    }

    @DeleteMapping("{gameId}")
    public ResponseEntity<String> removeGame(@PathVariable String gameId) {
        Games game = gamesRepository.removeGame(gameId);
        if(game == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(String.format("Removed Game:  %s \n  All players associated with game have been removed  ", gameId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<String> getGame() {
        Map<String, Games>  games = gamesRepository.getGames();
        if(games == null || games.isEmpty()){
            return new ResponseEntity<>("Game not found.", HttpStatus.NOT_FOUND);
        }
        String gameString = games.entrySet().stream()
                .map(entry -> "\n Game ID: " + entry.getValue().getId() + ", Title: " + entry.getValue().getCardGame().get(0).getTitle())
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(String.format("List Games: %s", gameString), HttpStatus.OK);
    }

}