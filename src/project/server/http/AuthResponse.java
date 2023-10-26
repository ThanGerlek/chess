package server.http;

import server.AuthToken;

/**
 * A record representing an HTTP response to an authorization request.
 *
 * @param status    the HTTP status code
 * @param authToken a new authorization token
 * @param username  the username of the user the token represents
 * @param message   the response message
 */
public record AuthResponse(int status, AuthToken authToken, String username, String message) {
}

// Login:
//    Success response	[200] { "username":"", "authToken":"" }
//    Failure response	[401] { "message": "Error: unauthorized" }
//    Failure response	[500] { "message": "Error: description" }

// Register:
//    Success response	[200] { "username":"", "authToken":"" }
//    Failure response	[400] { "message": "Error: bad request" }
//    Failure response	[403] { "message": "Error: already taken" }
//    Failure response	[500] { "message": "Error: description" }
