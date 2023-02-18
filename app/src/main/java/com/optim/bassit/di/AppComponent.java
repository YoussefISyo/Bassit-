package com.optim.bassit.di;

import android.content.Context;

import com.optim.bassit.App;
import com.optim.bassit.Main2Activity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.di.modules.ContextModule;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.ui.activities.AddDemandServiceActivity;
import com.optim.bassit.ui.activities.AddMontantActivity;
import com.optim.bassit.ui.activities.AdsAddEditActivity;
import com.optim.bassit.ui.activities.AlbumsAddEditActivity;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.CommissionActivity;
import com.optim.bassit.ui.activities.ConsommationActivity;
import com.optim.bassit.ui.activities.DemandDetailsActivity;
import com.optim.bassit.ui.activities.EditInformationActivity;
import com.optim.bassit.ui.activities.EditServiceActivity;
import com.optim.bassit.ui.activities.FilterActivity;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.activities.InterestActivity;
import com.optim.bassit.ui.activities.LoginActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.MapActivity;
import com.optim.bassit.ui.activities.MapFragActivity;
import com.optim.bassit.ui.activities.MapSearchActivity;
import com.optim.bassit.ui.activities.OldReviewActivity;
import com.optim.bassit.ui.activities.PartagerGagnerActivity;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.activities.RechercheActivity;
import com.optim.bassit.ui.activities.RegisterActivity;
import com.optim.bassit.ui.activities.RegisterActivity2;
import com.optim.bassit.ui.activities.SearchResultActivity;
import com.optim.bassit.ui.activities.ServiceActivity;
import com.optim.bassit.ui.activities.ServiceAddEditActivity;
import com.optim.bassit.ui.activities.ShowNegociationActivity;
import com.optim.bassit.ui.activities.SortieBonusActivity;
import com.optim.bassit.ui.activities.SplashActivity;
import com.optim.bassit.ui.activities.StatistiquesActivity;
import com.optim.bassit.ui.activities.TransfererActivity;
import com.optim.bassit.ui.activities.UpdateMyProfileActivity;
import com.optim.bassit.ui.activities.mynotifiActivity;
import com.optim.bassit.ui.activities.myofferActivity;
import com.optim.bassit.ui.activities.offerActivity;
import com.optim.bassit.ui.activities.repreqAddEditActivity;
import com.optim.bassit.ui.activities.rewardActivity;
import com.optim.bassit.ui.dialogs.BuyPointsDialogFragment;
import com.optim.bassit.ui.dialogs.ChooseDialogFragment;
import com.optim.bassit.ui.dialogs.CloturerDialogFragment;
import com.optim.bassit.ui.dialogs.EvaluationDialogFragment;
import com.optim.bassit.ui.dialogs.FixDetteDialogFragment;
import com.optim.bassit.ui.dialogs.MontantDialogFragment;
import com.optim.bassit.ui.fragments.ActiveDemands;
import com.optim.bassit.ui.fragments.AdsFragment;
import com.optim.bassit.ui.fragments.BeneficeFragment;
import com.optim.bassit.ui.fragments.EtatAnnuelFragment;
import com.optim.bassit.ui.fragments.EtatMensuelFragment;
import com.optim.bassit.ui.fragments.EtatServiceFragment;
import com.optim.bassit.ui.fragments.FinishedDemands;
import com.optim.bassit.ui.fragments.HomeImageFragment;
import com.optim.bassit.ui.fragments.TresorFragment;
import com.optim.bassit.ui.fragments.CompteFragment;
import com.optim.bassit.ui.fragments.DetteFragment;
import com.optim.bassit.ui.fragments.HomeFragment;
import com.optim.bassit.ui.fragments.LivreFinancierFragment;
import com.optim.bassit.ui.fragments.MapFragment;
import com.optim.bassit.ui.fragments.MyProfileFragment;
import com.optim.bassit.ui.fragments.ProFragment;
import com.optim.bassit.ui.fragments.RappelFragment;
import com.optim.bassit.ui.fragments.TachesFragment;
import com.optim.bassit.ui.fragments.WaitingDemands;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(App application);

    // void inject(SplashActivity activity);
    void inject(LoginActivity activity);

    void inject(ShowNegociationActivity activity);

    void inject(MapSearchActivity activity);

    void inject(RegisterActivity2 activity);

    void inject(DemandDetailsActivity activity);

    void inject(RegisterActivity activity);

    void inject(EditServiceActivity activity);

    void inject(FilterActivity activity);

    void inject(AddDemandServiceActivity activity);

    void inject(CommissionActivity activity);

    Context getContext();

    AppApi getApi();


    void inject(MainActivity homeActivity);

    void inject(HomeFragment homeFragment);


    void inject(SplashActivity splashActivity);

    void inject(TachesFragment tachesFragment);

    void inject(InterestActivity interestActivity);


    void inject(MyProfileFragment myProfileFragment);

    void inject(ProfileActivity profileFragment);


    void inject(TransfererActivity transfererActivity);



    void inject(EvaluationDialogFragment evaluationDialogFragment);

    void inject(MontantDialogFragment montantDialogFragment);

    void inject(ServiceAddEditActivity serviceAddEditActivity);

    void inject(ServiceActivity serviceActivity);

    void inject(UpdateMyProfileActivity updateMyProfileActivity);


    void inject(ChatActivity chatActivity);

    void inject(ProFragment proFragment);

    void inject(RappelFragment rappelFragment);

    void inject(RechercheActivity rechercheActivity);

    void inject(MapFragment mapFragment);

    void inject(EditInformationActivity editInformationActivity);

    void inject(ChooseDialogFragment chooseDialogFragment);

    void inject(CloturerDialogFragment cloturerDialogFragment);

    void inject(CompteFragment compteFragment);

    void inject(AddMontantActivity addMontantActivity);

    void inject(ConsommationActivity creditConsommationActivity);

    void inject(BuyPointsDialogFragment buyPointsDialogFragment);

    void inject(PartagerGagnerActivity partagerGagnerActivity);

    void inject(FinanceActivity financeActivity);

    void inject(DetteFragment detteFragment);

    void inject(LivreFinancierFragment livreFinancierFragment);

    void inject(TresorFragment tresorFragment);

    void inject(BeneficeFragment beneficeFragment);

    void inject(StatistiquesActivity statistiquesActivity);

    void inject(EtatAnnuelFragment etatAnnuelFragment);

    void inject(EtatMensuelFragment etatMensuelFragment);

    void inject(EtatServiceFragment etatServiceFragment);

    void inject(SortieBonusActivity sortieBonusActivity);

    void inject(AlbumsAddEditActivity albumsAddEditActivity);

    void inject(MapActivity mapActivity);

    void inject(HomeImageFragment homeImageFragment);

    void inject(FixDetteDialogFragment fixDetteDialogFragment);

    void inject(OldReviewActivity oldReviewActivity);

    void inject(Main2Activity main2Activity);

    void inject(MapFragActivity mapFragActivity);

    void inject(AdsFragment adsFragment);

    void inject(AdsAddEditActivity adsAddEditActivity);

    void inject(repreqAddEditActivity repreqAddEditActivity);

    void inject(myofferActivity myofferActivity);

    void inject(offerActivity offerActivity);

    void inject(rewardActivity rewardActivity);

    void inject(mynotifiActivity mynotifiActivity);

    void inject(WaitingDemands waitingDemands);

    void inject(ActiveDemands activeDemands);

    void inject(FinishedDemands finishedDemands);

    void inject(SearchResultActivity searchResultActivity);
}
