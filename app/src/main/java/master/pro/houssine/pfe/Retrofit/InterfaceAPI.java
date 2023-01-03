package master.pro.houssine.pfe.Retrofit;

import master.pro.houssine.pfe.Response.ResponseAddAccident;
import master.pro.houssine.pfe.Response.ResponseAddMessage;
import master.pro.houssine.pfe.Response.ResponseAddRapport;
import master.pro.houssine.pfe.Response.ResponseAfficheAccident;
import master.pro.houssine.pfe.Response.ResponseAfficheLatLongAcc;
import master.pro.houssine.pfe.Response.ResponseContact;
import master.pro.houssine.pfe.Response.ResponseDeleteToken;
import master.pro.houssine.pfe.Response.ResponseListAccidentParJour;
import master.pro.houssine.pfe.Response.ResponseLoaclisation;
import master.pro.houssine.pfe.Response.ResponseLogin;
import master.pro.houssine.pfe.Response.ResponseMessage;
import master.pro.houssine.pfe.Response.ResponseNbrNotif;
import master.pro.houssine.pfe.Response.ResponseNotifications;
import master.pro.houssine.pfe.Response.ResponseOccurAccident;
import master.pro.houssine.pfe.Response.ResponsePourcentageMort;
import master.pro.houssine.pfe.Response.ResponseRapports;
import master.pro.houssine.pfe.Response.ResponseRegister;
import master.pro.houssine.pfe.Response.ResponseRendezVous;
import master.pro.houssine.pfe.Response.ResponseResetPassword;
import master.pro.houssine.pfe.Response.ResponseSeenNotif;
import master.pro.houssine.pfe.Response.ResponseUpdateAccident;
import master.pro.houssine.pfe.Response.ResponseUpdateProfile;
import master.pro.houssine.pfe.Response.ResponseVerifyPhone;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceAPI {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> login(@Field("phone") String phone,
                              @Field("password") String password,
                              @Field("token") String token
    );

    @FormUrlEncoded
    @POST("updateLocalisation")
    Call<ResponseLoaclisation> updateLocalisation(
            @Field("id") int id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("add")
    Call<ResponseRegister> add(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("sexe") String sexe

    );

    @FormUrlEncoded
    @POST("verifyPhone")
    Call<ResponseVerifyPhone> verifyPhone(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("updateConfirmeAccident")
    Call<ResponseUpdateAccident> updateConfirmeAccident(
            @Field("id") int id);

    @FormUrlEncoded
    @POST("ShowAccident")
    Call<ResponseAfficheLatLongAcc> ShowAccident(
            @Field("id") int id);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<ResponseResetPassword> updatePassword(
            @Field("password") String password,
            @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("contactAdmin")
    Call<ResponseContact> contactAdmin(
            @Field("user_id") int user_id,
            @Field("name") String user_name,
            @Field("phone") String user_phone,
            @Field("sujet") String subject,
            @Field("message") String message
    );


    @GET("listDiscussions")
    Call<ResponseContact> listDiscussions(@Query("user_id") int user_id);

    @GET("listMessages")
    Call<ResponseMessage> listMessages(@Query("discussion_id") int discussion_id);

    @GET("listNotifications")
    Call<ResponseNotifications> listNotifications(@Query("user_id") int user_id);

    @GET("listRapports")
    Call<ResponseRapports> listRapports(@Query("user_id") int user_id);

    @GET("NbrNotif")
    Call<ResponseNbrNotif> NbrNotif(@Query("user_id") int user_id);

    @GET("getRendezVous")
    Call<ResponseRendezVous> getRendezVous(@Query("user_id") int user_id);

    @GET("pourcentageMorts")
    Call<ResponsePourcentageMort> pourcentageMorts();

    @GET("listAccidentStat")
    Call<ResponseListAccidentParJour> listAccidentStat();


    @GET("NbrOccurAccident")
    Call<ResponseOccurAccident> NbrOccurAccident(@Query("accident_id") int accident_id);

    @GET("listAccident")
    Call<ResponseAfficheAccident> listAccident();

    @GET("listAccident")
    Call<ResponseAfficheAccident> listAccidents(@Query("date") String date);

    @FormUrlEncoded
    @POST("addMessage")
    Call<ResponseAddMessage> addMessage(
            @Field("discussion_id") int discussion_id,
            @Field("message") String message,
            @Field("user_id") int user_id

    );

    @FormUrlEncoded
    @POST("deleteToken")
    Call<ResponseDeleteToken> deleteToken(
            @Field("token") String token,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("updateProfile")
    Call<ResponseUpdateProfile> updateProfile(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("naissance") String naissance,
            @Field("id") int id
    );
 @FormUrlEncoded
    @POST("Rendez_vous")
    Call<ResponseUpdateProfile> Rendez_vous(
            @Field("rendez_vous") String rendez_vous,
            @Field("id") int id
    );

    @Multipart
    @POST("updateImage")
    Call<ResponseUpdateImage> updateImage(
            @Part MultipartBody.Part image,
            @Part("user_id") int id);

    @Multipart
    @POST("addRapport")
    Call<ResponseAddRapport> addRapport(
            @Part("latitude") String latitude,
            @Part("longitude") String longitude,
            @Part("typeUsager") String typeUsager,
            @Part("ageUsager") int ageUsager,
            @Part("sexeUsager") String sexeUsager,
            @Part("detailRoute") String detailRoute,
            @Part("detailEnv") String detailEnv,
            @Part("descriptionAccident") String descriptionAccident,
            @Part("vehicule") String vehicule,
            @Part("vitesse") String vitesse,
            @Part("alcole") String alcole,
            @Part("user_id") int user_id,
            @Part MultipartBody.Part image,
            @Part("accident_id") int accident_id,
            @Part("nbr_mort") int nbr_mort

    );


    @FormUrlEncoded
    @POST("SeenNotif")
    Call<ResponseSeenNotif> SeenNotif(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("addAccident")
    Call<ResponseAddAccident> addAccident(
            @Field("serialNumber") String serialNumber,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("commentaire") String commentaire,
            @Field("dgr_dange") String dgr_dange,
            @Field("nb_blessure") int nb_blessure,
            @Field("nb_morts") int nb_morts
    );

}
