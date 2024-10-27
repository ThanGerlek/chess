package server.webSocket;

import dataaccess.*;
import http.ChessSerializer;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorServerMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketServer {
    private final UserGameCommandHandler cmdHandler;

    public WebSocketServer(AuthDAO authDAO, GameDAO gameDAO) {
        this.cmdHandler = new UserGameCommandHandler(authDAO, gameDAO, this);
    }

    public void setUpWebSocketHandlers(WsConfig ws) {
        ws.onMessage(this::onMessage);
        ws.onError(this::onWebSocketError);
    }

    public void onMessage(WsMessageContext ctx) {
        String message = ctx.message();
        Session session = ctx.session;
        try {
            UserGameCommand gameCommand = ChessSerializer.gson().fromJson(message, UserGameCommand.class);
            switch (gameCommand.getCommandType()) {
                case CONNECT -> cmdHandler.parseAsConnect(session, message);
                case MAKE_MOVE -> cmdHandler.parseAsMakeMove(session, message);
                case LEAVE -> cmdHandler.parseAsLeave(session, message);
                case RESIGN -> cmdHandler.parseAsResign(session, message);
            }
        } catch (UnauthorizedAccessException e) {
            sendError(session, e, "Invalid token. Are you logged in correctly?");
        } catch (BadRequestException e) {
            sendError(session, e, "Sorry, you can't do that right now.");
        } catch (NoSuchItemException e) {
            sendError(session, e, "Not found. Did you enter everything correctly?");
        } catch (Exception e) {
            sendError(session, e, "Sorry, there's an unknown problem. Please try again.");
            System.out.println("Caught generic throwable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendError(Session session, Throwable e, String errMsg) {
        System.err.println("Server sent an error while parsing UserGameCommand: '" + errMsg + "'\n\tOriginal error: " + e.getMessage());
        send(session, new ErrorServerMessage(errMsg));
    }

    public void send(Session session, ServerMessage serverMessage) {
        String messageJson = ChessSerializer.gson().toJson(serverMessage);
        try {
            System.out.println("Sending to session " + session.hashCode() + " with message " + messageJson);
            session.getRemote().sendString(messageJson);
        } catch (java.nio.channels.ClosedChannelException e) {
            System.err.println("Failed to send WebSocket message, channel was closed");
        } catch (IOException e) {
            System.err.println("Failed to send WebSocket message with error: " + e.getMessage());
        }
    }

    public void onWebSocketError(WsErrorContext ctx) {
        if (ctx.error() != null) {
            System.err.println("Server threw uncaught WebSocket error: " + ctx.error().getMessage());
        }
        else {
            System.err.println("onWebSocketError() was called, but ctx.error() was null");
        }
    }

    public void sendError(Session session, String errMsg) {
        System.err.println("Server sent an error while parsing UserGameCommand: " + errMsg);
        send(session, new ErrorServerMessage(errMsg));
    }

}