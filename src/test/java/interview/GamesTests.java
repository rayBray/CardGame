package interview;

import interview.controller.GamesController;
import interview.model.Games;
import interview.repository.GamesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GamesTests {

    private MockMvc mockMvc;

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private Games games;

    @InjectMocks
    private GamesController gamesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gamesController).build();
    }

    @Test
    void testCreateGame() throws Exception {
        Games games1 = games();
        when(gamesRepository.findById(games1.getId())).thenReturn(games1);

        mockMvc.perform(post("/games/{gameId}/{cardPlayerName}","Poker", "JohnDoe"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testRemoveGameSuccess() throws Exception {
        when(gamesRepository.removeGame(anyString())).thenReturn(games);
        when(games.getId()).thenReturn("gameId123");
        ResponseEntity<String> response = gamesController.removeGame("gameId123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        }


    private Games games(){
        return new Games("JohnDoe","Poker");
    }
}
