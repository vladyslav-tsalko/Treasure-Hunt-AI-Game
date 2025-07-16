package client_test.server;

import client.main.GameClient;
import client.map.GameMap;
import client.map.Position;
import client.map.Terrain;
import client.map.half_map.HalfMap;
import client.server.ServerConnection;
import client.server.ServerResponseException;
import client.server.player_state.PlayerServerStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import messagesbase.messagesfromclient.PlayerRegistration;

import java.util.*;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServerConnection_Tests_UsingJUnit5AndMockito {
    @Mock
    private ServerConnection serverConnectionMock;
    private GameClient gameClientMock;
    private static String name = "Vladyslav";
    private static String surname = "Tsalko";
    private static String uid = "tsalkov21";
    /*void setUpServerConnection() {
        serverConnectionMock = new ServerConnection("serverBaseUrl", "foundMockGame");
    }*/

    @Test
    void testRegistrationFails() {
        try(MockedConstruction<ServerConnection> mockServerConnection = Mockito.mockConstruction(ServerConnection.class, (mock, context) -> {
            if (!"foundMockGame".equalsIgnoreCase(context.arguments().get(1).toString())) {
                doThrow(new ServerResponseException("Server Response Exception: unable to register a new player: Name: ...; Surname: ...; UAccount: ...;\n" +
                        "Error message: Match was not found, i.e., a game with the given game ID did not exist, checked for game ID: GameIdentifier=sSK4Q Check for typical errors: a) the provided game ID wasn't correct; or b) the game and player ID were mixed; or c) the game became to old and, thus, was deleted."))
                        .when(mock).registerPlayer(any(String.class), any(String.class), any(String.class));
            }
        })){
            serverConnectionMock = new ServerConnection("serverBaseUrl", "notFoundMockGame");
            assertThrows(ServerResponseException.class, () -> serverConnectionMock.registerPlayer(name, surname, uid));
        }
    }

    @Test
    void testRegistrationSucceeds() {
        try(MockedConstruction<ServerConnection> mockServerConnection = Mockito.mockConstruction(ServerConnection.class, (mock, context) -> {
            if (!"foundMockGame".equalsIgnoreCase(context.arguments().get(1).toString())) {
                doThrow(new ServerResponseException("Server Response Exception: unable to register a new player: Name: ...; Surname: ...; UAccount: ...;\n" +
                        "Error message: Match was not found, i.e., a game with the given game ID did not exist, checked for game ID: GameIdentifier=sSK4Q Check for typical errors: a) the provided game ID wasn't correct; or b) the game and player ID were mixed; or c) the game became to old and, thus, was deleted."))
                        .when(mock).registerPlayer(any(String.class), any(String.class), any(String.class));
            }
        })){
            serverConnectionMock = new ServerConnection("serverBaseUrl", "foundMockGame");
            assertDoesNotThrow(() -> serverConnectionMock.registerPlayer(name, surname, uid));
        }
    }

    @Test
    void testUpdateClientInfoGameMapSize() {
        //setUpServerConnection();
        ServerConnection mockedServerConnection = Mockito.mock(ServerConnection.class);
        LinkedHashMap<Position, Terrain> terrains100 = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                terrains100.put(new Position(i, j), Terrain.EMPTY);
            }
        }

        GameMap gameMap100 = new GameMap(terrains100, Position.getUninitializedPosition(), Position.getUninitializedPosition(),Position.getUninitializedPosition(),Position.getUninitializedPosition(),Position.getUninitializedPosition());
        try{
            Mockito.when(mockedServerConnection.updateClientInfo(false)).thenReturn(Map.entry(gameMap100, Mockito.mock(PlayerServerStatus.class)));
            Mockito.when(mockedServerConnection.updateClientInfo(true)).thenReturn(Map.entry(Mockito.mock(GameMap.class), Mockito.mock(PlayerServerStatus.class)));
            Assertions.assertEquals(100, mockedServerConnection.updateClientInfo(false).getKey().getGameMapNodes().size(), "Expected to be mocked to 100");
            Assertions.assertEquals(0, mockedServerConnection.updateClientInfo(true).getKey().getGameMapNodes().size(), "Expected to be mocked to 0");
        } catch (ServerResponseException e) {
            System.out.println("Server Response Exception: " + e.getMessage());
        }

    }
}
