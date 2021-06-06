package org.meicode.appfilm.Models;

import java.util.List;

public class MovieCategory {
    String categoryTitle;
    Integer categoryId;
    private List<Movie> movieOfCateList;

    public MovieCategory(Integer categoryId, String categoryTitle, List<Movie> movieOfCateList) {
        this.categoryTitle = categoryTitle;
        this.categoryId = categoryId;
        this.movieOfCateList = movieOfCateList;
    }

    public List<Movie> getCateItemm() {
        return movieOfCateList;
    }

    public void setCateItemm(List<Movie> cateItemm) {
        movieOfCateList = cateItemm;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
