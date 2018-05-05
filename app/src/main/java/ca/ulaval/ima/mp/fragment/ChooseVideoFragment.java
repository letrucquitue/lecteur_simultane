package ca.ulaval.ima.mp.fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.listener.OnItemClickListener;
import ca.ulaval.ima.mp.adapter.VideoListAdapter;
import ca.ulaval.ima.mp.model.VideoModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseVideoFragment extends Fragment {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyBfdzOTVomBllyKzi3GROReQFtO9PrYGLs";
    private static String CHANNEL_GET_URL_BEFORE_KEYWORDS = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
    private static String CHANNEL_GET_URL_AFTER_KEYWORDS = "&type=video&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY;
    private static String VIDEO_DETAILS_GET_URL = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&key="+GOOGLE_YOUTUBE_API_KEY+"&id=";
    //private static String CHANNEL_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + PLAYLIST_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";

    private EditText search;
    private RecyclerView mList_videos = null;
    private VideoListAdapter adapter = null;
    private ArrayList<VideoModel> mListData = new ArrayList<>();
    private String keywords_to_search = "";
    private final String DEFAULT_KEYWORDS = "Denis Brogniart AH";

    public ChooseVideoFragment() {
        // Required empty public constructor
    }


    //A la creation de la vue
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_video, container, false);

        //Gestion du champ "Rechercher"
        search = (EditText) view.findViewById(R.id.search_field);

        //KEYWORDS FROM PREFERENCES
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keywords = prefs.getString("keywords", DEFAULT_KEYWORDS);
        if(keywords != DEFAULT_KEYWORDS)
            search.setText(keywords);

        //MAJ LIST
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos);
        actualizeList(keywords);

        //search event
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //hide keyboard
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    //MAJ PREFERENCES KEYWORDS
                    Context context = getActivity().getApplicationContext();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putString("keywords", search.getText().toString());
                    editor.commit();

                    //actualize list
                    actualizeList(search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        //enter key event (pour simulation sur ordinateur)
        search.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            actualizeList(search.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        return view;
    }

    //Initialisation de la liste de videos affichees dans l'onglet "Choisir"
    private void initList(ArrayList<VideoModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Adapter qui va gerer le clic sur un element
        adapter = new VideoListAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(VideoModel item) {
                //maj du bottom navigation view (afficher onglet properties)
                BottomNavigationView nav = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                nav.setSelectedItemId(R.id.navigation_properties);

                //SET VIDEO ID ON PREFERENCES
                Context context = getActivity().getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("video_id", item.getId());
                editor.commit();

                //go to properties fragment with video id parameter
                PropertiesFragment fragment = new PropertiesFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_content, fragment , "fragment_properties");
                ft.commit();
            }

            @Override
            public void onItemClick(String item) {
                //USELESS (A REVOIR)
            }
        });
        mList_videos.setAdapter(adapter);

    }

    //Apres avoir recherche un mot
    private void actualizeList(String keywords){
        this.keywords_to_search = keywords;
        initList(mListData);
        new RequestYoutubeAPI().execute();
    }

    //Asynctask qui recupere les donnees de l'API Youtube Data V3
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            //Definir la liste de mots cles
            String[] keywords = keywords_to_search.split(" ");
            //Definir l'URL du GET par rapport aux mots cles
            String url = CHANNEL_GET_URL_BEFORE_KEYWORDS;
            for (int i=0;i<keywords.length;i++){
                url += keywords[i]+"+";
            }
            url += CHANNEL_GET_URL_AFTER_KEYWORDS;

            HttpGet httpGet = new HttpGet(url);
            Log.e("URL", url);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Fonction qui permet de recuperer la liste de videos obtenues a l aide du GET sur l'API Youtube Data V3
    public ArrayList<VideoModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<VideoModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("kind")) {
                        if (json.getString("kind").equals("youtube#searchResult")) {
                            VideoModel youtubeObject = new VideoModel();
                            JSONObject jsonSnippet = json.getJSONObject("snippet");
                            JSONObject jsonId = json.getJSONObject("id");
                            String video_id = "undefined";
                            if (jsonId.has("videoId")) {
                                video_id = jsonId.getString("videoId");
                            }
                            String title = jsonSnippet.getString("title");
                            String description = jsonSnippet.getString("description");
                            String publishedAt = jsonSnippet.getString("publishedAt");
                            String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                            youtubeObject.setTitle(title);
                            youtubeObject.setDescription(description);
                            youtubeObject.setPublishedAt(publishedAt);
                            youtubeObject.setThumbnail(thumbnail);
                            youtubeObject.setId(video_id);

                            //get duration of the video
                            //with another GET
                            /*HttpClient httpClient = new DefaultHttpClient();
                            String url = VIDEO_DETAILS_GET_URL+video_id;
                            HttpGet httpGet = new HttpGet(url);
                            Log.e("URL", url);
                            try {
                                HttpResponse response = httpClient.execute(httpGet);
                                HttpEntity httpEntity = response.getEntity();
                                String json_details = EntityUtils.toString(httpEntity);
                                jsonObject = new JSONObject(json_details);
                                jsonArray = jsonObject.getJSONArray("items");
                                json = jsonArray.getJSONObject(0);
                                JSONObject jsonContentDetails = json.getJSONObject("contentDetails");
                                String duration = jsonContentDetails.getString("duration");
                                Log.e("Duration : ", duration);
                                youtubeObject.setDuration("duration");
                            } catch (IOException e) {
                                Log.e("OMG","OMG");
                                e.printStackTrace();
                            }*/

                            mList.add(youtubeObject);

                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }
}
