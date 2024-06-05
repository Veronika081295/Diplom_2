import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.RestApiConstants;
import praktikum.OrderAPI;
import praktikum.OrderAssertions;
import praktikum.User;
import praktikum.UserAPI;
import praktikum.UserUtils;
import praktikum.UserAssertions;

@DisplayName("Tests for orders retrieving")
public class GetOrderTest {
    private OrderAPI orderAPI;
    private UserAPI userAPI;
    private UserAssertions userAssertions;
    private OrderAssertions orderAssertions;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = RestApiConstants.BASE_URI;
        orderAPI = new OrderAPI();
        userAPI = new UserAPI();
        userAssertions = new UserAssertions();
        orderAssertions = new OrderAssertions();

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
    @DisplayName("Check status code of get orders when user authorized")
    public void getOrdersAuthorizedTest() {
        Response getOrdersResponse = orderAPI.getOrders(accessToken);
        orderAssertions.assertOrdersRetrievedSuccessfully(getOrdersResponse);
    }
    @Test
    @DisplayName("Check if fifty orders in response")
    public void getFiftyOrdersTest() {
        Response getFiftyOrdersResponse = orderAPI.getFiftyOrders(accessToken);
        orderAssertions.assertFiftyOrdersRetrievedSuccessfully(getFiftyOrdersResponse);
    }
    @Test
    @DisplayName("Check status code of get orders when user not authorized")
    public void getOrdersUnauthorizedTest() {
        Response getOrdersResponse = orderAPI.getOrders("");
        orderAssertions.assertOrdersRetrievalFailed(getOrdersResponse);
    }
}
