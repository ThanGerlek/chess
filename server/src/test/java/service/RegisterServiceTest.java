package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.ValueAlreadyTakenException;
import http.AuthResponse;
import http.RegisterRequest;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.RegisterService;

class RegisterServiceTest extends ServiceTest {
    private RegisterService service;

    // TODO 200, 400 bad req, 500

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(createTestUser("existingUser", "existingPass", "existingMail"));
        service = new RegisterService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void getRegisteredUserReturnsUserWithCorrectUsernameAndEmail() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "email1"));
        User retrievedUser = userDAO.getUser("user1");
        Assertions.assertEquals(retrievedUser.username(), "user1");
        Assertions.assertEquals(retrievedUser.email(), "email1");
    }

    // Negative test
    @Test
    void registerExistingUserThrowsAlreadyTaken() {
        Assertions.assertThrows(ValueAlreadyTakenException.class,
                () -> service.register(new RegisterRequest("existingUser", "pass1", "mail1")));
    }

    @Test
    void registerNewUserReturnsOkay() throws DataAccessException {
        AuthResponse response = service.register(new RegisterRequest("user1", "pass1", "mail1"));
        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void registerWithNullUsernameThrowsBadRequestError() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.register(new RegisterRequest(null, "pass1", "mail1")));
    }

    @Test
    void registerWithNullPasswordThrowsBadRequestError() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.register(new RegisterRequest("user1", null, "mail1")));
    }

    @Test
    void registerWithNullEmailReturnsUser() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", null));
        User retrievedUser = userDAO.getUser("user1");
        Assertions.assertEquals(retrievedUser.username(), "user1");
        Assertions.assertNull(retrievedUser.email());
    }
}
