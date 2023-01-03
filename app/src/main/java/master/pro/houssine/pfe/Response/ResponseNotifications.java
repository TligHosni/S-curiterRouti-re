package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseNotifications implements Serializable {

    //    private int id, Vu, user_id;
//    private String name, phone, sujet, created_at ;
//
//    public int getId() {
//        return id;
//    }
//
//    public int getVu() {
//        return Vu;
//    }
//
//    public int getUser_id() {
//        return user_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public String getSujet() {
//        return sujet;
//    }
//
//    public String getCreated_at() {
//        return created_at;
//    }

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("notifications")
    @Expose
    private List<ResponseAfficheNotification> notifications;




    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public List<ResponseAfficheNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<ResponseAfficheNotification> notifications) {
        this.notifications = notifications;
    }

}
