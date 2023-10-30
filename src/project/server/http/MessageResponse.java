package server.http;

/**
 * A record representing a simple HTTP response with only a message header. Used by
 * {@link services.ClearApplicationService}, {@link services.JoinGameService}, and {@link services.LogoutService}.
 *
 * @param message the response message
 */
public record MessageResponse(String message) {
}

// Clear application:
//    Success response	[200]
//    Failure response	[500] { "message": "Error: description" }

// Join game:
//    Success response	[200]
//    Failure response	[400] { "message": "Error: bad request" }
//    Failure response	[401] { "message": "Error: unauthorized" }
//    Failure response	[403] { "message": "Error: already taken" }
//    Failure response	[500] { "message": "Error: description" }

// Logout:
//    Success response	[200]
//    Failure response	[401] { "message": "Error: unauthorized" }
//    Failure response	[500] { "message": "Error: description" }
