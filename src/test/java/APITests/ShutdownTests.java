package APITests;

import static io.restassured.RestAssured.get;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

public class ShutdownTests extends BaseTestSetup {

	private static final int CREATED_STATUS_CODE = 201;
	private static final int OK_STATUS_CODE = 200;

	public ShutdownTests() {
		RestAssured.baseURI = "http://localhost:4567/shutdown";
	}


	@Test
	public void testShutdownApiServer() throws InterruptedException {
		
		String error = "";
		
		try {
			get("http://localhost:4567/");
			try {
				Response res = get();
				String body = res.getBody().asString();
				System.out.println(body);
			} catch (Exception e) {
				error = e.toString();
				System.out.println(error);
			}
			
			Assert.assertEquals(error.toString().contains("Connection refused"), true);
			
		} catch (Exception e) {
			System.out.println("API not running.");
			Assert.assertEquals(false, true);
		}

		
	}
}
