package server.handlers;

import com.google.gson.Gson;
import dataaccess.*;
import http.MessageResponse;
import io.javalin.http.Context;

public abstract class HttpHandler<S> {
    private final S service;

    protected HttpHandler(S service) {
        this.service = service;
    }

    public static void parseToBody(Context ctx, Object response, int status) {
        String bodyStr = new Gson().toJson(response);
        ctx.contentType("application/json");
        ctx.result(bodyStr);
        ctx.status(status);
    }

    public void handleRequest(Context ctx) {
        String authToken = ctx.header("authentication");
        String body = ctx.body();

        Object response;
        try {
            response = getResponse(body, authToken);
        } catch (NoSuchItemException | BadRequestException e) {
            handleError(ctx, 400, e);
            return;
        } catch (UnauthorizedAccessException e) {
            handleError(ctx, 401, e);
            return;
        } catch (ValueAlreadyTakenException e) {
            handleError(ctx, 403, e);
            return;
        } catch (Exception e) {
            System.out.println("[ERROR] Threw an unknown error: " + e.getMessage());
            handleError(ctx, 500, e);
            return;
        }

        parseToBody(ctx, response, 200);
    }

    private void handleError(Context ctx, int status, Exception e) {
        MessageResponse response = new MessageResponse(String.format("Error: %s", e.getMessage()));
        parseToBody(ctx, response, status);
    }

    protected S getService() {
        return service;
    }

    protected abstract Object getResponse(String body, String authToken) throws DataAccessException;

}
