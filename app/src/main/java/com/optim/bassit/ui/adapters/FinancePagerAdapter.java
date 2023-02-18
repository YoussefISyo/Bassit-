package com.optim.bassit.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.ui.activities.FinanceActivity;
import com.optim.bassit.ui.fragments.BeneficeFragment;
import com.optim.bassit.ui.fragments.PlanFragment;
import com.optim.bassit.ui.fragments.TresorFragment;
import com.optim.bassit.ui.fragments.DetteFragment;
import com.optim.bassit.ui.fragments.LivreFinancierFragment;

public class FinancePagerAdapter extends FragmentStatePagerAdapter {

    public FinancePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    DetteFragment detteFragment;
    LivreFinancierFragment livrefinancierFragment;
    BeneficeFragment beneficeFragment;
    TresorFragment tresorFragment;
    PlanFragment planFragment;

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (CurrentUser.getInstance().getmCustomer().getPlan() >= 100) {

                    if (detteFragment == null)
                        detteFragment = new DetteFragment();

                    FinanceActivity.registerFilter(detteFragment);
                    return detteFragment;
                } else {
                    if (planFragment == null)
                        planFragment = PlanFragment.newInstance("Bronze");
                    return planFragment;
                }
            case 1:
                if (beneficeFragment == null)
                    beneficeFragment = new BeneficeFragment();
                FinanceActivity.registerFilter(beneficeFragment);
                return beneficeFragment;
            case 2:
                if (tresorFragment == null)
                    tresorFragment = new TresorFragment();
                FinanceActivity.registerFilter(tresorFragment);
                return tresorFragment;
            case 3:
                if (livrefinancierFragment == null)
                    livrefinancierFragment = new LivreFinancierFragment();
                FinanceActivity.registerFilter(livrefinancierFragment);
                return livrefinancierFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public void setIsDirty() {
        if (tresorFragment != null)
            tresorFragment.getTresor();
        if (livrefinancierFragment != null)
            livrefinancierFragment.getLivre();
    }
}
