package master.pro.houssine.pfe.Activities;

public class Messages {


    private int discussion_id;
    private String message;
    private String created_at;

    public Messages(int discussion_id, String message, String created_at) {
        this.discussion_id = discussion_id;
        this.message = message;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getDiscussion_id() {
        return discussion_id;
    }

    public String getMessage() {
        return message;
    }
}
