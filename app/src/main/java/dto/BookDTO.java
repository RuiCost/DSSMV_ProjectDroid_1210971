package dto;

import android.graphics.Bitmap;
import model.Reviewer;

import java.util.List;

public class BookDTO {

    private String publishDate;

    private String numberOfPages;
    private String description;
    private String riview;
    private String name;
    private String author;
    private String isbn;
    private String iconUrl;
    private Bitmap icon;

    private List<String> tags;
    private List<Reviewer> reviewers;
    public String getStock() {return stock;}

    public void setStock(String stock) {this.stock = stock;}

    private String stock;
    public BookDTO(){}

    public String getName() {return this.name;}
    public void setName(String name) {this.name=name;}

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {return this.author;}

    public String getRiview() {
        return riview;
    }

    public void setRiview(String riview) {
        this.riview = riview;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl="http://193.136.62.24/v1/assets/cover/"+iconUrl+"-L.jpg";
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public Bitmap getIcon(){
        return icon;
    }
    public void setIcon(Bitmap icon){
        this.icon = icon;
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
