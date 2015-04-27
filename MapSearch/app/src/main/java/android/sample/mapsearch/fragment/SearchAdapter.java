package android.sample.mapsearch.fragment;

import android.content.Context;
import android.sample.mapsearch.R;
import android.sample.mapsearch.model.OpeningHours;
import android.sample.mapsearch.model.Result;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pratyush Kshirsagar on 4/24/15.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Result> mResultList;
    private Context mContext;

    public SearchAdapter(Context cntx) {
        mContext = cntx;
    }

    @Override
    public int getItemCount() {
        if (mResultList != null) {
            return mResultList.size();
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder searchViewHolder, int i) {
        if (mResultList != null) {
            Result result = mResultList.get(i);
            if (result != null) {
                if (result.getName() != null) {
                    searchViewHolder.vTitle.setText(result.getName());

                } else {
                    return;
                }
                OpeningHours openingHours = result.getOpeningHours();
                if (openingHours != null) {
                    String open = openingHours.getOpenNow() ? mContext.getResources().getString(R.string.open_yes) :
                            mContext.getResources().getString(R.string.open_no);
                    searchViewHolder.vOpeningHours.setText(mContext.getResources().getString(R.string.open_now) + " - " + open);
                } else {
                    searchViewHolder.vOpeningHours.setVisibility(View.GONE);
                }
                List<String> types = result.getTypes();
                if (types != null) {
                    StringBuilder listString = new StringBuilder();

                    for (int index = 0; index < types.size(); index++) {
                        if (index ==0) {
                            listString.append(types.get(index) + " ");
                        } else {
                            listString.append(", " + types.get(index) + " ");
                        }
                    }

                    searchViewHolder.vTypes.setText(mContext.getResources().getString(R.string.location_type) + " - " + listString);
                } else {
                    searchViewHolder.vTypes.setVisibility(View.GONE);
                }
                if (result.getVicinity() != null) {
                    searchViewHolder.vVicinity.setText(mContext.getResources().getString(R.string.vicinity) + " - " + result.getVicinity());
                } else {
                    searchViewHolder.vVicinity.setVisibility(View.GONE);
                }
                if (result.getIcon() != null) {
                    Picasso.with(mContext)
                            .load(result.getIcon())
                            .into(searchViewHolder.vLocationIcon);
                }
            }
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new SearchViewHolder(itemView);
    }

    public void cloneList(List<Result> list) {
        mResultList = new ArrayList<Result>(list);
        notifyDataSetChanged();
    }

    public void appendList(List<Result> list) {
        if (mResultList != null) {
            mResultList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void insertItem(Result result) {
        if (mResultList != null) {
            mResultList.add(result);
            notifyItemInserted(mResultList.size()-1);
        }
    }
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        protected TextView vVicinity;
        protected TextView vOpeningHours;
        protected TextView vTitle;
        protected TextView vTypes;
        protected ImageView vLocationIcon;

        public SearchViewHolder(View v) {
            super(v);
            vVicinity =  (TextView) v.findViewById(R.id.tvVicinity);
            vOpeningHours = (TextView)  v.findViewById(R.id.tvOpeningHours);
            vTypes = (TextView)  v.findViewById(R.id.tvLocationType);
            vTitle = (TextView) v.findViewById(R.id.tvTitle);
            vLocationIcon = (ImageView)v.findViewById(R.id.ivIcon);
        }
    }
}