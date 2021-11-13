package com.example.accountbook_uiux;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;


public class TotalStatsFragment extends Fragment {

    private View view_totalStats;
    public static Fragment TYPE_SELECTED = null;

    TabLayout tab_stats;
    FrameLayout frameLayout_stats;

    StatsViewFragment statsViewFragment;
    HistoryViewFragment historyViewFragment;
    ItemViewFragment itemViewFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_totalStats = inflater.inflate(R.layout.frame_total_stats, container, false);
        tab_stats = view_totalStats.findViewById(R.id.tablayout_stats);
        frameLayout_stats = view_totalStats.findViewById(R.id.frameLayout_stats);

        statsViewFragment = new StatsViewFragment();
        historyViewFragment = new HistoryViewFragment();
        itemViewFragment = new ItemViewFragment();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frameLayout_stats, statsViewFragment);
        ft.commit();

        tab_stats.addTab(tab_stats.newTab().setText("지출"));
        tab_stats.addTab(tab_stats.newTab().setText("일간"));
        tab_stats.addTab(tab_stats.newTab().setText("품목별"));


        tab_stats.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    TYPE_SELECTED = statsViewFragment;
                }
                else if (tab.getPosition() == 1) {
                    TYPE_SELECTED = historyViewFragment;
                }
                else if (tab.getPosition() == 2) {
                    TYPE_SELECTED = itemViewFragment;
                }
                ft.replace(R.id.frameLayout_stats, TYPE_SELECTED);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view_totalStats;
    }
}