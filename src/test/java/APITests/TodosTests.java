package APITests;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.*;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class TodosTests {
	
	@Before 
	public void setUp() {
		RestAssured.baseURI = "http://localhost:4567";
	}
	
	@Test
	public void getAllTodos() {
		
	}
	
	@Test
	public void getTodoById() {
		
	}
	
	@Test
	public void testCreateTodoValidInfo() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "play guitar");
		JSONObject requestParams = new JSONObject();
		requestParams.put("title", "play guitar");
		
		Response response = given()
			.body(requestParams.toJSONString())
			.when()
			.post("/todos").then().statusCode(200).extract().response();
		
		String titleResponse = response.jsonPath().getString("title");
		assertEquals(titleResponse,"play guitar");
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
