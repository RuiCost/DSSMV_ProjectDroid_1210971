package dto;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class LibraryDTO {
    private String id;

    private String name;

    private String openTime;
    private String closeTime;
    private String  openDays;
    private String  address;
    private List<BookDTO> bookDTOS;
    public LibraryDTO() {
        this.name = "No Book";
        bookDTOS=new ArrayList<BookDTO>();
    }

    public void addbookDTO(BookDTO bookDTO){this.bookDTOS.add(bookDTO);}
    public List<BookDTO> getBookDTOS(){return bookDTOS;}

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOpenDays() {
        return openDays;
    }

    public void setOpenDays(String openDays) {
        this.openDays = openDays;
    }

    public void setBookIcon(String urlIcon, Bitmap icon){
        for (BookDTO bs : bookDTOS) {
            if (bs.getIconUrl().equals(urlIcon)){
                bs.setIcon(icon);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
