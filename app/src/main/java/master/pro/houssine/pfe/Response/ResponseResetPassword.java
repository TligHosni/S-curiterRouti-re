package master.pro.houssine.pfe.Response;

import master.pro.houssine.pfe.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ResponseResetPassword implements Serializable {


    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("verifier")
    @Expose
    private Integer verifier;

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

    public Integer getVerifier() {
        return verifier;
    }

    public void setVerifier(Integer verifier) {
        this.verifier = verifier;
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

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return this.user;
    }
}
