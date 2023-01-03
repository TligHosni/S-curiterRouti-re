package master.pro.houssine.pfe.Response;

import android.app.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseSeenNotif implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("notif")
    @Expose
    private Notification notif;


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

    public void setUser(Notification notif) {
        this.notif = notif;
    }
    public Notification getUser() {
        return this.notif;
    }
}
