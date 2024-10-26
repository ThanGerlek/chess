package server.handlers;

import dataaccess.DataAccessException;
import server.services.ClearApplicationService;

public class ClearApplicationHandler extends HttpHandler<ClearApplicationService> {
    public ClearApplicationHandler(ClearApplicationService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        return getService().clearApplication();
    }

}
