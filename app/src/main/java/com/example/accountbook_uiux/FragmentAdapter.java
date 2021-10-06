package com.example.accountbook_uiux;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//탭이 변할때마다 position을 받아 Fragment를 전환함
public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    //position 값 받음
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new Frag_outlay();
            case 1:
                return new Frag_income();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
