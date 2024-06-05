package praktikum;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
public class UserAssertions {
    @Step("Assert user creation successful")
    public void assertUserCreationSuccessful(Response createResponse) {
        createResponse.then().assertThat()
                .body("success", equalTo(true))
                .statusCode(SC_OK);
    }

    @Step("Assert change user response")
    public void assertChangeUserResponse(Response changeResponse, User changedUser) {
        changeResponse.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
        String email = changeResponse.path("user.email");
        String name = changeResponse.path("user.name");

        Assert.assertThat(email, equalToIgnoringCase(changedUser.getEmail()));
        Assert.assertThat(name, equalToIgnoringCase(changedUser.getName()));
    }

    @Step("Assert change user with existing email failure")
    public void assertChangeUserWithExistingEmailFailure(Response changeResponse) {
        changeResponse.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Assert change unauthorized user response")
    public void assertChangeUnauthorizedUserResponse(Response changeResponse) {
        changeResponse.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Assert user creation failed")
    public void assertUserCreationFailed(Response response) {
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Assert login user success")
    public void assertLoginUserSuccess(Response loginResponse) {
        loginResponse.then().assertThat()
                .body("success", equalTo(true))
                .statusCode(SC_OK);
    }

    @Step("Assert login user failure")
    public void assertLoginUserFailure(Response loginResponse) {
        loginResponse.then().assertThat()
                .body("success", equalTo(false))
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Assert logout user success")
    public void assertLogoutUserSuccess(Response logoutResponse) {
        logoutResponse.then().assertThat()
                .body("success", equalTo(true))
                .statusCode(SC_OK);
    }
    @Step("Assert delete user success")
    public void assertDeleteUserSuccess(Response deleteUser) {
        deleteUser.then().assertThat()
                .body("success", equalTo(true))
                .statusCode(SC_ACCEPTED);
    }
}
