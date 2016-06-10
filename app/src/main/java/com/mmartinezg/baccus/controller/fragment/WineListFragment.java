package com.mmartinezg.baccus.controller.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mmartinezg.baccus.R;
import com.mmartinezg.baccus.controller.activity.WineryActivity;
import com.mmartinezg.baccus.model.Wine;
import com.mmartinezg.baccus.model.Winery;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WineListFragment extends Fragment {

    private OnWineSelectedListener mOnWineSelectedListener = null;
    private ProgressDialog mProgressDialog = null;


    public WineListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root =  inflater.inflate(R.layout.fragment_wine_list, container, false);

        AsyncTask<Void, Void, Winery> wineryDownloader = new AsyncTask<Void, Void, Winery>() {
            @Override
            protected Winery doInBackground(Void... params) {
                return Winery.getInstance();
            }

            @Override
            protected void onPostExecute(final Winery winery) {
                super.onPostExecute(winery);
                WineListAdapter adapter = new WineListAdapter(getActivity(), winery);

                ExpandableListView listView = (ExpandableListView)root.findViewById(android.R.id.list);

                listView.setAdapter(adapter);

                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        int index = winery.getAbsolutePosition(Winery.WineType.values()[groupPosition], childPosition);
                        if(mOnWineSelectedListener != null){
                            mOnWineSelectedListener.onWineSelected(index);
                        }return false;
                    }
                });

                mProgressDialog.dismiss();
            }
        };

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        if(!Winery.isInstaceAvailable()){
            mProgressDialog.show();
        }
        wineryDownloader.execute();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnWineSelectedListener = (OnWineSelectedListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnWineSelectedListener = null;
    }

    public interface OnWineSelectedListener{
        void onWineSelected(int wineIndex);
    }

    class WineListAdapter extends BaseExpandableListAdapter{
        private Context mContext = null;
        private Winery mWinery = null;

        public WineListAdapter(Context context, Winery winery){
            mContext = context;
            mWinery = winery;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View wineRow = inflater.inflate(R.layout.list_item_wine, parent, false);

            ImageView wineImage = (ImageView) wineRow.findViewById(R.id.wine_image);
            TextView wineName = (TextView) wineRow.findViewById(R.id.wine_name);
            TextView wineCompany = (TextView) wineRow.findViewById(R.id.wine_company);

            Wine currentWine = getChild(groupPosition, childPosition);
            wineImage.setImageBitmap(currentWine.getPhoto(getActivity()));
            wineName.setText(currentWine.getName());
            wineCompany.setText(currentWine.getComanyName());

            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "Valentina-Regular.otf");
            wineName.setTypeface(tf);
            wineCompany.setTypeface(tf);

            return  wineRow;
        }

        @Override
        public int getGroupCount() {
            return Winery.WineType.values().length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mWinery.getWineCount(getGroup(groupPosition));
        }

        @Override
        public Winery.WineType getGroup(int position) {
            return Winery.WineType.values()[position];
        }

        @Override
        public Wine getChild(int groupPosition, int childPosition) {
            return mWinery.getWine(getGroup(groupPosition), childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View wineHader = inflater.inflate(R.layout.list_item_wine_header, parent, false);
            TextView headerText = (TextView) wineHader.findViewById(R.id.wine_type);
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "Valentina-Regular.otf");
            headerText.setTypeface(tf);

            if(getGroup(groupPosition) == Winery.WineType.RED){
                headerText.setText(R.string.red);
            }else if(getGroup(groupPosition) == Winery.WineType.WHITE){
                headerText.setText(R.string.white);
            }else if(getGroup(groupPosition) == Winery.WineType.ROSE){
                headerText.setText(R.string.rose);
            }else {
                headerText.setText(R.string.other);
            }
            return wineHader;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
