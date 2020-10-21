package APITests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProjectsTests {

    public ProjectsTests() {
        RestAssured.baseURI = "http://localhost:4567/projects";

    }

    @Test
    public void testCreateProjectValidInfo() {

        String title = "proj1";
        boolean completed = false;
        boolean active = true;
        String description = "this is a proj";

        String requestBody = withValidInfo(title, completed, active, description);

        RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body(requestBody);

        request.post().then().
                assertThat().
                body("title", equalTo(title),
                        "completed", equalTo(String.valueOf(completed)),
                        "active", equalTo(String.valueOf(active)),
                        "description", equalTo(String.valueOf(description)));


    }

    private String withValidInfo(String title, boolean completed, boolean active, String description) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("completed", completed);
        requestParams.put("active", active);
        requestParams.put("description", description);
        return requestParams.toJSONString();
    }

}
