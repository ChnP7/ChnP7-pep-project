package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("localhost:8080", this::exampleHandler);
        app.post("/register", this::registrationHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registrationHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String json = context.body();
        try {
            Account account = om.readValue(json, Account.class);
            UserService service = new UserService();
            Account result = service.addAccount(account);
            if (result == null) {
                System.out.println("HERE");
                context.status(400);
            }
            else {
                context.status(200);
                context.json(result);
            }
            

        }
        catch(JsonProcessingException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
    }


}