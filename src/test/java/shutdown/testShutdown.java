package shutdown;

import static io.restassured.RestAssured.get;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import io.restassured.response.Response;
import junit.framework.Assert;

public class testShutdown {

	String url = "http://localhost:4567/";
	String endpoint = "shutdown";

	String api = url + endpoint;

	public void restartApi() {
		Process ps;

		Response res = get(api);

		if (res != null) {
			if (res.getStatusCode() == 200) {
				System.out.println("Starting API server...");

				restartApplication();

				/*
				 * try { //ps = Runtime.getRuntime().exec(new
				 * String[]{"java","-jar","runTodoManagerRestAPI-1.5.5.jar"}); //ps.waitFor(); }
				 * catch (IOException e) { e.printStackTrace(); } catch (InterruptedException e)
				 * { e.printStackTrace(); }
				 */
			}
		}
	}

	public static void restartApplication() {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File("runTodoManagerRestAPI-1.5.5.jar");

		// is it a jar file?
		if (!currentJar.getName().endsWith(".jar"))
			return;

		// Build command: java -jar application.jar
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		System.out.println("Starting API server...");

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Test
	public void testShutdownApiServer() throws InterruptedException {
		
		String error = "";
		
		try {
			Response res = get(api);
			String body = res.getBody().asString();
			System.out.println(body);
		} catch (Exception e) {
			error = e.toString();
			System.out.println(error);
		}
		
		Assert.assertEquals(error.toString().contains("Connection refused"), true);
		
		// Below does not work... it restarts the app but causes the test to fail... although the test is good and passes without the stuff below
		restartApplication();
		Thread.sleep(3000);
		
		//System.out.println(error);
		//System.out.println(error.toString().contains("Connection refused"));
		


	}

}
