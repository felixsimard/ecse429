package APITests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.Assert;

public class ShutdownTests extends BaseTestSetup {

	private static final int OK_STATUS_CODE = 200;

	public ShutdownTests() {
		RestAssured.baseURI = "http://localhost:4567/shutdown";
	}

	@Test
	public void testShutdownApiServer() throws InterruptedException {
			
		String error = "";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
		
		// Assert that application is currently running
		try {
	        request.get("http://localhost:4567/").then().
	        			assertThat().
	        			statusCode(equalTo(OK_STATUS_CODE));
		} catch (Exception e) {
			System.out.println("Shutdown Test: Application is initially not running.");
		}
		
		// Request the shutdown endpoint
		try {
			request.get();
		} catch (Exception e) {
			System.out.println("Shutdown Test: Shutdown endpoint called.");
		}
		
		// Assert that application is shutdown (not running)
		try {
			Response res = get("http://localhost:4567/");
			String body = res.getBody().asString();
			System.out.println(body);
		} catch (Exception e) {
			error = e.toString();
			System.out.println(error);
		}
		Assert.assertEquals(error.toString().contains("Connection refused"), true);

	}
}
