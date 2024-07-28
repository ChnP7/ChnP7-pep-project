package Service;

import java.util.ArrayList;
import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

/**
 * Represents the service layer for operations involving messages.
 */
public class MessageService {
    private MessageDAO dao;

    public MessageService() {
        dao = MessageDAO.getInstance();
    }

    /**
     * Get all messages on the platform.
     * @return ArrayList<Message> of all messages on the platform
     */
    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    /**
     * Get all messages by a particular user
     * @param id id of user
     * @return ArrayList<Message> of all messages by a particular user. 
     */
    public List<Message> getAllMessagesByUser(int id) {
        return dao.getAllMessagesByUser(id);
    }

    /**
     * Get a particular message by its id
     * @param id id of the message
     * @return the Message object with id, or null if not found.
     */
    public Message getMessageById(int id) {
        return dao.getMessage(id);
    }

    /**
     * Attempts to create a new message. Operation will be unsuccessful if:
     * (1) Message text is blank
     * (2) Message length is more than 255 characters
     * (3) Message is posted by a non-existing user
     * (4) An internal error occurs
     * @param message Message Object containing message to add
     * @return The newly-created message if successful, null if unsuccessful
     */
    public Message createMessage(Message message) {

        /* Message text should not be blank */
        if (message.getMessage_text().equals("")) {
            return null;
        }

        /* Message text should not be > 255 characters */
        if (message.getMessage_text().length() > 255) {
            return null;
        }

        /* Message should be posted by an existing user */
        UserService userService = new UserService();
        List<Account> accounts = userService.getAllAccounts();
        List<Integer> ids = new ArrayList<>();
        for (Account account : accounts) {
            ids.add(account.getAccount_id());
        }
        if (!ids.contains(message.posted_by)) {
            return null;
        }

        return dao.addMessage(message);
        
    }

    /**
     * Deletes a message with given id.
     * @param id id of message to delete
     * @return the recently-deleted message
     */
    public Message deleteMessage(int id) {
        return dao.DeleteMessage(id);
    }


    /**
     * Updates an existing message with new text content.
     * Operation is unsuccessful if the following conditions are not met:
     * (1) Text is not empty
     * (2) Text is <= 255 characters
     * (3) Message with given id does not exist
     * The update may also fail if an internal database error occurs.
     * @param id id of message to update
     * @param text text to replace the original message text with
     * @return A Message Object representing the updated object if success, null if not.
     */
    public Message updateMessage(int id, String text) {

        /* Ensure message with id exists */
        if (this.getMessageById(id) == null) {
            return null;
        }

        /* Ensure message text is < 255 characters */
        if (text.length() > 255) {
            return null;
        }

        /* Ensure message text is not empty */
        if (text.equals("")) {
            return null;
        }

        return dao.updateMessage(id, text);
    }


}
