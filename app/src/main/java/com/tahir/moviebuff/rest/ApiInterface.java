package com.tahir.moviebuff.rest;

import com.tahir.moviebuff.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlaying(@Query("api_key") String apiKey,
                                       @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcoming(@Query("api_key") String apiKey,
                                     @Query("page") int page
    );

    @GET("movie/popular")
    Call<MoviesResponse> getPopular(@Query("api_key") String apiKey,
                                    @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRated(@Query("api_key") String apiKey,
                                     @Query("page") int page
    );

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getFilteredMovies(@Query("api_key") String apiKey,
                                           @Query("page") int page,
                                           @Query("primary_release_date.gte") String releaseDateGte,
                                           @Query("primary_release_date.lte") String releaseDateLte
    );


}
