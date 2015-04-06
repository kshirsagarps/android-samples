package android.example.tabslide;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Pratyush Kshirsagar on 3/27/15.
 */
public class TabPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    Context context;

    // Tab titles
    private String[] tabs = { "Music", "Photos", "Movies" };

    public TabPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MusicFragment();
            case 1:
                return new PhotosFragment();
            case 2:
                return new MoviesFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
