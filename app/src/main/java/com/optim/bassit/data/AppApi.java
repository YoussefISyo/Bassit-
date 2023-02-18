package com.optim.bassit.data;

import com.optim.bassit.models.Activite;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Apidata;
import com.optim.bassit.models.Apidatahome;
import com.optim.bassit.models.Avis;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Chat;
import com.optim.bassit.models.Commission;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.DemandService;
import com.optim.bassit.models.Dette;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.Journal;
import com.optim.bassit.models.Montant;
import com.optim.bassit.models.Photo;
import com.optim.bassit.models.Reward;
import com.optim.bassit.models.Service;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.models.Stats;
import com.optim.bassit.models.Tache;
import com.optim.bassit.models.Word;
import com.optim.bassit.models.notifi;
import com.optim.bassit.models.offerchat;
import com.optim.bassit.models.repreq;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppApi {


    @POST("register")
    Call<ApiResponse> registerCustomer(@Body Customer customer);

    @POST("updateinformation")
    Call<ApiResponse> updateCustomer(@Body Customer customer);

    @POST("updateStatus")
    Call<ApiResponse> updateStatus(@Body Customer customer);

    @POST("login")
    Call<ApiResponse> loginCustomer(@Body Customer customer);


    @GET("categories")
    Call<List<Categorie>> getCategories();

    @GET("ads")
    Call<List<Ads>> getAds();

    @GET("adsb")
    Call<List<Ads>> getofferAds();


    @GET("profile")
    Call<Customer> customerProfile(@Query("user_id") Integer user_id);

    @POST("chat")
   Call<List<Chat>> getChat(@Query("service_id") Integer service_id,@Query("typechat") String typec,@Query("offer_id") String offer_id);
   // @POST("chat")
 //   Call<List<Chat>> getChat(@Query("service_id") Integer service_id);

    @POST("chat")
    Call<List<Chat>> getChatByBon(@Query("bon_id") Integer bon_id);


    @Multipart
    @POST("chat/send")
    Call<ApiResponse> sendChat(@Query("service_id") Integer service_id,@Query("typechat") String typec,@Query("offer_id") String offer_id, @Query("type") Integer type, @Query("message") String message,
                               @Query("bon_id") Integer bon_id, @Part MultipartBody.Part image);


    @GET("search")
    Call<List<HomeFeed>> getSearchHome(@Query("souscategorie") Integer kind, @Query("categorie") Integer cats, @Query("word") String word, @Query("page") Integer page,
                                       @Query("prixMin") Integer min_price, @Query("prixMax") Integer max_price,
                                       @Query("official_account") Integer official_account, @Query("vues") String vues,
                                       @Query("operator") String operator, @Query("rating") Integer rating);

    @GET("search")
    Call<List<HomeFeed>> getSearchHomeCats(@Query("souscategorie") Integer kind, @Query("categorie") Integer cats, @Query("word") String word, @Query("page") Integer page);

    @GET("search")
    Call<List<HomeFeed>> getSearchHomePosition(@Query("souscategorie") Integer kind, @Query("categorie") Integer cats, @Query("word") String word, @Query("page") Integer page,
                                               @Query("prixMin") Integer min_price, @Query("prixMax") Integer max_price,
                                               @Query("official_account") Integer official_account, @Query("vues") String vues,
                                               @Query("operator") String operator, @Query("rating") Integer rating,
                                               @Query("longitude") Double longitude, @Query("latitude") Double latitude);

    @GET("search")
    Call<List<HomeFeed>> getSearchHomeWithoutRating(@Query("souscategorie") Integer kind, @Query("categorie") Integer cats, @Query("word") String word, @Query("page") Integer page,
                                       @Query("prixMin") Integer min_price, @Query("prixMax") Integer max_price,
                                       @Query("official_account") Integer official_account, @Query("vues") String vues,
                                       @Query("operator") String operator);

    @GET("search")
    Call<List<HomeFeed>> getSearchHomePositionWithoutRating(@Query("souscategorie") Integer kind, @Query("categorie") Integer cats, @Query("word") String word, @Query("page") Integer page,
                                                            @Query("prixMin") Integer min_price, @Query("prixMax") Integer max_price,
                                                            @Query("official_account") Integer official_account, @Query("vues") String vues,
                                                            @Query("operator") String operator, @Query("longitude") Double longitude, @Query("latitude") Double latitude);

    @POST("chat/same")
    Call<List<HomeFeed>> getSame(@Query("bon_id") Integer bon_id);


    @GET("cats")
    Call<List<Categorie>> getCats();

    @GET("souscategories")
    Call<List<SousCategorie>> getSousCategories();


    @POST("facebook")
    Call<ApiResponse> facebookLogin(@Body Customer customer);

    @POST("google")
    Call<ApiResponse> googleLogin(@Body Customer customer);

    @POST("dopro")
    Call<ApiResponse> doPro();

    @POST("doafrili")
    Call<ApiResponse> doAfrili();

    @POST("undopro")
    Call<ApiResponse> undoPro();

    @POST("doclient")
    Call<ApiResponse> doClient();

    @POST("service/pause")
    Call<ApiResponse> pauseService(@Query("service_id") Integer service_id);
    @POST("service/delete")
    Call<ApiResponse> deleteService(@Query("service_id") Integer service_id);
    @POST("offer/delete")
    Call<ApiResponse> deleteoffer(@Query("id") Integer id);
    @POST("repreq/delete")
    Call<ApiResponse> deleterepreq(@Query("id") Integer id);

    @POST("Voffers")
    Call<ApiResponse> Voffers(@Query("id") Integer id);

    @POST("Vreq")
    Call<ApiResponse> Vreq(@Query("id") Integer id);

 @POST("offer/st")
 Call<ApiResponse> changestoffer(@Query("id") Integer id,@Query("st") String st);

    @POST("service/log")
    Call<ApiResponse> touchService(@Query("service_id") Integer service_id);

    @GET("service/log")
    Call<ApiResponse> getViewsService(@Query("service_id") Integer service_id);

    @POST("resetpwd")
    Call<ApiResponse> resetPWD(@Query("email") String email);

    @GET("version")
    Call<String> getVersion();

    @GET("dict")
    Call<List<Word>> getDict();

    @POST("pause")
    Call<ApiResponse> pauseUser();

    @POST("chat/close")
    Call<ApiResponse> doCloturer(@Query("bon_id") int bon_id,@Query("typechat") String typec,@Query("offer_id") String offer_id, @Query("montant") double montant, @Query("charges") double charges, @Query("payed") double payed,
                                 @Query("avis") double avis, @Query("comment") String comment);
    @GET("offerchat")
    Call<List<offerchat>> getofferchat(@Query("offer_id") String offer_id);

    @POST("chat/fix")
    Call<ApiResponse> doFixDette(@Query("bon_id") int bon_id, @Query("montant") double montant, @Query("payed") double payed);

    @POST("chat/rate")
    Call<ApiResponse> doRate(@Query("bon_id") int bon_id,@Query("typechat") String typec,@Query("offer_id") String offer_id,
                             @Query("client_avis") double avis, @Query("client_comment") String comment);

    @POST("rating/add")
    Call<ApiResponse> addRating(@Query("title") String title,@Query("service_id") String service_id,@Query("client_id") String client_id,
                             @Query("avis") double avis, @Query("comment") String comment, @Query("type") String type);

    @POST("rating/check")
    Call<ApiResponse> checkRating(@Query("service_id") String service_id,@Query("client_id") String client_id);

    @POST("chat/fail")
    Call<ApiResponse> failTache(@Query("bon_id") int bon_id);

    @POST("chat/spam")
    Call<ApiResponse> spam(@Query("bon_id") int bon_id, @Query("text") String text);

    @POST("chat/blocage")
    Call<ApiResponse> blocage(@Query("him") int him);

    @POST("chat/doblock")
    Call<ApiResponse> doBlock(@Query("him") int him);

    @POST("chat/dotransfert")
    Call<ApiResponse> doTransfert(@Query("bon_id") int bon_id, @Query("service_id") int service_id);

    @POST("finance/addedittresor")
    Call<ApiResponse> addEditTresor(@Query("date") String date, @Query("designation") String designation, @Query("montant") double montant, @Query("old") Integer old);

    @POST("finance/deletetresor")
    Call<ApiResponse> deleteTresor(@Query("old") Integer old);


    @GET("reward/list")
    Call<List<Reward>> getRewards();

    @POST("reward/buy")
    Call<ApiResponse> doBuy(@Query("id") int id);

    @POST("service/candelete")
    Call<ApiResponse> canDelete(@Query("id") int id);

    @POST("reward/addgagner")
    Call<ApiResponse> addGagner(@Query("montant") int montant);

    @POST("reward/plan")
    Call<ApiResponse> doPlan(@Query("id") int id);

    @POST("reward/swap")
    Call<ApiResponse> swapEtat(@Query("service_id") int service_id);

    @GET
    Call<List<Journal>> getCreditConsommation(@Url String url);

    @POST("reward/link")
    Call<ApiResponse> generateLink(@Query("service_id") int service_id);

    @POST("reward/activatelink")
    Call<ApiResponse> activateLink(@Query("link") String link);

    @POST("reward/dosortie")
    Call<ApiResponse> doSortie(@Query("note") String note, @Query("montant") Integer montant, @Query("user_id") Integer user_id);

    @POST("reward/dobonustocredit")
    Call<ApiResponse> doBonusToCredit(@Query("note") String note, @Query("montant") Integer montant);

    @POST("reward/userbyref")
    Call<ApiResponse> userByRef(@Query("destination_id") Integer id);

    @POST("service/asknew")
    Call<ApiResponse> askNew(@Query("designation") String s);

    @Multipart
    @POST("reward/addmontant")
    Call<ApiResponse> addMontant(@Query("note") String note, @Query("montant") Integer montant, @Part MultipartBody.Part image);


    @GET("poi")
    Call<List<Categorie>> getInterest();

    @POST("poi/save")
    Call<ApiResponse> saveInterests(@Query("cat_id") Integer cat_id, @Query("onoff") Integer onn_off);


    @POST("fcm")
    Call<ApiResponse> fcm(@Query("fcm") String fcm);

    @POST("unfcm")
    Call<ApiResponse> unfcm(@Query("fcm") String fcm);

    @GET("oldreview")
    Call<List<Avis>> getOldReview();

    @POST("deleteoldreview")
    Call<ApiResponse> deleteOldReview(@Query("bon_id") Integer bon_id);

    //*************************************************************************************************
    @GET("test")
    Call<String> getTest();

    @GET("home")
    Call<Apidatahome> getHomeFeed(@Query("page") Integer page, @Query("getnotifi") String notifi);

    @GET("servicefeed")
    Call<List<HomeFeed>> getuserservice(@Query("id") String id);

    @GET("homeads")
    Call<Apidata> getHomeadsFeed(@Query("page") Integer page, @Query("srch") String srch, @Query("side") String side);

    @GET("getmynotifi")
    Call<List<notifi>> getmynotifi(@Query("page") Integer page, @Query("srch") String srch, @Query("id") String id);

    @GET("homeadsb")
    Call<List<HomeadsFeed>> getmyoffer(@Query("page") Integer page,@Query("srch") String srch,@Query("id") String id);

    @GET("repreq")
    Call<List<repreq>> getoffer(@Query("page") Integer page, @Query("id") String id);

    @POST("quicklogin")
    Call<Customer> quickLoginCustomer();

    @POST("updateprofile")
    Call<Customer> updateProfile(@Body Customer customer);

    @POST("updatecompte")
    Call<Customer> updateCompte(@Body Customer customer);

    @GET("chat/list")
    Call<List<Tache>> getTaches();

    @POST("finance/dettes")
    Call<List<Dette>> getDettes(@Query("month") String month);

    @POST("finance/tresor")
    Call<List<Journal>> getTresor(@Query("month") String month);

    @POST("finance/livre")
    Call<List<Journal>> getLivre(@Query("month") String month);

    @POST("statistiques/etatannuel")
    Call<List<Journal>> getEtatAnnuel(@Query("year") Integer year);

    @POST("statistiques/etatmensuel")
    Call<List<Dette>> getEtatMensuel(@Query("year") Integer year);

    @POST("statistiques/etatservicemensuel")
    Call<List<Dette>> getEtatServiceMensuel(@Query("year") Integer year);

    @POST("statistiques/etatservice")
    Call<List<Dette>> getEtatService(@Query("year") Integer year);

    @POST("chat/one")
    Call<Tache> getOneTaches(@Query("bon_id") String bon_id);

    @POST("offer/one")
    Call<HomeadsFeed> getOneoffer(@Query("id") String id);


    @GET("montant")
    Call<List<Montant>> getMontant();

    @GET("myservice")
    Call<List<Service>> getMyService();

    @Multipart
    @POST("service/addedit")
    Call<ApiResponse> addEditService(@Query("nom") String nom,@Query("city") String city,@Query("wilaya") String wilaya,
            @Query("souscategorie_id") Integer sous_id,@Query("min_price") String min_price, @Query("title") String title, @Query("description") String description,
                                     @Query("tags") String tags, @Query("service_id") Integer service_id, @Query("gagner") Integer gagner, @Part MultipartBody.Part image);

    @Multipart
    @POST("service/editservice")
    Call<ApiResponse> editService(@Query("nom") String nom,@Query("city") String city,@Query("wilaya") String wilaya,
                                     @Query("souscategorie_id") Integer sous_id,@Query("min_price") String min_price, @Query("title") String title, @Query("description") String description,
                                     @Query("tags") String tags, @Query("service_id") Integer service_id, @Query("gagner") Integer gagner, @Part MultipartBody.Part image);


    @Multipart
    @POST("service/uploadalbums")
    Call<ApiResponse> uploadAlbums(@Query("service_id") Integer id, @PartMap() Map<String, RequestBody> albums, @Part List<MultipartBody.Part> params);

    @GET("service/albums")
    Call<List<Photo>> getAlbums(@Query("service_id") Integer id);


    @GET("service/avis")
    Call<List<Avis>> getAvis(@Query("service_id") Integer service_id);

    @GET("services")
    Call<List<HomeFeed>> getServicesByUser(@Query("user_id") Integer user_id);


    @GET("maps")
    Call<List<Customer>> getMap(@Query("lat") String lat, @Query("lon") String lon, @Query("zoom") String zoom, @Query("dist") String dist);

    @POST("updateservice")
    Call<ApiResponse> updateService(@Body Service service);


    @GET("service")
    Call<List<Service>> getService(@Query("admin_id") int admin_id);

    @GET("service/one")
    Call<HomeFeed> getOneService(@Query("service_id") int id);

    @Multipart
    @POST("avatar")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("photos")
    Call<ApiResponse> uploadPhotos(@Part List<MultipartBody.Part> photos, @Query("service_id") Integer service_id);

    @GET("similar")
    Call<List<Customer>> getSimilar(@Query("category") String category);

    @GET("search")
    Call<List<Customer>> getSearch(@Query("text") String text);

    @GET("demandeadmins")
    Call<List<Customer>> getDemandeAdmins(@Query("demande_id") int admin_id);

    @GET("activite")
    Call<List<Activite>> getActivite();

    @GET("demandefini")
    Call<List<Tache>> getDemandeFini();

    @GET("demandeaccepte")
    Call<List<Tache>> getDemandeAccepte();


    @POST("deletemontant")
    Call<ApiResponse> deleteMontant(@Body Montant montant);

    @Multipart
    @POST("addevaluation")
    Call<ApiResponse> addEvaluation(@Body Avis evaluation, @Part MultipartBody.Part image);

    @POST("addevaluation")
    Call<ApiResponse> addEvaluation(@Body Avis evaluation);


    @GET("stats")
    Call<List<Stats>> getStats();

    @POST("updatePoints")
    Call<ApiResponse> updatePointsUser(@Body Customer customer);


    @GET("demandeconversation")
    Call<List<Chat>> getDemandeConversation(@Query("demande_id") Integer demande_id, @Query("admin_id") Integer admin_id);


    @POST("gopro")
    Call<Customer> goPro(@Body Customer customer);

    @GET("oldposition")
    Call<Customer> getOldPosition();

    @POST("updateposition")
    Call<ApiResponse> updatePosition(@Body Customer customer);

    @POST("updatebusinessposition")
    Call<ApiResponse> updateBusinessPosition(@Body Customer customer);

    @POST("updatebusiness")
    Call<ApiResponse> updateBusiness(@Body Customer customer);

    @POST("offer/add")
    Call<ApiResponse> addEditAds(@Query("user") String user,@Query("type") String type,@Query("title") String title,@Query("des") String des,@Query("daytime") String daytime,
                                 @Query("adrs") String adrs, @Query("urgence") int urgence, @Query("dateStart") String dateStart, @Query("unity") String unity, @Query("quantity") int quantity, @Query("price") String price);

    @POST("repreq/add")
    Call<ApiResponse> addEditrepreq(@Query("user") String user,@Query("req") String id, @Query("des") String des,@Query("daytime") String daytime,@Query("prix") String prix
            , @Query("change") String change,@Query("service_id") int serv,@Query("type") String type, @Query("date_start") String date_start, @Query("unity") String unity, @Query("quantity") int quantity);


    @POST("demand/add")
    Call<ApiResponse> addDemandService(@Query("user_id") int user_id,@Query("service_id") int service_id,@Query("date_start") String dateStart,@Query("date_end") String dateEnd,@Query("category") String category,
                                       @Query("city") String city, @Query("urgence") int urgence, @Query("code_promo") String code_promo, @Query("unity") String unity, @Query("quantity") int quantity, @Query("price") String price,
                                       @Query("description") String description, @Query("pro_id") int pro_id, @Query("option") int option);

    @POST("demand/add")
    Call<ApiResponse> addDemandService(@Query("user_id") int user_id,@Query("service_id") int service_id,@Query("date_start") String dateStart,@Query("date_end") String dateEnd,@Query("category") String category,
                                       @Query("city") String city, @Query("urgence") int urgence, @Query("code_promo") String code_promo, @Query("unity") String unity, @Query("quantity") int quantity, @Query("price") String price,
                                       @Query("description") String description, @Query("pro_id") int pro_id, @Query("option") int option,
                                       @Query("offer_id") int offer_id);

    @POST("demand/get")
    Call<DemandService> getDemandService(@Query("id") int id);

    @GET("demand/get")
    Call<List<DemandService>> getDemandsServiceUser(@Query("state") int state, @Query("user_id") int user_id);

    @GET("demand/get")
    Call<List<DemandService>> getDemandsServicePro(@Query("state") int state, @Query("pro_id") int pro_id);

    @POST("demand/state")
    Call<ApiResponse> updateDemandState(@Query("state") int state, @Query("id") int demand_id);

    @POST("demand/update")
    Call<ApiResponse> updateDemandService(@Query("date_start") String date_start,
                                          @Query("date_end") String date_end, @Query("quantity") int quantity,
                                          @Query("price") int price, @Query("id") int demand_id,
                                          @Query("negociate") int negociate, @Query("unity") String unity);

    @POST("demand/update/date")
    Call<ApiResponse> updateDateDemandService(@Query("date_end") String date_end, @Query("id") int demand_id);

    @GET("commissions/get")
    Call<List<Commission>> getCommissions(@Query("pro_id") int pro_id);

    @POST("commission/add")
    Call<ApiResponse> addCommission(@Query("demandService_id") int demandService_id, @Query("pro_id") int pro_id,
                                    @Query("user_id") int user_id, @Query("commission") String commission);

    @Multipart
    @POST("commission/paied")
    Call<ApiResponse> paiedCommission(@Query("id") String commission_id, @Part MultipartBody.Part image);

    @POST("offer/negociation/decline")
    Call<ApiResponse> declineNegociation(@Query("id") int repreq_id);

    @POST("user/services/available")
    Call<List<Service>> availableServices(@Query("id") int user_id);

}