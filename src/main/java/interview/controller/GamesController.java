package interview.controller;

import interview.model.Games;
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
    //get people in game
    // start game
    //end game
    //get rank of people in game
    @Autowired
    private GamesRepository gamesRepository;

    @PostMapping("{game}/{player}")
    public ResponseEntity<String> createGame(@PathVariable String player, @PathVariable String game) {
        Games games = new Games(player, game);
        gamesRepository.save(games);
        return new ResponseEntity<>(String.format("Created %s game: %s",game, games.getCardGame().get(0).getId()), HttpStatus.OK);
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
        if(games == null){
            return new ResponseEntity<>("game not found.", HttpStatus.NOT_FOUND);
        }
        String gameString = games.entrySet().stream()
                .map(entry -> "Game ID: " + entry.getValue().getCardGame().get(0).getId() + ", Title: " + entry.getValue().getCardGame().get(0).getTitle())
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(String.format("List Games:", gameString), HttpStatus.OK);
    }

}