package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.NoSuchItemException;
import dataAccess.UnauthorizedAccessException;
import dataAccess.ValueAlreadyTakenException;
import server.http.MessageResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class Handler {
    protected Gson gson = new Gson();

    public Object handleRequest(Request req, Response res) {
        return defaultErrorHandler(req, res, this::route);
    }

    protected Object defaultErrorHandler(Request req, Response res, Route route) {
        try {
            return route.handle(req, res);
        } catch (NoSuchItemException e) {
            return handleError(res, 400, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            return handleError(res, 401, e.getMessage());
        } catch (ValueAlreadyTakenException e) {
            return handleError(res, 403, e.getMessage());
        } catch (Exception e) {
            return handleError(res, 500, e.getMessage());
        }
    }

    protected abstract Object route(Request request, Response response) throws DataAccessException;

    protected String handleError(Response res, int status, String errMsg) {
        res.status(status);
        MessageResponse response = new MessageResponse(0, String.format("Error: %s", errMsg));
        return gson.toJson(response);
    }

}
