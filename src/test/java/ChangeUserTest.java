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

@DisplayName("Tests for user change")
public class ChangeUserTest {
    private UserAPI userAPI;
    private UserAssertions userAssertions;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = RestApiConstants.BASE_URI;
        userAPI = new UserAPI();
        userAssertions = new UserAssertions();

        user = UserUtils.getRandomCredentials();
        Response createUserResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationSuccessful(createUserResponse);

        accessToken = createUserResponse.path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null && user != null) {
            Response deleteUserResponse = userAPI.deleteUser(user, accessToken);
            userAssertions.assertDeleteUserSuccess(deleteUserResponse);
        }
    }

    @Test
    @DisplayName("Check status code of change authorized user")
    public void changeAuthorizedUserTest() {
        // Change user details
        User changedUser = UserUtils.getRandomCredentials();
        Response changeResponse = userAPI.changeUser(changedUser, accessToken);
        userAssertions.assertChangeUserResponse(changeResponse, changedUser);

        // Verify login with new details
        Response loginResponse = userAPI.loginUser(changedUser);
        userAssertions.assertLoginUserSuccess(loginResponse);

    }

    @Test
    @DisplayName("Check status code of change authorized user with existing email")
    public void changeAuthorizedUserWithExistingEmailTest() {
        // Create the second user
        User secondUser = UserUtils.getRandomCredentials();
        Response createSecondUserResponse = userAPI.createUser(secondUser);
        userAssertions.assertUserCreationSuccessful(createSecondUserResponse);

        // Attempt to change the first user's email to the second user's email
        User changedUser = new User(secondUser.getEmail(), user.getPassword(), user.getName());
        Response changeResponse = userAPI.changeUser(changedUser, accessToken);
        userAssertions.assertChangeUserWithExistingEmailFailure(changeResponse);

        // Clean up: delete both created users
        String secondUserAccessToken = createSecondUserResponse.path("accessToken");
        Response deleteSecondUserResponse = userAPI.deleteUser(secondUser, secondUserAccessToken);
        userAssertions.assertDeleteUserSuccess(deleteSecondUserResponse);
    }


    @Test
    @DisplayName("Check status code of change unauthorized user")
    public void changeUnauthorizedUserTest() {
        // Attempt to change user details without authorization
        User changedUser = UserUtils.getRandomCredentials();
        Response changeResponse = userAPI.changeUser(changedUser, "");
        userAssertions.assertChangeUnauthorizedUserResponse(changeResponse);
    }
}
