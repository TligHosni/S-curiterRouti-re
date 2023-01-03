package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAfficheMessage implements Serializable {


    private int id, discussion_id, user_id,admin;
    private String message, created_at;

    public int getAdmin() { return admin; }

    public void setAdmin(int admin) { this.admin = admin; }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getDiscussion_id() {
        return discussion_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDiscussion_id(int discussion_id) {
        this.discussion_id = discussion_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
