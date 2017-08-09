package com.tahir.moviebuff.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tahir.moviebuff.BuildConfig;
import com.tahir.moviebuff.R;
import com.tahir.moviebuff.activities.MainActivity;
import com.tahir.moviebuff.activities.MovieDetailActivity;
import com.tahir.moviebuff.adapters.MovieAdapter;
import com.tahir.moviebuff.model.Movies;
import com.tahir.moviebuff.model.MoviesResponse;
import com.tahir.moviebuff.rest.ApiClient;
import com.tahir.moviebuff.rest.ApiInterface;
import com.tahir.moviebuff.util.CommonUtil;
import com.tahir.moviebuff.util.Network;
import com.tahir.moviebuff.util.PaginationListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tahir.moviebuff.activities.MovieDetailActivity.BUNDLE_MOVIE;
import static com.tahir.moviebuff.util.Constants.filterSet;
import static com.tahir.moviebuff.util.Constants.fragNowPlaying;
import static com.tahir.moviebuff.util.Constants.fragPopular;
import static com.tahir.moviebuff.util.Constants.fragTopRated;
import static com.tahir.moviebuff.util.Constants.fragUpcoming;


public class CommonFragment extends Fragment implements MovieAdapter.HolderItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    MovieAdapter movieAdapter;
    LinearLayoutManager linearLayoutManager;

    public boolean isLoading = false;
    public boolean isLastPage = false;

    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private int TOTAL_PAGES = 5;


    ApiInterface apiService;

    String tagName;
    Call<MoviesResponse> moviesResponseCall;


    public CommonFragment() {
        // Required empty public constructor
    }

    public static CommonFragment newInstance() {
        CommonFragment fragment = new CommonFragment();
        return fragment;
    }

    public static CommonFragment newInstance(String param1, String param2) {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        tagName = this.getTag();

        if (tagName != null && tagName.equalsIgnoreCase(fragNowPlaying))
            ((MainActivity) getActivity()).setTitle(getString(R.string.tab_now_playing));

        else if (tagName != null && tagName.equalsIgnoreCase(fragTopRated))
            ((MainActivity) getActivity()).setTitle(getString(R.string.tab_top_rated));

        else if (tagName != null && tagName.equalsIgnoreCase(fragUpcoming))
            ((MainActivity) getActivity()).setTitle(getString(R.string.tab_upcoming));

        else if (tagName != null && tagName.equalsIgnoreCase(fragPopular))
            ((MainActivity) getActivity()).setTitle(getString(R.string.tab_popular));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_now_playing, container, false);
        ButterKnife.bind(this, v);
        movieAdapter = new MovieAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnHolderItemClickListener(this);

        apiService = ApiClient.getClient(getActivity().getApplication()).create(ApiInterface.class);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public int getCurrentPage() {
                return currentPage;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {

        if (!Network.isConnected(getActivity())) {
            ((MainActivity) getActivity()).showMsg("No Internet Found");
            return;
        }

        if (tagName != null && tagName.equalsIgnoreCase(fragNowPlaying)) {
            if (!filterSet)
                moviesResponseCall = apiService.getNowPlaying(BuildConfig.TMDB_API_KEY, currentPage);
            else {
                String[] yearFilters = CommonUtil.getMinMaxYear(getActivity());
                moviesResponseCall = apiService.getFilteredMovies(BuildConfig.TMDB_API_KEY, currentPage, yearFilters[0], yearFilters[1]);
            }
        } else if (tagName != null && tagName.equalsIgnoreCase(fragTopRated))
            moviesResponseCall = apiService.getTopRated(BuildConfig.TMDB_API_KEY, currentPage);

        else if (tagName != null && tagName.equalsIgnoreCase(fragUpcoming))
            moviesResponseCall = apiService.getUpcoming(BuildConfig.TMDB_API_KEY, currentPage);

        else if (tagName != null && tagName.equalsIgnoreCase(fragPopular))
            moviesResponseCall = apiService.getPopular(BuildConfig.TMDB_API_KEY, currentPage);

        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                List<Movies> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                movieAdapter.addAll(results);
                TOTAL_PAGES = response.body().getTotalPages();

                if (currentPage <= TOTAL_PAGES) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }

    private void loadNextPage() {
        if (!Network.isConnected(getActivity())) {
            ((MainActivity) getActivity()).showMsg("No Internet Found");
            return;
        }
        //      Log.d(TAG, "loadNextPage: " + currentPage);
        if (tagName != null && tagName.equalsIgnoreCase(fragNowPlaying)) {
            if (!filterSet)
                moviesResponseCall = apiService.getNowPlaying(BuildConfig.TMDB_API_KEY, currentPage);
            else {
                String[] yearFilters = CommonUtil.getMinMaxYear(getActivity());
                moviesResponseCall = apiService.getFilteredMovies(BuildConfig.TMDB_API_KEY, currentPage, yearFilters[0] + "-01-01", yearFilters[1] + "-12-31");
            }
        } else if (tagName != null && tagName.equalsIgnoreCase(fragTopRated))
            moviesResponseCall = apiService.getTopRated(BuildConfig.TMDB_API_KEY, currentPage);

        else if (tagName != null && tagName.equalsIgnoreCase(fragUpcoming))
            moviesResponseCall = apiService.getUpcoming(BuildConfig.TMDB_API_KEY, currentPage);

        else if (tagName != null && tagName.equalsIgnoreCase(fragPopular))
            moviesResponseCall = apiService.getPopular(BuildConfig.TMDB_API_KEY, currentPage);

        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movieAdapter.removeLoadingFooter();
                isLoading = false;

                List<Movies> movies = fetchResults(response);
                movieAdapter.addAll(movies);

                if (currentPage != TOTAL_PAGES) movieAdapter.addLoadingFooter();
                else isLastPage = true;

                String msg = "Fetched page " + currentPage + " of " + TOTAL_PAGES;
                ((MainActivity) getActivity()).showMsg(msg);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private List<Movies> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        return topRatedMovies.getMovies();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        Movies movies = movieAdapter.getItem(position);
        intent.putExtra(BUNDLE_MOVIE, movies);
        getActivity().startActivity(intent);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = 1;
    }
}
