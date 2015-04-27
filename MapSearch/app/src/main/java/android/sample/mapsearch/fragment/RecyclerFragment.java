package android.sample.mapsearch.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.sample.mapsearch.R;
import android.sample.mapsearch.support.Constants;
import android.sample.mapsearch.support.SearchTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pratyush Kshirsagar on 4/24/15.
 */
public class RecyclerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SearchAdapter mRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mVisibleThreshold = 5;
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerviewAdapter = new SearchAdapter(getActivity().getApplicationContext());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerviewAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(scrollListener);

    }

    /**
     * ScrollListener to query the next page and update the search results.
     */
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mVisibleItemCount = mRecyclerView.getChildCount();
            mTotalItemCount = mLinearLayoutManager.getItemCount();
            mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (mLoading) {
                if (mTotalItemCount > mPreviousTotal) {
                    mLoading = false;
                    mPreviousTotal = mTotalItemCount;
                }
            }
            if (!mLoading && (mTotalItemCount - mVisibleItemCount)
                    <= (mFirstVisibleItem + mVisibleThreshold)) {

                if (Constants.sSearchResult != null && Constants.sPrevUrl != null &&
                        Constants.sSearchResult.getNextPageToken() != null) {
                    String searchUrl = Constants.sPrevUrl + "&" + Constants.NEXTPAGETOKEN + "=" + Constants.sSearchResult.getNextPageToken();
                    new SearchTask(searchUrl, getActivity(), true, mRecyclerviewAdapter).execute();
                }

                mLoading = true;
            }
        }
    };

    public SearchAdapter getAdapter() {
        return mRecyclerviewAdapter;
    }

}
