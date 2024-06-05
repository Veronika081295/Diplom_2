package praktikum;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
public class OrderAssertions {
    @Step("Assert orders retrieved successfully")
    public void assertOrdersRetrievedSuccessfully(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
    @Step("Assert fifty orders retrieved successfully")
    public void assertFiftyOrdersRetrievedSuccessfully(Response response) {
        response.then().assertThat()
                .body("success", equalTo(true))
                .body("orders.size()", greaterThanOrEqualTo(50))
                .statusCode(SC_OK);
    }

    @Step("Assert orders retrieval failed due to unauthorized access")
    public void assertOrdersRetrievalFailed(Response response) {
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Assert order created successfully")
    public void assertOrderCreatedSuccessfully(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Step("Assert order creation failed")
    public void assertOrderCreationFailed(Response response) {
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step("Assert order creation returned error")
    public void assertOrderCreationWithError(Response response) {
        response.then().assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
