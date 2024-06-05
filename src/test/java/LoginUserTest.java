import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import praktikum.RestApiConstants;
import praktikum.User;
import praktikum.UserAPI;
import praktikum.UserAssertions;
import praktikum.UserUtils;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Tests for user log in")
public class LoginUserTest {
    private UserAPI userAPI;
    private UserAssertions userAssertions;
    private User user;
    private User userLogin;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = RestApiConstants.BASE_URI;
        userAPI = new UserAPI();
        userAssertions = new UserAssertions();

        user = UserUtils.getRandomCredentials();
        Response createUserResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationSuccessful(createUserResponse);

        // Extract accessToken from the createUserResponse
        accessToken = createUserResponse.path("accessToken");

        // Initialize userLogin with the same credentials as user
        userLogin = new User(user.getEmail(), user.getPassword(), user.getName());
    }

    @After
    public void cleanUp() {
        if (accessToken != null && user != null) {
            Response deleteUserResponse = userAPI.deleteUser(user, accessToken);
            userAssertions.assertDeleteUserSuccess(deleteUserResponse);
        }
    }

    @Test
    @DisplayName("Check status code of login user")
    public void loginUserTest() {
        // Attempt to log in with the newly created user
        Response loginResponse = userAPI.loginUser(userLogin);
        userAssertions.assertLoginUserSuccess(loginResponse);
    }

    @Test
    @DisplayName("Check status code of login user with wrong email")
    public void loginUserWithWrongEmailTest() {
        // Modify the email to an incorrect value
        userLogin.setEmail("1234" + userLogin.getEmail());

        // Attempt to log in with the incorrect email
        Response loginResponse = userAPI.loginUser(userLogin);
        userAssertions.assertLoginUserFailure(loginResponse);
    }

    @Test
    @DisplayName("Check status code of login user with wrong password")
    public void loginUserWithWrongPasswordTest() {
        // Modify the password to an incorrect value
        userLogin.setPassword(userLogin.getPassword() + "1234");

        // Attempt to log in with the incorrect password
        Response loginResponse = userAPI.loginUser(userLogin);
        userAssertions.assertLoginUserFailure(loginResponse);
    }

    @Test
    @DisplayName("Check status code of logout user")
    public void logoutUserTest() {
        // Log in with the newly created user
        Response loginResponse = userAPI.loginUser(userLogin);
        userAssertions.assertLoginUserSuccess(loginResponse);

        // Extract the refresh token from the login response
        String refreshToken = loginResponse.path("refreshToken");

        // Log out using the refresh token
        Response logoutResponse = userAPI.logoutUser(refreshToken);
        userAssertions.assertLogoutUserSuccess(logoutResponse);
    }
}
