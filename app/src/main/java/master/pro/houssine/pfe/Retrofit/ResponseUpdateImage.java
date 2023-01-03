package master.pro.houssine.pfe.Retrofit;

import master.pro.houssine.pfe.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUpdateImage {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private User user;


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


    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public User getUser() {
        return this.user;
    }
}
