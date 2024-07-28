package Service;

import DAO.MessageDAO;

/**
 * Represents the service layer for operations involving messages.
 */
public class MessageService {
    private MessageDAO dao;

    public MessageService() {
        dao = MessageDAO.getInstance();
    }

    /* TODO: Methods utilizing MessageDAO and validation checks */

}
