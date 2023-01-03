package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAfficheNotification implements Serializable {

    private int id, user_id,element,value;
    private String type, title_notification,content_notification,created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle_notification() {
        return title_notification;
    }

    public void setTitle_notification(String title_notification) {
        this.title_notification = title_notification;
    }

    public String getContent_notification() {
        return content_notification;
    }

    public void setContent_notification(String content_notification) {
        this.content_notification = content_notification;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    @SerializedName("cont")
    @Expose
    private String cont;

    public String getResult() {
        return cont;
    }

    public void setResult() {
        this.cont = cont;
    }
}
