package master.pro.houssine.pfe.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAfficheRapport implements Serializable {

    private int id, user_id,ageUsager;
    private String latitude, longitude,date_heure,typeUsager,sexeUsager,detailRoute,detailEnv,descriptionAccident,vehicule,
            vitesse,alcole,image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAgeUsager() {
        return ageUsager;
    }

    public void setAgeUsager(int ageUsager) {
        this.ageUsager = ageUsager;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(String date_heure) {
        this.date_heure = date_heure;
    }

    public String getTypeUsager() {
        return typeUsager;
    }

    public void setTypeUsager(String typeUsager) {
        this.typeUsager = typeUsager;
    }

    public String getSexeUsager() {
        return sexeUsager;
    }

    public void setSexeUsager(String sexeUsager) {
        this.sexeUsager = sexeUsager;
    }

    public String getDetailRoute() {
        return detailRoute;
    }

    public void setDetailRoute(String detailRoute) {
        this.detailRoute = detailRoute;
    }

    public String getDetailEnv() {
        return detailEnv;
    }

    public void setDetailEnv(String detailEnv) {
        this.detailEnv = detailEnv;
    }

    public String getDescriptionAccident() {
        return descriptionAccident;
    }

    public void setDescriptionAccident(String descriptionAccident) {
        this.descriptionAccident = descriptionAccident;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public String getVitesse() {
        return vitesse;
    }

    public void setVitesse(String vitesse) {
        this.vitesse = vitesse;
    }

    public String getAlcole() {
        return alcole;
    }

    public void setAlcole(String alcole) {
        this.alcole = alcole;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //    @SerializedName("error")
//    @Expose
//    private Integer error;
//
//
//    @SerializedName("succes")
//    @Expose
//    private Boolean succes;
//
//    @SerializedName("messagee")
//    @Expose
//    private String messagee;

    @SerializedName("rapport")
    @Expose
    private String rapport;

    public String getRapport() {
        return rapport;
    }

    public void setRapport() {
        this.rapport = rapport;
    }

//    public boolean getSucces() {
//        return succes;
//    }
//
//    public void setSucces(Boolean succes) {
//        this.succes = succes;
//    }
//
//
//
//    public Integer getError() {
//        return error;
//    }
//
//    public void setError(Integer error) {
//        this.error = error;
//    }
//
//
//    public String getMessagee() {
//        return messagee;
//    }
//
//    public void setMessagee(String messagee) {
//        this.messagee = messagee;
//    }


}
