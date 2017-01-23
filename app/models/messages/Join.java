package models.messages;

/**
 * Created by cp24 on 2017-01-21.
 */
public class Join {

    final String username;

    public String getUsername() {
        return username;
    }

    public Join(String username) {
        this.username = username;
    }
}