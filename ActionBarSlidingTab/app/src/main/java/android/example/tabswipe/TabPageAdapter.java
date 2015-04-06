package android.example.tabswipe;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 00922988 on 3/27/15.
 */
public class TabPageAdapter extends FragmentPagerAdapter {

    Context context;

    public TabPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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
}
