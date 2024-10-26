package server.handlers;

import dataaccess.DataAccessException;
import server.services.LogoutService;

public class LogoutHandler extends HttpHandler<LogoutService> {
    public LogoutHandler(LogoutService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        return getService().logout(authToken);
    }
}
