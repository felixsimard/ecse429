package APITests;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;


public class TodosTests {
	
	private static final int STATUS_CODE_SUCCESS = 200;
	private static final int STATUS_CODE_CREATED = 201;
	private static final int STATUS_CODE_FAILURE = 400;
	private static final int STATUS_CODE_NOT_FOUND = 404;
	
	/**
	 * The expect array error for missing field. Used in invalid creation todo test.
	 */
	ArrayList<String> fieldErrorArray = new ArrayList<String>(Arrays.asList("title : field is mandatory"));
	
	/**
	 * The expected array error for adding non existing category to todo.
	 */
	ArrayList<String> categoryError = new ArrayList<String>(Arrays.asList("Could not find thing matching value for id"));
	

	/**
	 * Will clear the data and set up baseURI before each test
	 * 
	 */
	@Before 
	public void setUp() {
		RestAssured.baseURI = "http://localhost:4567";
		ApplicationManipulation.startApplication();
	}
	
//	@After
//	public void tearDown() {
//		ApplicationManipulation.stopApplication();
//	}

	/**
	 * Will test the endpoint GET /todos - get all todos created
	 * Endpoint: GET /todos
	 */
	@Test
	public void testGetAllTodos() {
		given()
			.get("/todos")
			.then()
			.assertThat()
			.statusCode(equalTo(STATUS_CODE_SUCCESS));
	}
	
	/**
	 * Test: create a valid todo with only title in body.
	 * Endpoint: POST /todos
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
			.statusCode(equalTo(STATUS_CODE_CREATED))
			.body("title", equalTo(title));
	}

	/**
	 * Test: create a todo with a title, description and doneStatus.
	 * Endpoint: POST /todos
	 */
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
			.statusCode(equalTo(STATUS_CODE_CREATED))
			.body("title", equalTo(title),
					"description", equalTo(description), 
					"doneStatus", equalTo(String.valueOf(doneStatus)));
	}

	/**
	 * Test: create a todo with no title. Assert that it fails.
	 * Endpoint: POST /todos
	 */
	@Test 
	public void testCreateTodoNoTitle() {
		RequestSpecification request = RestAssured.given();
		
		request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        
		request.post("/todos")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_FAILURE))
				.body("errorMessages", equalTo(fieldErrorArray));
	}
	
	/**
	 * Test: todo can be found by it's ID in the URL
	 * Endpoint: GET /todos/:id
	 */
	@Test
	public void testGetTodoById() {
		String todoTitle = "Dummy TODO";
		
		try {
			int todoID = createTodo(todoTitle);
			get("/todos/" + todoID)
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_SUCCESS))
				.body("todos[0].title", equalTo(todoTitle));
		}catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * Test: todo can be found by it's title (query parameter)
	 * Endpoint: GET /todos?title=$todo_title$
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
				.statusCode(equalTo(STATUS_CODE_SUCCESS))
				.body("todos[0].title", equalTo(todoTitle));
		}catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}

	// THESE TESTS ARE FOR ENDPOINT TASKOF WHICH DOES NOT WORK AS DOUCMENTED
//	@Test
//	public void testAddTodoToProject() {
//		String projectName = "Dummy project";
//		String todoName = "Dummy todo";
//		
//		try {
//			int projectId = createProject(projectName);
//			int todoId = createTodo(todoName);
//			
//			// make sure that the 
//			RequestSpecification requestPost = RestAssured.given();
//			JSONObject requestParams = new JSONObject();
//			requestParams.put("id", projectId);
	
//			requestPost.body(requestParams.toJSONString());
//			
//			requestPost
//				.post("/todos/" + todoId + "/taskof")
//				.then()
//				.assertThat()
//				.statusCode(equalTo(STATUS_CODE_SUCCESS));
//			
//			
//			RequestSpecification requestGet = RestAssured.given();
//			
//			requestGet
//				.get("/todos/" + todoId + "/taskof")
//				.then()
//				.assertThat()
//				.body("", "")
//				
//		} catch (Exception e) {
//			System.out.println(e);
//			Assert.fail();
//		}
//	}
//
//	@Test
//	public void testAddTodoToNonExistingProject() {
//// POST /todos/:id/tasksof
//	}
//
//	@Test
//	public void testRemoveTodoFromProject() {
////DELETE /todos/:id/tasksof/:id
//	}
//
//	@Test
//	public void testGetAllTodoProjects() {
//// GET /todos/:id/tasksof
//	}
	
	/**
	 * Test: adding a category to a todo 
	 * EndpointL POST /todos/:id/categories
	 */
	@Test
	public void testAddCategoryToTodo() {
		String categoryName = "Dummy category";
		String todoName = "Dummy todo";
		
		try {
			int categoryId = createCategory(categoryName);
			int todoId = createTodo(todoName);
			
			// add the category to the todo
			RequestSpecification requestPost = RestAssured.given();
			JSONObject requestParams = new JSONObject();
			requestParams.put("id", String.valueOf(categoryId));
			
			requestPost.body(requestParams.toJSONString());
			
			requestPost
				.post("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_CREATED));
			
			// Assert that it is present
			get("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_SUCCESS))
				.body("categories[0].id", equalTo(String.valueOf(categoryId)),
						"categories[0].title", equalTo(String.valueOf(categoryName)));
				
		} catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * Test: that adding a non existing category to a todo fails
	 * EndpointL POST /todos/:id/categories
	 */
	@Test
	public void testAddNonExistingCategoryToTodo() {
		String todoName = "Dummy todo";
		String randomHugeId = "99999999991";
		
		try {
			int todoId = createTodo(todoName);
			
			// add the non existing category to todo - make sure it fails
			RequestSpecification requestPost = RestAssured.given();
			JSONObject requestParams = new JSONObject();
			requestParams.put("id", randomHugeId);
			
			requestPost.body(requestParams.toJSONString());
			
			requestPost
				.post("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_NOT_FOUND))
				.body("errorMessages", equalTo(categoryError));
				
		} catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * Test: if a category can be removed from a todo
	 * Endpoint: DELETE /todos/:id/categories/:id
	 */
	@Test
	public void testRemoveCategoryFromTodo() {
		String categoryName = "Dummy category";
		String todoName = "Dummy todo";
		
		try {
			int categoryId = createCategory(categoryName);
			int todoId = createTodo(todoName);
			
			// Add the category to todo
			RequestSpecification requestPost = RestAssured.given();
			JSONObject requestParams = new JSONObject();
			requestParams.put("id", String.valueOf(categoryId));
			requestPost.body(requestParams.toJSONString());		
			requestPost
				.post("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_CREATED));
			
			// Delete the connection
			delete("/todos/" + todoId + "/categories/" + categoryId)
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_SUCCESS));
			
			// Make sure it was successfully deleted
			Response response = get("/todos/" + todoId + "/categories");
			
			assertEquals(response.statusCode(),STATUS_CODE_SUCCESS);
			assertNotEquals(response.jsonPath().get("categories[0].id"), String.valueOf(categoryId));
			assertNotEquals(response.jsonPath().get("categories[0].title"), categoryName);
				
		} catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * Test: if all categories can be found from a todo
	 * Endpoint: GET /todos/:id/categories
	 */
	@Test
	public void testGetAllTodoCategories() {
		String categoryName = "Dummy category";
		String categoryName2 = "Dummy category2";
		String todoName = "Dummy todo";
		
		try {
			int categoryId = createCategory(categoryName);
			int categoryId2 = createCategory(categoryName2);
			int todoId = createTodo(todoName);
			
			// add category 1 to todo
			RequestSpecification requestPost = RestAssured.given();
			JSONObject requestParams = new JSONObject();
			requestParams.put("id", String.valueOf(categoryId));
			requestPost.body(requestParams.toJSONString());
			requestPost
				.post("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_CREATED));
			
			// add category 2 to todo
			RequestSpecification requestPost2 = RestAssured.given();
			JSONObject requestParams2 = new JSONObject();
			requestParams2.put("id", String.valueOf(categoryId2));
			requestPost2.body(requestParams2.toJSONString());
			requestPost2
				.post("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_CREATED));
			
			// Make sure we can get all categories
			get("/todos/" + todoId + "/categories")
				.then()
				.assertThat()
				.statusCode(equalTo(STATUS_CODE_SUCCESS))
				.body("categories[0].id", equalTo(String.valueOf(categoryId)),
						"categories[0].title", equalTo(String.valueOf(categoryName)),
						"categories[1].id", equalTo(String.valueOf(categoryId2)),
						"categories[1].title", equalTo(String.valueOf(categoryName2)));
				
		} catch (Exception e) {
			System.out.println(e);
			Assert.fail();
		}
	}
	
	/**
	 * This function creates a Todo.
	 * 
	 * @param titleOfTodo - the tile to be used to create the todo
	 * 
	 * @return int corresponding to id of todo
	 * 
	 * @throws Exception - if todo could not be created throws an exception -
	 */
	private int createTodo(String titleOfTodo) throws Exception {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", titleOfTodo);
		
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/todos");
		
		if (response.statusCode() != STATUS_CODE_CREATED) {
			throw new Exception("Could not create dummy TODO");
		}
		
		// want to return the 'Object' value of JSON field "id" as int
		int todoID = Integer.parseInt((String) response.jsonPath().get("id"));
		return todoID;
	}
	
	/**
	 * This function will create a project.
	 * 
	 * @param projectName
	 * 
	 * @return int corresponding to id of the project
	 * 
	 * @throws Exception - if project could not be created throws an exception -
	 */
	private int createProject(String projectName) throws Exception {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", projectName);
		
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/projects");
		
		if (response.statusCode() != STATUS_CODE_CREATED) {
			throw new Exception("Could not create dummy project");
		}
		
		int projectID = Integer.parseInt((String) response.jsonPath().get("id"));
		return projectID;
	}

	/**
	 * This function will create a category 
	 * 
	 * @param categoryName
	 * 
	 * @return int corresponding to id of the category
	 * 
	 * @throws Exception - if category could not be created throws an exception -
	 */
	private int createCategory(String categoryName) throws Exception {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", categoryName);
		
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/categories");
		
		if (response.statusCode() != STATUS_CODE_CREATED) {
			throw new Exception("Could not create dummy category");
		}
		
		int categoryID = Integer.parseInt((String) response.jsonPath().get("id"));
		return categoryID;
	}

}
