package todos;

import org.junit.Test;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.*;
import io.restassured.specification.*;
import junit.framework.Assert;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

public class testTodos {
	
	String url = "http://localhost:4567/";
	String endpoint = "todos";
	
	String api = url + endpoint;
	
	@Test
	public void testCreateTodoWithTitleAndDescription() {
			
		String title = "Wash dishes";
		String description = "Must not forget to wash dishes.";
		
		Response res = given().contentType("application/json; charset=utf-8").accept(ContentType.ANY).when().post(api+"?title="+title+"&description="+description);
		res.prettyPrint();
		
		ResponseBody body = res.getBody();
		String bodyAsString = body.asString();
		
		Assert.assertEquals(bodyAsString.contains("dishes"),  true);
		
	}


}
