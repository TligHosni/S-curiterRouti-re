package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseRendezVous implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("rendezvous")
    @Expose
    private String rendezvous;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(String rendezvous) {
        this.rendezvous = rendezvous;
    }
}
