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
        userDAO.insertNewUser(new User("existingUser", "existingPass", "existingMail"));
        service = new RegisterService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void get_registered_user_returns_user_with_correct_username_and_email() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pwHash1", "mail1"));
        User retrievedUser = userDAO.getUser("user1");
        Assertions.assertEquals(retrievedUser.username(), "user1");
        Assertions.assertEquals(retrievedUser.email(), "email1");
    }

    // Negative test
    @Test
    void register_existing_user_throws_already_taken() {
        Assertions.assertThrows(ValueAlreadyTakenException.class,
                () -> service.register(new RegisterRequest("existingUser", "pass1", "mail1")));
    }

    @Test
    void register_new_user_returns_okay() throws DataAccessException {
        AuthResponse response = service.register(new RegisterRequest("user1", "pass1", "mail1"));
        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void register_with_null_username_throws_bad_request_error() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.register(new RegisterRequest(null, "pass1", "mail1")));
    }

    @Test
    void register_with_null_password_throws_bad_request_error() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.register(new RegisterRequest("user1", null, "mail1")));
    }

    @Test
    void register_with_null_email_returns_user() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", null));
        User retrievedUser = userDAO.getUser("user1");
        Assertions.assertEquals(retrievedUser.username(), "user1");
        Assertions.assertNull(retrievedUser.email());
    }
}