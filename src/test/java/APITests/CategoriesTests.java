package APITests;

import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class CategoriesTests {

    private static final String BASEURI = "http://localhost:4567";
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int CREATED_STATUS_CODE = 201;
    private static final int FAILED_STATUS_CODE = 400;

    @Test
    public void testCreateValidCategoryWithTitleAndDescription() {

        String title = "Jet Category";
        String description = "Working from private jet";

        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("description", description);

        given()
                .body(requestBody.toJSONString())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .post("/categories")

                .then()
                .assertThat().statusCode(CREATED_STATUS_CODE)
                .assertThat().body(containsString("id"))
                .assertThat().body(containsString(title))
                .assertThat().body(containsString(description));
    }

    @Test
    public void testCreateValidCategoryOnlyTitle() {

        String title = "Jet Category";

        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);

        given()
                .body(requestBody.toJSONString())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .post("/categories")

                .then()
                .assertThat().statusCode(CREATED_STATUS_CODE)
                .assertThat().body(containsString("id"))
                .assertThat().body(containsString(title));
    }

    @Test
    public void testCreateCategoryNoTitle() {

        String description = "Working from private jet";

        JSONObject requestBody = new JSONObject();
        requestBody.put("description", description);

        given()
                .body(requestBody.toJSONString())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .post("/categories")

                .then()
                .assertThat().statusCode(FAILED_STATUS_CODE);
    }

    @Test
    public void testGetAllCategories() {
        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .get("/categories")

                .then()
                .assertThat().statusCode(SUCCESS_STATUS_CODE);
    }

    @Test
    public void testGetCategoryFromValidId() {
        String id = CategoriesHelper.createCategory("Test title", "Test description");

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .get("/categories/" + id)

                .then()
                .assertThat().statusCode(SUCCESS_STATUS_CODE)
                .assertThat().body(containsString(id));
    }

    @Test
    public void testDeleteCategoryFromValidId() {

        String id = CategoriesHelper.createCategory("Test title", "Test description");

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .baseUri(BASEURI)

                .delete("/categories/" + id)

                .then()
                .assertThat().statusCode(SUCCESS_STATUS_CODE);
    }

    @Test
    public void testModifyCategoryTitleAndDescriptionFromValidId() {
        String old_title = "Old title";
        String old_description = "Old description";

        String new_title = "New title";
        String new_description = "New description";

        String id = CategoriesHelper.createCategory(old_title, old_description);

        JSONObject requestBody = new JSONObject();
        requestBody.put("title", new_title);
        requestBody.put("description", new_description);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody.toJSONString())
                .baseUri(BASEURI)

                .post("/categories/" + id)

                .then()
                .assertThat().statusCode(SUCCESS_STATUS_CODE)
                .assertThat().body(containsString(id))
                .assertThat().body(containsString(new_title))
                .assertThat().body(containsString(new_description));
    }

    @Test
    public void testGetAllProjectsLinkedToCategoryFromValidId() {

    }
}
