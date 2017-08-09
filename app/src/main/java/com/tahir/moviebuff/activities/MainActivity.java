package com.tahir.moviebuff.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tahir.moviebuff.R;
import com.tahir.moviebuff.fragments.CommonFragment;
import com.tahir.moviebuff.util.BottomNavigationViewEx;
import com.tahir.moviebuff.util.CommonUtil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.tahir.moviebuff.util.Constants.currentFrag;
import static com.tahir.moviebuff.util.Constants.filterSet;
import static com.tahir.moviebuff.util.Constants.fragNowPlaying;
import static com.tahir.moviebuff.util.Constants.fragPopular;
import static com.tahir.moviebuff.util.Constants.fragTopRated;
import static com.tahir.moviebuff.util.Constants.fragUpcoming;

public class MainActivity extends AppCompatActivity {

    @BindColor(R.color.appRedDark)
    int appRedDark;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottomNavigationViewEx)
    BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init the crashlytics lib
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(appRedDark);

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(
                new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        //setFragmentEnterRightExitLeftAnim(ft);


                        //if same fragment is clicked again, ignore it. TAK
                        if (isSameFragmentClicked(id)) {
                            return true;
                        }
                        //END


                        switch (item.getItemId()) {
                            case R.id.actionTabNowPlaying:
                                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                    getSupportFragmentManager().popBackStack();
                                }
                                ft.add(R.id.mainFrameLayout, CommonFragment.newInstance(), fragNowPlaying).addToBackStack(null).
                                        commit();
                                toolbar.setTitle(getString(R.string.tab_now_playing));
                                break;
                            case R.id.actionTabUpcoming:
                                filterSet = false;
                                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragUpcoming).addToBackStack(null).
                                        commit();
                                toolbar.setTitle(getString(R.string.tab_upcoming));
                                break;
                            case R.id.actionTabPopular:
                                filterSet = false;
                                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragPopular).addToBackStack(null).commit();
                                toolbar.setTitle(getString(R.string.tab_popular));
                                break;
                            case R.id.actionTabTopRated:
                                filterSet = false;
                                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragTopRated).addToBackStack(null).commit();
                                toolbar.setTitle(getString(R.string.tab_top_rated));
                                break;
                        }
                        return true;
                    }
                });

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragNowPlaying).commit();

    }

    @Override
    public void onBackPressed() {

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 1; i++) {
            getSupportFragmentManager().popBackStack();
        }
        super.onBackPressed();
        bottomNavigationViewEx.setSelectedItemId(R.id.actionTabNowPlaying);
        toolbar.setTitle(getString(R.string.tab_now_playing));

    }

    private boolean isSameFragmentClicked(int itemClicked) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        String tagName = fragment.getTag();

        if (itemClicked == R.id.actionTabNowPlaying && tagName != null && tagName.equalsIgnoreCase(fragNowPlaying))
            return true;
        else if (itemClicked == R.id.actionTabUpcoming && tagName != null && tagName.equalsIgnoreCase(fragUpcoming))
            return true;
        else if (itemClicked == R.id.actionTabPopular && tagName != null && tagName.equalsIgnoreCase(fragPopular))
            return true;
        else if (itemClicked == R.id.actionTabTopRated && tagName != null && tagName.equalsIgnoreCase(fragTopRated))
            return true;
        return false;
    }

    public void showMsg(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        String tagName = fragment.getTag();

        if (tagName != null && tagName.equalsIgnoreCase(fragNowPlaying))
            bundle.putString(currentFrag, fragNowPlaying);
        else if (tagName != null && tagName.equalsIgnoreCase(fragTopRated))
            bundle.putString(currentFrag, fragTopRated);
        else if (tagName != null && tagName.equalsIgnoreCase(fragPopular))
            bundle.putString(currentFrag, fragPopular);
        else if (tagName != null && tagName.equalsIgnoreCase(fragUpcoming))
            bundle.putString(currentFrag, fragUpcoming);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter:
                openFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getString(currentFrag) != null) {
            String tmpFlag = savedInstanceState.getString(currentFrag);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (tmpFlag.equalsIgnoreCase(fragUpcoming)) {
                bottomNavigationViewEx.setSelectedItemId(R.id.actionTabUpcoming);
                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragUpcoming).addToBackStack(null).commit();
                toolbar.setTitle(getString(R.string.tab_upcoming));

            } else if (tmpFlag.equalsIgnoreCase(fragPopular)) {
                bottomNavigationViewEx.setSelectedItemId(R.id.actionTabPopular);
                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragPopular).addToBackStack(null).commit();
                toolbar.setTitle(getString(R.string.tab_popular));
            } else if (tmpFlag.equalsIgnoreCase(fragTopRated)) {
                bottomNavigationViewEx.setSelectedItemId(R.id.actionTabTopRated);
                ft.replace(R.id.mainFrameLayout, CommonFragment.newInstance(), fragTopRated).addToBackStack(null).commit();
                toolbar.setTitle(getString(R.string.tab_top_rated));
            } else
                bottomNavigationViewEx.setSelectedItemId(R.id.actionTabNowPlaying);
        }


    }

    public void openFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filter_layout);

        final Spinner minYear = (Spinner) dialog.findViewById(R.id.minYear);

        final Spinner maxYear = (Spinner) dialog.findViewById(R.id.maxYear);

        ArrayList<Integer> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1950; i <= thisYear + 3; i++) {
            years.add(i);
        }
        ArrayAdapter<Integer> minYeardapter = new ArrayAdapter<Integer>(this, R.layout.spinner_row, years);

        minYear.setAdapter(minYeardapter);

        ArrayAdapter<Integer> maxYeardapter = new ArrayAdapter<Integer>(this, R.layout.spinner_row, years);

        maxYear.setAdapter(maxYeardapter);

        minYear.setSelection(years.size() - 4);
        maxYear.setSelection(years.size() - 4);

        Button filterButton = (Button) dialog.findViewById(R.id.filterButton);

        final TextView errorView = (TextView) dialog.findViewById(R.id.errorView);

        filterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int minyear = (int) minYear.getSelectedItem();
                int maxyear = (int) maxYear.getSelectedItem();
                if (minyear > maxyear && minyear != maxyear) {
                    errorView.setVisibility(View.VISIBLE);
                    return;
                }
                filterSet = true;
                CommonUtil.saveMinMaxYear(MainActivity.this, minyear, maxyear);
                dialog.dismiss();
                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.mainFrameLayout, CommonFragment.newInstance(), fragNowPlaying).addToBackStack(null).
                        commit();
                toolbar.setTitle(getString(R.string.tab_now_playing));
                bottomNavigationViewEx.setSelectedItemId(R.id.actionTabNowPlaying);

            }
        });

        dialog.show();

    }


}
