package APITests;

import org.junit.Test;

import junit.framework.Assert;

import static io.restassured.RestAssured.*;

public class DocsTests {
	
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
