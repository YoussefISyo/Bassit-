package com.optim.bassit.ui.adapters;

import android.content.Context;

import com.google.android.flexbox.FlexboxLayoutManager;

public class FlexboxLayoutManagerBugless extends FlexboxLayoutManager {

    public FlexboxLayoutManagerBugless(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
