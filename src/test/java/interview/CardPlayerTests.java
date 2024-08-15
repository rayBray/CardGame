package interview;

import interview.controller.CardPlayerController;
import interview.model.Card;
import interview.model.CardGame;
import interview.model.CardPlayer;
import interview.repository.CardGameRepository;
import interview.repository.CardPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes=CardGameApplicationTests.class)
@AutoConfigureMockMvc
public class CardPlayerTests {

    private MockMvc mockMvc;

    @Mock
    private CardPlayerRepository cardPlayerRepository;

    @Mock
    private CardGameRepository cardGameRepository;

    @InjectMocks
    private CardPlayerController cardPlayerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardPlayerController).build();
    }

    @Test
    public void testCreateCardPlayer() throws Exception {
        CardPlayer cardPlayer = cardPlayer();
        when(cardPlayerRepository.findById(CardPlayer.getId())).thenReturn(cardPlayer);
        CardGame cardGame = cardGame();
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        mockMvc.perform(post("/card_player/{gameId}/{cardPlayerName}", cardPlayer.getGameId(), cardPlayer.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Created %s", cardPlayer)))
                .andReturn();
    }

    @Test
    public void testGetCardPlayer() throws Exception {
        CardPlayer cardPlayer = cardPlayer();
        when(cardPlayerRepository.findById(CardPlayer.getId())).thenReturn(cardPlayer);
        mockMvc.perform(get("/card_player/{cardPlayerId}", cardPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(cardPlayer.toString()))
                .andReturn();
    }

    @Test
    public void testRemoveCardPlayer() throws Exception {
        CardPlayer cardPlayer = cardPlayer();
        when(cardPlayerRepository.removeCardPlayer(anyString())).thenReturn(cardPlayer);
        CardGame cardGame = cardGame();
        when(cardGameRepository.findById(anyString())).thenReturn(cardGame);
        cardGame.addCardPlayer(cardPlayer);

        mockMvc.perform(delete("/card_player/{gameId}/{cardPlayerId}", cardGame.getId(), cardPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Removed Card Player: \n  %s  ", cardPlayer)))
                .andReturn();
        verify(cardPlayerRepository, times(1)).removeCardPlayer(cardPlayer.getId());
    }

    @Test
    public void testGetCardPlayerHand() throws Exception {
        CardPlayer cardPlayer = cardPlayer();
        cardPlayer.appendToHand(new Card("Hearts", "2",2));
        cardPlayer.appendToHand(new Card("Hearts", "3",3));
        cardPlayer.appendToHand(new Card("Hearts", "4",4));

        when(cardPlayerRepository.findById(anyString())).thenReturn(cardPlayer);

        mockMvc.perform(get("/card_player/{cardPlayerId}/current_hand", cardPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("cardPlayer current hand: 2 of Hearts, 3 of Hearts, 4 of Hearts"))
                .andReturn();

        verify(cardPlayerRepository, times(1)).findById(cardPlayer.getId());
    }

    @Test
    public void testGetCardPlayerScore() throws Exception {
        CardPlayer cardPlayer =  cardPlayer();
        when(cardPlayerRepository.findById(anyString())).thenReturn(cardPlayer);
        mockMvc.perform(get("/card_player/{cardPlayerId}/score", cardPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Card Player %s score: %s", cardPlayer.getName(), cardPlayer.getScore())))
                .andReturn();
    }

    private CardPlayer cardPlayer(){
        String gameId = "game1";
        String cardPlayerName = "JohnDoe";
        return new CardPlayer(cardPlayerName, gameId);
    }
    private CardGame cardGame(){
        return new CardGame("G1",  "game1","JaneDoe");
    }
}
