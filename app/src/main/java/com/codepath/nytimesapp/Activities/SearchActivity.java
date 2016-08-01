package com.codepath.nytimesapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.nytimesapp.Adapters.ArticleArrayAdapter;
import com.codepath.nytimesapp.EndlessScrollListener;
import com.codepath.nytimesapp.Fragments.DatePickerFragment;
import com.codepath.nytimesapp.Models.Article;
import com.codepath.nytimesapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //EditText etQuery;
    //Button btnSearch;
    GridView gvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    boolean art;
    boolean business;
    boolean cars;
    boolean dining;
    boolean education;
    boolean jobs;
    boolean sports;
    boolean tech;
    boolean nyt;
    boolean startDate;
    Integer sdYear;
    Integer sdMonth;
    Integer sdDay;
    String q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startDate = false;
        q = "";
        setupViews();

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                fetchArticles(q, page, true);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });


    }

    public void setupViews() {
        //etQuery = (EditText) findViewById(R.id.etQuery);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gdResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(i, 5);
            return true;
        }
        if (id == R.id.action_startdate) {
            showDatePickerDialog(gvResults);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 5) {
            if (data.getBooleanExtra("a", false)) {
                art = true;
            }
            if (data.getBooleanExtra("b", false)) {
                business = true;
            }
            if (data.getBooleanExtra("c", false)) {
                cars = true;
            }
            if (data.getBooleanExtra("d", false)) {
                dining = true;
            }
            if (data.getBooleanExtra("e", false)) {
                education = true;
            }
            if (data.getBooleanExtra("j", false)) {
                jobs = true;
            }
            if (data.getBooleanExtra("s", false)) {
                sports = true;
            }
            if (data.getBooleanExtra("t", false)) {
                tech = true;
            }
            if (data.getBooleanExtra("source", false)) {
                nyt = true;
            }
        }
    }

    @Deprecated
    public void onArticleSearch(View view) {
        //String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "bb087743efd64040862baad2635ebf29");
        params.put("page", 0);
        //params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                JSONArray articleResults = null;

                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleResults));
                    Log.d("DEBUG", articles.toString());
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                q = query;
                searchView.clearFocus();
                fetchArticles(query, 0, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                q = newText;
                fetchArticles(newText, 0, false);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchArticles(String query, int page, final boolean loadMore) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "bb087743efd64040862baad2635ebf29");
        params.put("page", page);
        params.put("q", query);
        if (startDate) {
            String date = sdYear.toString() + sdMonth.toString() + sdDay.toString();
            params.put("begin_date", date);
        }
        if (business) {
            params.put("news_desk", "Business");
        }
        if (cars) {
            params.put("news_desk", "Cars");
        }
        if (dining) {
            params.put("news_desk", "Dining");
        }if (education) {
            params.put("news_desk", "Education");
        }if (sports) {
            params.put("news_desk", "Sports");
        }if (tech) {
            params.put("news_desk", "Technology");
        }if (nyt) {
            params.put("source", "New York Times");
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                JSONArray articleResults = null;
                if(!loadMore) {
                    articles.clear();
                }
                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleResults));
                    Log.d("DEBUG", articles.toString());
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        startDate = true;
        sdDay = day;
        sdMonth = month;
        sdYear = year;
    }
}
