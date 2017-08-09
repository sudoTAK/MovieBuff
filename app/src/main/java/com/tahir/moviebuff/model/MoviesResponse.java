package com.tahir.moviebuff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movies> results = new ArrayList<Movies>();
    @SerializedName("total_results")
    @Expose
    private Integer totalMoviess;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Movies> getMovies() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setMoviess(List<Movies> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalMoviess
     */
    public Integer getTotalMoviess() {
        return totalMoviess;
    }

    /**
     *
     * @param totalMoviess
     * The total_results
     */
    public void setTotalMoviess(Integer totalMoviess) {
        this.totalMoviess = totalMoviess;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
