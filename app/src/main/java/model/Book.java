package model;

import android.graphics.Bitmap;

import java.util.List;

public class Book {

    private String publishDate;

    private String numberOfPages;
    private String description;
    private String riview;
    private String name;
    private String author;
    private String isbn;
    private Bitmap icon;
    private int stock;
    private List<String> tags;

    private List<Reviewer> reviewers;

    public Book(){}
    public Book(String name)
    {
        this.name=name;

    }


    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {return this.author;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name=name;}
    public String getIsbn() {
        return isbn;
    }
    public void setIcon(Bitmap icon){this.icon=icon;}
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public Bitmap getIcon(){
        return this.icon;
    }

    public String getRiview() {
        return riview;
    }

    public void setRiview(String riview) {
        this.riview = riview;
    }



    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }
}
