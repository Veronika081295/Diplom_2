package praktikum;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.response.Response;
import praktikum.RestApiConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderAPI {
    public List<Ingredient> getIngredients() {
        Response response = given().get(RestApiConstants.INGREDIENT_URL);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonData = jsonParser.parse(response.asPrettyString())
                .getAsJsonObject()
                .get("data");
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<Ingredient>>() {}.getType());
    }

    public Response createOrder(List<String> ingredientIds) {
        Map<String, List<String>> requestParams = new HashMap<>();
        requestParams.put("ingredients", ingredientIds);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestParams)
                .when()
                .post(RestApiConstants.ORDER_URL);
    }

    public Response getOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(RestApiConstants.ORDER_URL);
    }
    public Response getFiftyOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(RestApiConstants.ALL_ORDERS_URL);
    }
}
