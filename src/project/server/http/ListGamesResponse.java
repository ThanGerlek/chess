package server.http;

/**
 * A record representing an HTTP response to a List Games service call.
 *
 * @param status  the HTTP status code
 * @param games   a list of games currently in the database
 * @param message the response message
 */
public record ListGamesResponse(int status, GameListItem[] games, String message) {
}

//    Success response	[200] { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
//    Failure response	[401] { "message": "Error: unauthorized" }
//    Failure response	[500] { "message": "Error: description" }
