package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAddMessage implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("succes")
    @Expose
    private Boolean succes;

    @SerializedName("messagee")
    @Expose
    private String messagee;

    @SerializedName("result")
    @Expose
    private ResponseAfficheMessage result;




    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }


    public String getMessagee() {
        return messagee;
    }

    public void setMessagee(String messagee) {
        this.messagee = messagee;
    }


    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public ResponseAfficheMessage getResult() {
        return result;
    }

    public void setContacts(ResponseAfficheMessage result) { this.result = result; }

}
