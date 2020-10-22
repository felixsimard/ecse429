package APITests;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;

public abstract class CategoriesHelper {

    private static final String BASEURI = "http://localhost:4567";
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int CREATED_STATUS_CODE = 201;
    private static final int FAILED_STATUS_CODE = 400;

    public static String createCategory(String title, String description)
    {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("description", description);

        RequestSpecification request = given()
                .body(requestBody.toJSONString())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI);

        Response response = request.post("/categories");

        response.then()
                .assertThat().statusCode(CREATED_STATUS_CODE)
                .assertThat().body(containsString("id"))
                .assertThat().body(containsString(title))
                .assertThat().body(containsString(description));

        return response.jsonPath().getString("id");
    }
}
