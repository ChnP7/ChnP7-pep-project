package DAO;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import Model.Message;
import Util.ConnectionUtil;

/**
 * Represents the DAO Layer for a message on the Social Media Platform
 * Singleton
 */
public class MessageDAO {

    private static MessageDAO dao = null;

    /* Column name for message id */
    private String message_id = "message_id";

    /* Column name for posted_by (FOREIGN KEY) */
    private String posted_by = "posted_by";

    /* Column name for message_text */
    private String message_text = "message_text";

    /* Column name for time_posted_epoch */
    private String time_posted_epoch = "time_posted_epoch";

    private MessageDAO() {

    }

    public static MessageDAO getInstance() {
        if (dao == null) {
            dao = new MessageDAO();
        }
        return dao;
    }

    /**
     * Get all messages on the social media platform.
     * @return an ArrayList<Message> of all messages
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int id = (int)results.getLong(message_id);
                int postedBy = (int)results.getLong(posted_by);
                String msgText = results.getString(message_text);
                long timePosted = results.getLong(time_posted_epoch);

                Message msg = new Message(
                    id, postedBy, msgText, timePosted
                );
                messages.add(msg);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return messages;
    }

    /**
     * Get all messages that are sent by a particular user
     * 
     * @param accountId id of user/account
     * @return ArrayList<Message> of all messages by user with id
     */
    public List<Message> getAllMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE " + posted_by + " = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int id = (int)results.getLong(message_id);
                int postedBy = (int)results.getLong(posted_by);
                String msgText = results.getString(message_text);
                long timePosted = results.getLong(time_posted_epoch);

                Message msg = new Message(
                    id, postedBy, msgText, timePosted
                );
                messages.add(msg);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return messages;
    }

    /**
     * Gets a particular message by id
     * @param id id of the message
     * @return Message object representing message if found, null if not found.
     */
    public Message getMessage(int id) {
        Message message = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE " + message_id + " = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                int msgId = (int)results.getLong(message_id);
                int postedBy = (int)results.getLong(posted_by);
                String msgText = results.getString(message_text);
                long timePosted = results.getLong(time_posted_epoch);

                message = new Message(
                    msgId, postedBy, msgText, timePosted
                );
            }

        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return message;
    }

    /**
     * Attempts to add a new message to the internal database.
     * @param message Message object containing message to add
     * @return Message object representing newly-added message, or null if unsuccessful
     */
    public Message addMessage(Message message) {
        Message newMsg = null;
        System.out.println("Yes");
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Message (" + posted_by + ", " + message_text + ", " + time_posted_epoch + 
                ") VALUES (?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());
            statement.executeUpdate();

            ResultSet results = statement.getGeneratedKeys();
            if (results.next()) {
                int msgId = (int)results.getLong(message_id);

                newMsg = new Message(
                    msgId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch()
                );

            }
        }
        catch(SQLException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return newMsg;
    }

    /**
     * Attempts to delete a message from the Platform if it exists
     * @param id id of message to delete
     * @return the now-deleted message if found, null if not found.
     */
    public Message DeleteMessage(int id) {
        Message message = getMessage(id);
        if (message == null) {
            return null;
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM Message WHERE " + message_id + " = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return null;
            }

        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return message;
    }

    /**
     * Attempts to update the message_text of a message if found.
     * @param id id of message to update
     * @param text new message_text to update the message with
     * @return the updated message if found, null if not found.
     */
    public Message updateMessage(int id, String text) {
        Message message = getMessage(id);
        if (message == null) {
            return null;
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE Message SET " + message_text + " = ? " + 
                "WHERE " + message_id + " = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, text);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 0) {
                return null;
            }

            return new Message(
                message.getMessage_id(), message.getPosted_by(), text, message.getTime_posted_epoch()
            );

        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return message;
    }
    
}
