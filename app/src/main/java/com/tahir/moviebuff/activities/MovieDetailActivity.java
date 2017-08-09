package com.tahir.moviebuff.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.tahir.moviebuff.R;
import com.tahir.moviebuff.model.Movies;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.tahir.moviebuff.adapters.MovieAdapter.BASE_URL_IMG;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String BUNDLE_MOVIE = "movie";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Movies mMovie;

    @BindView(R.id.movie_poster)
    ImageView posterImg;


    @BindView(R.id.movie_year)
    TextView movieYear;

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_desc)
    TextView movieDesc;

    @BindView(R.id.movie_progress)
    ProgressBar progressBar;

    Typeface DINRountOT_Lite;
    Typeface DINRountOT_Medium;
    Typeface DINRountOT_Bold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        progressBar.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra(BUNDLE_MOVIE)) {
            mMovie = (Movies) getIntent().getSerializableExtra(BUNDLE_MOVIE);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }


        DINRountOT_Lite = Typeface.createFromAsset(getAssets(), "DINRoundOT-Light.ttf");
        DINRountOT_Medium = Typeface.createFromAsset(getAssets(), "DINRoundOT-Medium.ttf");
        DINRountOT_Bold = Typeface.createFromAsset(getAssets(), "DINRoundOT-Bold.ttf");

        movieTitle.setTypeface(DINRountOT_Bold);
        movieDesc.setTypeface(DINRountOT_Lite);
        movieYear.setTypeface(DINRountOT_Medium);

        setTitle(mMovie.getTitle());

        movieYear.setText(
                "Rating " + mMovie.getVoteAverage() + " | " +
                        mMovie.getReleaseDate().substring(0, 4)
                        + " | "
                        + mMovie.getOriginalLanguage().toUpperCase()
                        + ((mMovie.getAdult()) ? " | Adult" : "")
        );

        movieTitle.setText(mMovie.getTitle());

        movieDesc.setText(mMovie.getOverview());

        Glide.with(this)
                .load(BASE_URL_IMG + mMovie.getPosterPath())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(posterImg);

    }

}
