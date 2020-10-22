package APITests;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TodosHelper {
	/**
	 * Static string dummy todos being created.
	 */
	public static String todoTitle = "Dummy TODO";
	
	/**
	 * 
	 * This function creates a Todo.
	 * 
	 * This function is used in the get Todo by ID test.
	 * 
	 * @throws Exception
	 */
	public static int createTodo() throws Exception {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", todoTitle);
		
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/todos");
		
		if (response.statusCode() != 201) {
			throw new Exception("Could not create dummy TODO");
		}
		
		// want to return the 'Object' value of JSON fiel "id" as int
		int todoID = Integer.parseInt((String) response.jsonPath().get("id"));
		return todoID;
	}

}
