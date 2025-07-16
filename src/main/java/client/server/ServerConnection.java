package client.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import client.map.GameMap;
import client.map.half_map.HalfMap;
import client.player.Move;
import client.server.converter.DataConverter;
import client.server.player_state.PlayerServerStatus;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;

public class ServerConnection {
	private static final Logger serverLogger = LoggerFactory.getLogger(ServerConnection.class);
	private UniquePlayerIdentifier playerId;
    private final DataConverter dataConverter;
	private final WebClient baseWebClient;
	
	@SuppressWarnings("rawtypes")
	private Mono<ResponseEnvelope>  getRequest(String uriSection) {
		return this.baseWebClient.method(HttpMethod.GET)
				.uri("/" + uriSection).retrieve()
				.bodyToMono(ResponseEnvelope.class);
	}
	
	@SuppressWarnings("rawtypes")
	private <T> Mono<ResponseEnvelope> postRequest(T bodyElement, String uriSection) {
		return this.baseWebClient.method(HttpMethod.POST)
				.uri("/" + uriSection)
				.body(BodyInserters.fromValue(bodyElement)) // specify the data which is sent to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
	}

	private void validatePostResponse(ResponseEnvelope response, String message) throws ServerResponseException {
		if(response == null){
			throw new ServerResponseException(message + "\nResponse is null");
		}
		if (response.getState() == ERequestState.Error) {
			throw new ServerResponseException(message + "\nError message: " + response.getExceptionMessage());
		}
	}
	
	private GameState getGameState() throws ServerResponseException {
		@SuppressWarnings("unchecked")
		ResponseEnvelope<GameState> response = this.getRequest("states/" + this.playerId.getUniquePlayerID()).block();
		if (response == null || response.getState() == ERequestState.Error) {
			throw new ServerResponseException(String.format(
				"unable to get game state. Error message: %s", response == null ? "response is null": response.getExceptionMessage())
			);
		}
		serverLogger.info("Successfully retrieved game data!");
		return response.getData().get();
	}
	
	public ServerConnection(String serverBaseUrl, String gameId) {
        this.dataConverter = new DataConverter();
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games/" + gameId)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // we send XML (cf. network protocol)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE) // we receive XML (cf. network protocol)
				.build();
	}
	
	public void registerPlayer(String playerName, String playerSurname, String playerUAcc) throws ServerResponseException {
		PlayerRegistration playerReg = new PlayerRegistration(playerName, playerSurname, playerUAcc);
		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> response = this.postRequest(playerReg, "players").block();
		validatePostResponse(response, String.format("unable to register a new player: Name: %s; Surname: %s; UAccount: %s;", playerName, playerSurname, playerUAcc));

		this.playerId = response.getData().get();
        serverLogger.info("Player successfully registered with ID: {}", playerId.getUniquePlayerID());
	}
	
	@SuppressWarnings("rawtypes")
	public void sendHalfMap(HalfMap halfMap) throws ServerResponseException {
		PlayerHalfMap playerHalfMap = dataConverter.getHalfMap(playerId.getUniquePlayerID(), halfMap.getFortPosition(), halfMap.getHalfMapNodes());
		ResponseEnvelope response = this.postRequest(playerHalfMap, "halfmaps").block();
		validatePostResponse(response, "unable to send a generated half map;");
        serverLogger.info("Half map successfully sent {}", playerId.getUniquePlayerID());
	}
	
	@SuppressWarnings("rawtypes")
	public void sendMove(Move move) throws ServerResponseException {
		PlayerMove playerMove = PlayerMove.of(this.playerId, dataConverter.getMove(move));
		ResponseEnvelope response = this.postRequest(playerMove, "moves").block();
		validatePostResponse(response, "unable to send a move;");
        serverLogger.info("Move [{}] successfully sent {}", move, playerId.getUniquePlayerID());
	}
	
	public Map.Entry<GameMap, PlayerServerStatus> updateClientInfo(boolean isMapInit) throws ServerResponseException {
		return dataConverter.getClientInfo(getGameState(), isMapInit, playerId.getUniquePlayerID());
	}	
}
