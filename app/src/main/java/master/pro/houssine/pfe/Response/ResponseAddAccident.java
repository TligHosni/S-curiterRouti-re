package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAddAccident implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private ResponseAfficheAccident result;




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

    public ResponseAfficheAccident getResult() {
        return result;
    }

    public void setResult(ResponseAfficheAccident result) { this.result = result; }

}
