package docs;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import junit.framework.Assert;

import static io.restassured.RestAssured.*;

public class testDocs {
	
	String url = "http://localhost:4567/";
	String endpoint = "docs";
	
	String api = url + endpoint;
	
	@Test
	public void testPageLoad() {
		int code  = get(api).getStatusCode();
		Assert.assertEquals(code,  200);
	}
	
	@Test
	public void testBodyAsHtml() {
		String body = get(api).getBody().asString();
		Assert.assertEquals(body.contains("<html>"), true);
	}

}
