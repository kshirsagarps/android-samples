package android.sample.mapsearch.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.sample.mapsearch.MainActivity;
import android.sample.mapsearch.R;
import android.sample.mapsearch.fragment.SearchAdapter;
import android.sample.mapsearch.model.SearchResult;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

/**
 * Created by Pratyush Kshirsagar on 4/24/15.
 * Search task downloads and blows out the model classes, further provides to the #SearchAdapter.
 */
public class SearchTask extends AsyncTask<Void,Void,JSONObject> {

    private String mSearchUrl;
    private Context mContext;
    private boolean isNext;
    private SearchAdapter mSearchAdapter;
    private ProgressDialog mProgressDialog;

    public SearchTask(String url, Context cntx, boolean next, SearchAdapter adptr) {
        mSearchUrl = url;
        mContext = cntx;
        isNext = next;
        mSearchAdapter = adptr;
    }

    @Override
    protected void onPreExecute() {
        if (!isNext) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
            mProgressDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        if (!isNext) {
            Constants.sPrevUrl = mSearchUrl;
        }
        JSONObject jsonObject = Utility.getJsonForUrl(mSearchUrl);
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        final ObjectMapper mapper = new ObjectMapper();
        try {
            if (jsonObject != null) {
                Constants.sSearchResult = mapper.readValue(jsonObject.toString(), SearchResult.class);

                Resources res = mContext.getResources();
                String[] response_msgs = res.getStringArray(R.array.response_status_array);
                int index = 0;
                if (Constants.sSearchResult != null && Constants.sSearchResult.getStatus() != null && mSearchAdapter != null) {
                    if (Constants.sSearchResult.getStatus().equals(Constants.OK)) {
                        if (isNext) {
                            mSearchAdapter.appendList(Constants.sSearchResult.getResults());
                        } else {
                            mSearchAdapter.cloneList(Constants.sSearchResult.getResults());
                        }
                        index = Constants.Status.OK.ordinal();

                    } else if ((Constants.sSearchResult.getStatus().equals(Constants.INVALID_REQUEST))) {
                        index = Constants.Status.INVALID_REQUEST.ordinal();
                    } else if ((Constants.sSearchResult.getStatus().equals(Constants.OVER_QUERY_LIMIT))) {
                        index = Constants.Status.OVER_QUERY_LIMIT.ordinal();
                    } else if ((Constants.sSearchResult.getStatus().equals(Constants.ZERO_RESULTS))) {
                        index = Constants.Status.ZERO_RESULTS.ordinal();
                    } else if ((Constants.sSearchResult.getStatus().equals(Constants.REQUEST_DENIED))) {
                        index = Constants.Status.REQUEST_DENIED.ordinal();
                    }
                }

                if (index != 0 && MainActivity.getMsgTextView() != null) {
                    MainActivity.getMsgTextView().setVisibility(View.VISIBLE);
                } else if (MainActivity.getMsgTextView() != null){
                    MainActivity.getMsgTextView().setVisibility(View.GONE);
                }

                if (!isNext) {
                    Utility.showToast(mContext, response_msgs[index]);
                }
            } else {
                Utility.showToast(mContext, mContext.getResources().getString(R.string.no_json_data));
            }

        }
        catch (final Exception e) {
            e.printStackTrace();
        }

    }
}
