package com.cuijeb.battleship;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SettingsActivity extends AppCompatActivity {

    // Number of pages: settings and instructions
    private static final int NUM_PAGES = 2;

    // Pager widget. allows swipes and moving through fragments
    private ViewPager mPager;

    // Pager adapter provides the pages to the view pager
    private PagerAdapter pagperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Instantiate a Vieew Pager and a PagerAdapter
        mPager = findViewById(R.id.pager);
        pagperAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagperAdapter);
    }

    // From android developer using ViewPagers, sliding through fragments.
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    // From android developer using ViewPagers, sliding through fragments.
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Fragment[] fragments = new Fragment[2];
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments[0] = new SettingsFragment();
            fragments[1] = new InstructionsFragment();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}