package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseDeleteToken implements Serializable {

    @SerializedName("result")
    @Expose
    private ResponseDeleteToken result;

    public ResponseDeleteToken getResult() {
        return result;
    }

    public void setResult(ResponseDeleteToken result) {
        this.result = result;
    }
}
