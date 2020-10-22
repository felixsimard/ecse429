package APITests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.*;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProjectsTests {

    private static final int CREATED_STATUS_CODE = 201;
    private static final int OK_STATUS_CODE = 200;
    public ProjectsTests() {
        RestAssured.baseURI = "http://localhost:4567/projects";
    }

    @Before
    public void setUp() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                statusCode(CREATED_STATUS_CODE).
                body("title", equalTo(title),
                        "completed", equalTo(String.valueOf(completed)),
                        "active", equalTo(String.valueOf(active)),
                        "description", equalTo(String.valueOf(description)));
    }

    @Test
    public void testCreateProjectNoInfo() {

        RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");

        request.post().then().
                assertThat().
                statusCode(CREATED_STATUS_CODE).
                body("title", equalTo(""),
                        "completed", equalTo(String.valueOf(false)),
                        "active", equalTo(String.valueOf(false)),
                        "description", equalTo(String.valueOf("")));


    }

    @Test
    public void testGetAllProjects() {

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
