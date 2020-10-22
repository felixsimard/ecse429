package APITests;

import org.apache.http.HttpStatus;
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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;


public class TodosTests {
	
	String baseURI = "http://localhost:4567";
	int statusCodeSuccess = 200;
	int statusCodeSuccessCreated = 201;
	int statusCodeFailure = 400;
	
	/**
	 * The expect array error for missing fiel. Used in invalid creation todo test.
	 */
	ArrayList<String> fieldErrorArray = new ArrayList<String>(Arrays.asList("title : field is mandatory"));
	

	/**
	 * Will clear the data and set up baseURI before each test
	 * 
	 */
	@Before 
	public void setUp() {
		RestAssured.baseURI = baseURI;
		
		// must figure out how to delete all data here
		// 
		// ShutdownTests.restartApplication(); ??
	}

	/**
	 * Will test the endpoint GET /todos - get all todos created
	 */
	@Test
	public void testGetAllTodos() {
		given()
			.get("/todos")
			.then()
			.assertThat()
			.statusCode(equalTo(statusCodeSuccess));
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
		
		request.post("/todos")
			.then()
			.assertThat()
			.statusCode(equalTo(statusCodeSuccessCreated))
			.body("title", equalTo(title));
	}

	@Test 
	public void testCreateToDoAllInfo() {
		String title = "Must complete ECSE429 project";
		boolean doneStatus = false;
		String description =  "This project must be completed by monday";
		

		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", title);
		requestParams.put("description", description);
		requestParams.put("doneStatus", doneStatus);

		request.body(requestParams.toJSONString());

		request.post("/todos")
			.then()
			.assertThat()
			.statusCode(equalTo(statusCodeSuccessCreated))
			.body("title", equalTo(title),
					"description", equalTo(description), 
					"doneStatus", equalTo(String.valueOf(doneStatus)));
	}

	@Test 
	public void testCreateTodoNoTitle() {
		RequestSpecification request = RestAssured.given();
		
		request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        
		request.post("/todos")
				.then()
				.assertThat()
				.statusCode(equalTo(statusCodeFailure))
				.body("errorMessages", equalTo(fieldErrorArray));
	}
	
	/**
	 * Will test if a todo can be found by it's ID in the URL
	 */
	@Test
	public void testGetTodoById() {
		String todoTitle = "Dummy TODO";
		
		try {
			int todoID = createTodo(todoTitle);
			get("/todos/" + todoID)
				.then()
				.assertThat()
				.statusCode(equalTo(statusCodeSuccess))
				.body("todos[0].title", equalTo(todoTitle));
		}catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * Will test if a todo can be found by it's title (query parameter)
	 */
	@Test
	public void testGetTodoByTitle() {
		String todoTitle = "Dummy TODO";
		
		try {
			createTodo(todoTitle);
			given().queryParam("title", todoTitle)
				.get("/todos")
				.then()
				.assertThat()
				.statusCode(equalTo(statusCodeSuccess))
				.body("todos[0].title", equalTo(todoTitle));
		}catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * This function creates a Todo.
	 * 
	 * @param titleOfTodo - the tile to be used to create the parameter
	 * 
	 * @return int corresponding to id of todo
	 * 
	 * @throws Exception - if todo could not be created for some reason
	 */
	private int createTodo(String titleOfTodo) throws Exception {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", titleOfTodo);
		
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/todos");
		
		if (response.statusCode() != statusCodeSuccessCreated) {
			throw new Exception("Could not create dummy TODO");
		}
		
		// want to return the 'Object' value of JSON field "id" as int
		int todoID = Integer.parseInt((String) response.jsonPath().get("id"));
		return todoID;
	}

	@Test
	public void testAddTodoToProject() {
		
		int projectId = createProject();

	}
	
	private int createProject() {
		return 0;
		
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
