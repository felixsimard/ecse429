package docs;

import org.junit.Test;

import junit.framework.Assert;

import static io.restassured.RestAssured.*;

public class testDocs {
	
	String url = "http://localhost:4567/";
	String endpoint = "docs";
	
	String api = url + endpoint;
	
	@Test
	public void testResponseCode() {
		int code  = get(api).getStatusCode();
		System.out.println("Received code: " + code);
		Assert.assertEquals(code,  200);
	}
	
	@Test
	public void testBody() {
		Long time = get(api).getTime();
		System.out.println("Response time: " + time);
	}

}
