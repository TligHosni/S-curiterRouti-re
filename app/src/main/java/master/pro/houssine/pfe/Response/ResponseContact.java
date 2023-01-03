package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseContact implements Serializable {


    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("contacts")
    @Expose
    private List<ResponseAfficheContact> contacts;




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

    public List<ResponseAfficheContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ResponseAfficheContact> contacts) {
        this.contacts = contacts;
    }
}
