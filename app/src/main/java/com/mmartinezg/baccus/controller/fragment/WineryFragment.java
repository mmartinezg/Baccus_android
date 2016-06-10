package com.mmartinezg.baccus.controller.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.mmartinezg.baccus.R;
import com.mmartinezg.baccus.controller.adapter.WineryPagerAdapter;
import com.mmartinezg.baccus.model.Wine;
import com.mmartinezg.baccus.model.Winery;

public class WineryFragment extends Fragment{

    public static final String ARG_WINE_INDEX = "ARG_WINE_INDEX";
    public static final String PREF_LAST_WINE_INDEX = "lastWine";


    private ProgressDialog mProgressDialog = null;
    private ViewPager mPager = null;
    private ActionBar mActionBar = null;
    private Winery mWinery = null;

    public static WineryFragment newInstance(int wineIndex){
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_WINE_INDEX, wineIndex);
        WineryFragment fragment = new WineryFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_winery, container, false);

        AsyncTask<Void, Void, Winery> wineryDownload = new AsyncTask<Void, Void, Winery>() {
            @Override
            protected Winery doInBackground(Void... params) {
                return Winery.getInstance();
            }

            @Override
            protected void onPostExecute(Winery winery) {
                super.onPostExecute(winery);
                mWinery = winery;

                mActionBar = (ActionBar) ((AppCompatActivity)getActivity()).getSupportActionBar();

                mPager = (ViewPager) root.findViewById(R.id.pager);
                mPager.setAdapter(new WineryPagerAdapter(getFragmentManager()));

                mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                    @Override
                    public void onPageSelected(int position) {
                        updateActionBar(position);

                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                                .edit()
                                .putInt(PREF_LAST_WINE_INDEX, position)
                                .commit();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });

                int initialWineIndex = getArguments().getInt(ARG_WINE_INDEX);
                mPager.setCurrentItem(initialWineIndex);
                mProgressDialog.dismiss();

            }
        };

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        if(!Winery.isInstaceAvailable()){
            mProgressDialog.show();
        }
        wineryDownload.execute();

        return root;
    }

    private void updateActionBar(int index){
        mActionBar.setTitle(mWinery.getWine(index).getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_winery, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superValue = super.onOptionsItemSelected(item);

        if(mPager != null) {
            if (item.getItemId() == R.id.menu_next && mPager.getCurrentItem() < mWinery.getWineCount() - 1) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
            } else if (item.getItemId() == R.id.menu_prev && mPager.getCurrentItem() > 0) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return false;
            }
        }
        return superValue;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mPager != null) {
            MenuItem menuNext = menu.findItem(R.id.menu_next);
            MenuItem menuPrev = menu.findItem(R.id.menu_prev);

            menuNext.setEnabled(mPager.getCurrentItem() < mWinery.getWineCount() - 1);
            menuPrev.setEnabled(mPager.getCurrentItem() > 0);
        }
    }

    public void changeWine(int wineIndex){
        mPager.setCurrentItem(wineIndex);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mPager != null) {
            getArguments().putInt(ARG_WINE_INDEX, mPager.getCurrentItem());
        }
    }
}
