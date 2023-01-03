package master.pro.houssine.pfe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    int id;

    public User(int id, String name, String email, String password, String role, String sexe, String phone, String image, String latitude, String longitude, String naissance, String lastlogin, String lastlogout, String deviceid, String created, String updated, String etat,String rendez_vous) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.sexe = sexe;
        this.phone = phone;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.naissance = naissance;
        this.lastlogin = lastlogin;
        this.lastlogout = lastlogout;
        this.deviceid = deviceid;
        this.created = created;
        this.updated = updated;
        this.etat = etat;
        this.rendez_vous = rendez_vous;
    }

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("password")
    @Expose
    String password;


    @SerializedName("role")
    @Expose
    String role;

    @SerializedName("sexe")
    @Expose
    String sexe;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("image")
    @Expose
    String image;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("naissance")
    @Expose
    String naissance;

    @SerializedName("lastlogin")
    @Expose
    String lastlogin;

    @SerializedName("lastlogout")
    @Expose
    String lastlogout;

    @SerializedName("deviceid")
    @Expose
    String deviceid;

    @SerializedName("created")
    @Expose
    String created;

    @SerializedName("updated")
    @Expose
    String updated;

    @SerializedName("etat")
    @Expose
    String etat;

    @SerializedName("rendez_vous")
    @Expose
    String rendez_vous;


    public void setRole(String role) {
        this.role = role;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNaissance(String naissance) {
        this.naissance = naissance;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public void setLastlogout(String lastlogout) {
        this.lastlogout = lastlogout;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }


    public String getRole() {
        return role;
    }

    public String getSexe() {
        return sexe;
    }

    public String getEtat() {
        return etat;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getNaissance() {
        return naissance;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public String getLastlogout() {
        return lastlogout;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public String getImage() {
        return image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRendez_vous() {
        return rendez_vous;
    }

    public void setRendez_vous(String rendez_vous) {
        this.rendez_vous = rendez_vous;
    }
}