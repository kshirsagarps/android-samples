package android.sample.mapsearch.support;

import android.sample.mapsearch.model.SearchResult;

/**
 * Created by Pratyush Kshirsagar on 4/23/15.
 */
public class Constants {

    /* Search query parameters */
    public static final String APIKEY = "AIzaSyDdaEwSdqIecJ2DWH1eCO8PnghGNyUJ2uk";
    public static final String RADIUS = "2500";
    public static final String SEARCHURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?key=%@&radius=%@&query=%@&location=%@";
    public static final String NEXTPAGETOKEN = "next_page_token";


    /* Network Connection constants */
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECTION_TIMEOUT = 15000;

    /* Constants for search response status */
    public static final String OK = "OK";
    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String REQUEST_DENIED = "REQUEST_DENIED";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";


    /* Search response status enums */
    public static enum Status {
        OK,
        ZERO_RESULTS,
        OVER_QUERY_LIMIT,
        REQUEST_DENIED,
        INVALID_REQUEST
    }

    /* Global Constants */
    public static SearchResult sSearchResult;
    public static String sPrevUrl;


    /* Used for location updates */
    public static final long MIN_DISTANCE_IN_METER = 10;
    public static final long MIN_TIME_IN_MS = 500;
}
