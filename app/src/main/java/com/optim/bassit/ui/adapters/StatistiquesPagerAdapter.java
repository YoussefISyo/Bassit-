package com.optim.bassit.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.ui.activities.StatistiquesActivity;
import com.optim.bassit.ui.fragments.EtatAnnuelFragment;
import com.optim.bassit.ui.fragments.EtatMensuelFragment;
import com.optim.bassit.ui.fragments.EtatServiceFragment;
import com.optim.bassit.ui.fragments.PlanFragment;

public class StatistiquesPagerAdapter extends FragmentStatePagerAdapter {

    public StatistiquesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    EtatMensuelFragment etatMensuelFragment;
    EtatAnnuelFragment etatAnnuelFragment;
    EtatServiceFragment etatServiceFragment;
    PlanFragment plan1;
    PlanFragment plan2;
    PlanFragment plan3;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (CurrentUser.getInstance().getmCustomer().getPlan() > 101) {
                    if (etatMensuelFragment == null)
                        etatMensuelFragment = EtatMensuelFragment.newInstance();

                    StatistiquesActivity.registerFilter(etatMensuelFragment);
                    return etatMensuelFragment;
                } else {
                    if (plan1 == null)
                        plan1 = PlanFragment.newInstance("Silver");
                    return plan1;
                }
            case 1:
                if (CurrentUser.getInstance().getmCustomer().getPlan() > 101) {
                if (etatAnnuelFragment == null)
                    etatAnnuelFragment = EtatAnnuelFragment.newInstance();

                StatistiquesActivity.registerFilter(etatAnnuelFragment);
                return etatAnnuelFragment;
                } else {
                    if (plan2 == null)
                        plan2 = PlanFragment.newInstance("Silver");
                    return plan2;
                }
            case 2:
                if (CurrentUser.getInstance().getmCustomer().getPlan() > 102) {
                if (etatServiceFragment == null)
                    etatServiceFragment = EtatServiceFragment.newInstance();

                StatistiquesActivity.registerFilter(etatServiceFragment);
                return etatServiceFragment;
                } else {
                    if (plan3 == null)
                        plan3 = PlanFragment.newInstance("Gold");
                    return plan3;
                }

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}
