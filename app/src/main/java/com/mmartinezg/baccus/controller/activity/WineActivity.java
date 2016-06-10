package com.mmartinezg.baccus.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mmartinezg.baccus.controller.fragment.WineFragment;


public class WineActivity extends FragmentContainerActivity {

    public static final String EXTRA_WINE = "EXTRA_WINE";


    @Override
    protected Fragment createFragment() {
        Bundle arguments = new Bundle();
        arguments.putSerializable(WineFragment.ARG_WINE, getIntent().getSerializableExtra(EXTRA_WINE));

        WineFragment fragment = new WineFragment();
        fragment.setArguments(arguments);

        return fragment;
    }
}
