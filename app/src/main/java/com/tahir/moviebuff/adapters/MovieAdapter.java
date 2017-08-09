package com.tahir.moviebuff.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tahir.moviebuff.R;
import com.tahir.moviebuff.model.Movies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    public static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";

    private List<Movies> movieMoviess;
    private Context context;

    private boolean isLoadingAdded = false;
    Typeface DINRountOT_Lite;
    Typeface DINRountOT_Medium;
    Typeface DINRountOT_Bold;

    private boolean isVerticle;


    public MovieAdapter(Context context) {
        this.context = context;
        movieMoviess = new ArrayList<>();

        DINRountOT_Lite = Typeface.createFromAsset(context.getAssets(), "DINRoundOT-Light.ttf");
        DINRountOT_Medium = Typeface.createFromAsset(context.getAssets(), "DINRoundOT-Medium.ttf");
        DINRountOT_Bold = Typeface.createFromAsset(context.getAssets(), "DINRoundOT-Bold.ttf");
        switch (context.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                isVerticle = true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                isVerticle = false;
                break;
        }

    }

    public List<Movies> getMoviess() {
        return movieMoviess;
    }

    public void setMoviess(List<Movies> movieMoviess) {
        this.movieMoviess = movieMoviess;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.card_item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = (isVerticle) ? inflater.inflate(R.layout.card_item_movies_2, parent, false) :
                inflater.inflate(R.layout.card_item_movies, parent, false);
        viewHolder = new MoviesVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movies movie = movieMoviess.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final MoviesVH movieVH = (MoviesVH) holder;

                movieVH.clickListener = onHolderItemClickListener;

                movieVH.mMoviesTitle.setText(movie.getTitle());


                movieVH.mYear.setText(
                        "Rating " + movie.getVoteAverage() + " | " +
                                movie.getReleaseDate().substring(0, 4)
                                + " | "
                                + movie.getOriginalLanguage().toUpperCase()
                );
                movieVH.mMoviesDesc.setText(movie.getOverview());

                Glide.with(context)
                        .load(BASE_URL_IMG + movie.getPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .crossFade()
                        .into(movieVH.mPosterImg);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieMoviess == null ? 0 : movieMoviess.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieMoviess.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Movies movies) {
        movieMoviess.add(movies);
        notifyItemInserted(movieMoviess.size() - 1);
    }

    public void addAll(List<Movies> moveMoviess) {
        for (Movies movies : moveMoviess) {
            add(movies);
        }
    }

    public void remove(Movies movies) {
        int position = movieMoviess.indexOf(movies);
        if (position > -1) {
            movieMoviess.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movies());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieMoviess.size() - 1;
        Movies result = getItem(position);

        if (result != null) {
            movieMoviess.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movies getItem(int position) {
        return movieMoviess.get(position);
    }


    protected class MoviesVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMoviesTitle;
        private TextView mMoviesDesc;
        private TextView mYear;
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        HolderItemClickListener clickListener;

        public MoviesVH(View itemView) {
            super(itemView);

            mMoviesTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMoviesTitle.setTypeface(DINRountOT_Bold);
            mMoviesDesc = (TextView) itemView.findViewById(R.id.movie_desc);
            mMoviesDesc.setTypeface(DINRountOT_Lite);
            mYear = (TextView) itemView.findViewById(R.id.movie_year);
            mYear.setTypeface(DINRountOT_Medium);
            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public interface HolderItemClickListener {
        public void onItemClick(View view, int position);
    }

    HolderItemClickListener onHolderItemClickListener;

    public HolderItemClickListener getOnHolderItemClickListener() {
        return onHolderItemClickListener;
    }

    public void setOnHolderItemClickListener(HolderItemClickListener onHolderItemClickListener) {
        this.onHolderItemClickListener = onHolderItemClickListener;
    }


}
