package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponsePourcentageMort implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Nbre_dangereux")
    @Expose
    private int Nbre_dangereux;

    @SerializedName("Nbre_normale")
    @Expose
    private int Nbre_normale;

    public int getNbre_dangereux() {
        return Nbre_dangereux;
    }

    public void setNbre_dangereux(int nbre_dangereux) {
        Nbre_dangereux = nbre_dangereux;
    }

    public int getNbre_normale() {
        return Nbre_normale;
    }

    public void setNbre_normale(int nbre_normale) {
        Nbre_normale = nbre_normale;
    }

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


}
