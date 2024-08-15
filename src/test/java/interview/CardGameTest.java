package interview;

import interview.model.Card;
import interview.repository.CardPlayerRepository;
import interview.repository.CardGameRepository;
import interview.controller.CardGameController;

import interview.model.CardGame;
import interview.model.Deck;
import interview.model.CardPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
public class CardGameTest {

    private MockMvc mockMvc;

    @Mock
    private CardPlayerRepository cardPlayerRepository;

    @InjectMocks
    private CardGameController cardGameController;

    @Mock
    private CardGameRepository cardGameRepository;

    @Mock
    private CardGame cardGame;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardGameController).build();
    }

    @Test
    void testAddDeck() {
        Deck deck = mock(Deck.class);
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        when(cardGame.getDeck()).thenReturn(deck);
        when(deck.getCards()).thenReturn(Arrays.asList(new Card("HEART","2", 2), new Card("SPADE", "2", 2))); // Mock card list size

        ResponseEntity<String> response = cardGameController.addDeck("gameId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deck added, current number of cards in deck: 2", response.getBody());
        verify(deck, times(1)).addCards();
    }

    @Test
    void testShuffleDeck() {
        Deck deck = mock(Deck.class);
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        when(cardGame.getDeck()).thenReturn(deck);

        ResponseEntity<String> response = cardGameController.shuffleDeck("gameId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deck shuffled", response.getBody());
        verify(deck, times(1)).shuffle();
    }

    @Test
    void testRemainingCards() throws Exception {
        CardGame cardGame = game();
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        mockMvc.perform(get("/card_game/{gameId}/remaining_cards", cardGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(cardCount()))
                .andReturn();
    }

    @Test
    void testGetSuitCount() throws Exception {
        CardGame cardGame = game();
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        mockMvc.perform(get("/card_game/{gameId}/suit_count", cardGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("13 Hearts\n" +
                        "13 Spades\n" +
                        "13 Clubs\n" +
                        "13 Diamonds\n"))
                .andReturn();
    }

    @Test
    void testGetPlayerRanks() throws Exception {
        CardPlayer cardPlayer = cardPlayer();
        CardGame cardGame = game();
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        when(cardPlayerRepository.findById(anyString())).thenReturn(cardPlayer);
        CardPlayer cardPlayer1 = new CardPlayer("JaneDoe", game().getId());
        cardGame.addCardPlayer(cardPlayer1);
        cardPlayer1.appendToHand(cardGame.getDeck().takeCard());

        mockMvc.perform(get("/card_game/{gameId}/player_rank", cardGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("JaneDoe: 1, JohnDoe: 0"))
                .andReturn();
    }

    private CardGame game(){
        return new CardGame("G1", "JohnDoe");
    }
    private  CardPlayer cardPlayer(){
        return new CardPlayer("JohnDoe","G1");
    }

    private String cardCount(){
        return "Hearts: 1 King, 1 Queen, 1 Jack, 1 10, 1 9, 1 8, 1 7, 1 6, 1 5, 1 4, 1 3, 1 2, 1 Ace\n" +
                "Spades: 1 King, 1 Queen, 1 Jack, 1 10, 1 9, 1 8, 1 7, 1 6, 1 5, 1 4, 1 3, 1 2, 1 Ace\n" +
                "Clubs: 1 King, 1 Queen, 1 Jack, 1 10, 1 9, 1 8, 1 7, 1 6, 1 5, 1 4, 1 3, 1 2, 1 Ace\n" +
                "Diamonds: 1 King, 1 Queen, 1 Jack, 1 10, 1 9, 1 8, 1 7, 1 6, 1 5, 1 4, 1 3, 1 2, 1 Ace\n";
    }
}
