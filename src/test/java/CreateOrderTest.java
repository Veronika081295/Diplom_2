import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.RestApiConstants;
import praktikum.Ingredient;
import praktikum.OrderAPI;
import praktikum.OrderAssertions;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Tests for orders creation")
public class CreateOrderTest {
    private OrderAPI orderAPI;
    private OrderAssertions orderAssertions;

    @Before
    public void setUp() {
        RestAssured.baseURI = RestApiConstants.BASE_URI;
        orderAPI = new OrderAPI();
        orderAssertions = new OrderAssertions();
    }

    @Test
    @DisplayName("Check status code of create order")
    public void createOrderTest() {
        List<String> ingredientIds = getIngredientIds();
        Response response = createOrder(ingredientIds);
        orderAssertions.assertOrderCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Check status code of create order without ingredients")
    public void createOrderWithoutIngredientsTest() {
        List<String> ingredientIds = new ArrayList<>();
        Response response = createOrder(ingredientIds);
        orderAssertions.assertOrderCreationFailed(response);
    }

    @Test
    @DisplayName("Check status code of create order with wrong ingredients")
    public void createOrderWithWrongIngredientsTest() {
        List<String> ingredientIds = new ArrayList<>();
        ingredientIds.add("wrongId");
        Response response = createOrder(ingredientIds);
        orderAssertions.assertOrderCreationWithError(response);
    }

    @Step("Get ingredient IDs")
    private List<String> getIngredientIds() {
        List<Ingredient> ingredients = orderAPI.getIngredients();
        List<String> ingredientIds = new ArrayList<>();
        if (ingredients.size() >= 3) {
            ingredientIds.add(ingredients.get(0).get_id());
            ingredientIds.add(ingredients.get(1).get_id());
            ingredientIds.add(ingredients.get(2).get_id());
        } else {
            throw new RuntimeException("Not enough ingredients available");
        }
        return ingredientIds;
    }

    @Step("Create an order with ingredient IDs and access token")
    private Response createOrder(List<String> ingredientIds) {
        return orderAPI.createOrder(ingredientIds);
    }
}
