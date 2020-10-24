package APITests;

import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.path.xml.XmlPath.CompatibilityMode.HTML;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Assert;

public class DocsTests extends BaseTestSetup {

	private static final int CREATED_STATUS_CODE = 201;
	private static final int OK_STATUS_CODE = 200;
	private static final String EXPECTED_HTML_DOC_TITLE = "API Documentation";

	public DocsTests() {
		RestAssured.baseURI = "http://localhost:4567/docs";
	}

	@Test
	public void testPageLoad() {
		
		RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        
        request.get().then().
        			assertThat().
        			statusCode(equalTo(OK_STATUS_CODE));
	}

	@Test
	public void testBodyAsHtml() {
		
		RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        
        Response res = request.get().then().contentType(ContentType.HTML).extract().response();
        
        XmlPath htmlpath = new XmlPath(HTML, res.getBody().asString());
        
        Assert.assertEquals(htmlpath.getString("html.head.title"), EXPECTED_HTML_DOC_TITLE);

	}

}
