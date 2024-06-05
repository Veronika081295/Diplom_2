package praktikum;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.RestApiConstants;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
public class UserAPI {
    @Step("Create a new user")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(RestApiConstants.REGISTER_URL);
    }

    @Step("Delete user")
    public Response deleteUser(User user, String token) {
        return given()
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .delete(RestApiConstants.USER_URL);
    }
    @Step("Login user")
    public Response loginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(RestApiConstants.LOGIN_URL);
    }
    @Step("Logout user")
    public Response logoutUser(String token) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("token", token);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestParams)
                .when()
                .post(RestApiConstants.LOGOUT_URL);
    }

    @Step("Change user")
    public Response changeUser(User user, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .patch(RestApiConstants.USER_URL);
    }
}
