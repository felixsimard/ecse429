package APITests;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ProjectsTests {

    @Before
    public ProjectsTests

    @Test
    public void testCreateProjectValidInfo() {

        String requestBody = withValidInfo("proj1", false, true, "this is proj 1");

        given()


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
