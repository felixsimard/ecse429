package todos;

import org.junit.Test;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.*;
import io.restassured.specification.*;
import junit.framework.Assert;

import static io.restassured.RestAssured.*;

public class testTodos {
	
	String url = "http://localhost:4567/";
	String endpoint = "todos";
	
	String api = url + endpoint;
	
	@Test
	public void testCreateTodoWithValidParameters() {
			
		String title = "Wash dishes";
		String description = "Must not forget to wash dishes.";
		
		Response res = given().urlEncodingEnabled(true)
							.param("title", title)
							.param("description", description)
							.header("Accept", "application/json")
				            .contentType("application/x-www-form-urlencoded")
							.post(api);
		
		ResponseBody body = res.getBody();
		String bodyAsString = body.asString();
		System.out.println(bodyAsString);
		Assert.assertEquals(bodyAsString.contains("dishes"),  true);
		
	}


}
