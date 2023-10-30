package server.http;

/**
 * A record representing an HTTP request to the {@link services.CreateGameService}.
 *
 * @param gameName a human-readable name for the game to create
 */
public record CreateGameRequest(String gameName) {
    //    { "gameName":"" }
}
