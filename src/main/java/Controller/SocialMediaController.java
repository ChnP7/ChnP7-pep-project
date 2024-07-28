package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
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
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messageCreateHandler);
        app.get("/messages", this::allMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::allMessagesyUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handles registration requests
     * @param context context
     */
    private void registrationHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String json = context.body();
        try {
            Account account = om.readValue(json, Account.class);
            UserService service = new UserService();
            Account result = service.addAccount(account);
            if (result == null) {
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

    /**
     * Handles login requests
     * @param context context
     */
    private void loginHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String json = context.body();
        try {
            Account account = om.readValue(json, Account.class);
            UserService service = new UserService();
            Account result = service.getAccount(account);
            if (result == null) {
                context.status(401);
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

    /**
     * Handles Requests to create a new message
     * @param context context
     */
    private void messageCreateHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String json = context.body();
        try {
            Message msg = om.readValue(json, Message.class);
            MessageService service = new MessageService();
            Message result = service.createMessage(msg);
            if (result == null) {
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

    /**
     * Handler to get all messages
     * @param context context
     */
    private void allMessagesHandler(Context context) {
        MessageService service = new MessageService();
        context.status(200);
        context.json(service.getAllMessages());
    }

    /**
     * Handles requests to obtain a sepcific message by id. Status code 200 by default
     * @param context context
     */
    private void getMessageHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        MessageService service = new MessageService();
        Message msg = service.getMessageById(id);
        if (msg != null) {
            context.json(msg);
        }
        context.status(200);
    }

    /**
     * Handles requests to delete a certain message by id. Default status 200
     * @param context context
     */
    private void deleteMessageHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        MessageService service = new MessageService();
        Message msg = service.deleteMessage(id);
        if (msg != null) {
            context.json(msg);
        }
        context.status(200);
    }

    /**
     * Handles requests to update a message with specified id. Status 400 upon error, 200 otherwise
     * @param context context
     */
    private void updateMessageHandler(Context context) {

        int id = Integer.parseInt(context.pathParam("message_id"));

        ObjectMapper om = new ObjectMapper();
        String json = context.body();
        try {
            Message message = om.readValue(json, Message.class);
            MessageService service = new MessageService();
            Message result = service.updateMessage(id, message.getMessage_text());
            if (result == null) {
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

    /**
     * Handles requests to get all messages by a user with account_id. Default status 200
     * @param context context
     */
    private void allMessagesyUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        MessageService service = new MessageService();
        context.json(service.getAllMessagesByUser(accountId));
        context.status(200);
    }



}