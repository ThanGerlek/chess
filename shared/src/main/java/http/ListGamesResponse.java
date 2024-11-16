package http;

import java.util.ArrayList;

/**
 * A record representing an HTTP response to a ListGamesService request.
 *
 * @param games   a list of games currently in the database
 * @param message the response message
 */
public record ListGamesResponse(ArrayList<GameListItem> games, String message) {
}
