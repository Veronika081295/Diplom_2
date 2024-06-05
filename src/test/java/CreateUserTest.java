import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import praktikum.User;
import praktikum.UserAPI;
import praktikum.UserAssertions;
import praktikum.UserUtils;
import org.junit.Before;
import org.junit.Test;

import static praktikum.RestApiConstants.BASE_URI;

@DisplayName("Tests for user creation")
public class CreateUserTest {
    private UserAPI userAPI;
    private UserAssertions userAssertions;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;  // Use constant for base URI
        userAPI = new UserAPI();
        userAssertions = new UserAssertions();
    }

    @Test
    @DisplayName("Check status code of create user")
    public void createNewUserTest() {
        User user = UserUtils.getRandomCredentials();
        Response createUserResponse = userAPI.createUser(user);
        String accessToken = createUserResponse.path("accessToken");
        Response deleteUserResponse = userAPI.deleteUser(user, accessToken);
        userAssertions.assertDeleteUserSuccess(deleteUserResponse);
    }

    @Test
    @DisplayName("Check status code of create registered user")
    public void createRegisteredUserTest() {
        User user = UserUtils.getRandomCredentials();
        Response createUserResponse = userAPI.createUser(user);
        Response createDuplicateResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationFailed(createDuplicateResponse);
        String accessToken = createUserResponse.path("accessToken");
        Response deleteUserResponse = userAPI.deleteUser(user, accessToken);
        userAssertions.assertDeleteUserSuccess(deleteUserResponse);
    }

    @Test
    @DisplayName("Check status code of create user without email")
    public void createUserWithoutEmailTest() {
        User user = UserUtils.getRandomCredentials();
        user.setEmail(null);
        Response createResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationFailed(createResponse);
    }

    @Test
    @DisplayName("Check status code of create user without password")
    public void createUserWithoutPasswordTest() {
        User user = UserUtils.getRandomCredentials();
        user.setPassword(null);
        Response createResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationFailed(createResponse);
    }

    @Test
    @DisplayName("Check status code of create user without name")
    public void createUserWithoutNameTest() {
        User user = UserUtils.getRandomCredentials();
        user.setName(null);
        Response createResponse = userAPI.createUser(user);
        userAssertions.assertUserCreationFailed(createResponse);
    }
}
