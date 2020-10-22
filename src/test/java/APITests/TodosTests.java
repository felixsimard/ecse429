package APITests;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class TodosTests {
	
	int statusCodeSuccess = 200;
	int statusCodeSuccessCreated = 201;

	/**
	 * Will clear the data and set up baseURI before each test
	 * 
	 */
	@Before 
	public void setUp() {
		RestAssured.baseURI = "http://localhost:4567";
		
		// must figure out how to delete all data here
		// 
		// ShutdownTests.restartApplication();
	}

	/**
	 * Will test the endpoint GET /todos - get all todos created
	 */
	@Test
	public void getAllTodos() {
		
		RequestSpecification request = RestAssured.given();
		
		Response response = request.get("/todos");
		
		// assert the response is succesfull 
		int statusCode = response.getStatusCode();
		assertEquals(statusCodeSuccess, statusCode);
	}

	/**
	 * Will test if a todo can be found by it's ID in the URL
	 */
	@Test
	public void getTodoById() {
		try {
			// call helper function to make todo and return id
			int todoID = TodosHelper.createTodo();
			RequestSpecification request = RestAssured.given();
			Response response = request.get("/todos/" + todoID);
			
			// assert the response is successful 
			int statusCode = response.getStatusCode();
			assertEquals(statusCodeSuccess, statusCode);
			
			//not sure why this is NULL but in testCreateTodoValidInfo it isn't
			String responseBodyTitle = response.jsonPath().get("title");
			
			System.out.println("K: " + response.body().jsonPath().get("title"));
			System.out.println("WHy: " + responseBodyTitle);
			
			assertEquals(TodosHelper.todoTitle, responseBodyTitle);
			int responseID = Integer.parseInt((String) response.jsonPath().get("id"));
			assertEquals(responseID, todoID);
			
		}catch (Exception e){
			System.out.println(e);
			Assert.fail();
		}
	}
	

	/**
	 * Will attempt to create a valid todo with a title in body
	 */
	@Test
	public void testCreateTodoValidInfo() {

		String title = "Must complete ECSE429 project";

		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", title);

		request.body(requestParams.toJSONString());

		Response response = request.post("/todos");

		// assertions 
		int statusCode = response.getStatusCode();
		assertEquals(statusCodeSuccessCreated, statusCode);
		String responseBodyTitle = response.jsonPath().get("title");
		assertEquals(responseBodyTitle, title);
	}

	@Test 
	public void testCreateToDoAllInfo() {

	}

	@Test 
	public void testCreateTodoNoTitle() {

	}

	@Test
	public void testAddTodoToProject() {

	}

	@Test
	public void testAddTodoToNonExistingProject() {

	}

	@Test
	public void testRemoveTodoFromProject() {

	}

	@Test
	public void testGetAllTodoProjects() {

	}

	@Test
	public void testAddTodoToCategory() {

	}

	@Test
	public void testAddTodoToNonExistingCategory() {

	}

	@Test
	public void testRemoveTodoFromCategory() {

	}

	@Test
	public void testGetAllTodoCategories() {

	}


}
