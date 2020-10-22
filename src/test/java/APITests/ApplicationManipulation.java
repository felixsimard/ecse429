package APITests;

import java.io.IOException;

import static io.restassured.RestAssured.get;

public class ApplicationManipulation {

    public static void startApplication() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopApplication() {
        get("http://localhost:4567/shutdown");
    }
}
