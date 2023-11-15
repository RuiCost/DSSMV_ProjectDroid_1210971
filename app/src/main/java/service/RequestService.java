package service;

import android.graphics.Bitmap;
import dto.BookDTO;
import dto.LibraryDTO;
import json.JsonHandler;
import model.Book;
import model.Library;
import network.HttpOperation;
import dto.Mapper;

import java.util.List;



public class RequestService {
    private static void getBitmapIcons(BookDTO bookDTO) {
            if (bookDTO.getIcon() == null) {
                Bitmap icon = HttpOperation.getIcon(bookDTO.getIconUrl());
                bookDTO.setIcon(icon);
            }
    }
    private static LibraryDTO _getLibraryBook (String urlStr){
        LibraryDTO library =null;
        try{

            String jsonString = HttpOperation.get(urlStr);
            library = JsonHandler.deSerializeJson2LibraryDTO(jsonString);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return library;
    }
    private static BookDTO _getBookL (String urlStr){
        BookDTO bookDTO=null;
        try{

            String jsonString = HttpOperation.get(urlStr);
            bookDTO = JsonHandler._deSerializeJson2BookDTO(jsonString);

            if(bookDTO !=null) {
                getBitmapIcons(bookDTO);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return bookDTO;
    }

    public static Library getLibraryBook(String urlStr) {
        LibraryDTO libraryDTO = _getLibraryBook(urlStr);
        Library library= Mapper.libraryDTO2livrary(libraryDTO);
        return library;
    }
    public static Book getBookL(String urlStr) {
        BookDTO bookDTODTO = _getBookL(urlStr);
        Book book= Mapper.BookDTO2Book(bookDTODTO);
        return book;
    }

    public static void addBookToLibrary(String urlStr, String stock){
            HttpOperation.post(urlStr, "{\n" +
                    " \"stock\":  \"" + stock + "\"\n" +
                    "}");

    }



    ////////////////
    public static List<Library> getLibrarys(String urlStr) {
        List<LibraryDTO> libraryDTO = _getLibrarys(urlStr);
        List<Library> library= Mapper.librarysDTO2livrarys(libraryDTO);
        return library;
    }
    private static  List<LibraryDTO>  _getLibrarys (String urlStr){
        List<LibraryDTO>  libraryDTO = null;
        try{

            String jsonString = HttpOperation.get(urlStr);
            libraryDTO = JsonHandler._deSerializeJson2LibDTO(jsonString);



        }catch (Exception e) {
            e.printStackTrace();
        }
        return libraryDTO;
    }



    ///////////////////////



    public static void postReview(String url, String review,String recommended) {
            HttpOperation.post(url, "{\n" +
                    "  \"recommended\": \"" +recommended+"\",\n" +
                    "  \"review\": \"" + review + "\"\n" +
                    "}");

    }
    public static String getID(String url){
        String ID=new String();
        String jsonString = HttpOperation.get(url);
        ID = JsonHandler._deSerializeID(jsonString);
        return ID;
    }
    public static void putReview (String url,String review,String recommended){
        HttpOperation.put(url, "{\n" +
                "  \"recommended\": true,\n" +
                "  \"review\": \""+review+"\"\n" +
                "}");
    }



    public static Library getBooksFromONELibrary(String url) {
        LibraryDTO libraryDTO = _getLibraryWithBooks(url);
        if (libraryDTO != null) {
            Library library = Mapper.libraryDTO2livrary(libraryDTO);
            return library;
        } else {
            // Lida com o caso de erro ao obter a Library ou LibraryDTO nula, se necessário
            return null;
        }
    }

    private static  LibraryDTO  _getLibraryWithBooks(String url) {
        LibraryDTO library =null;
        try{

            String jsonString = HttpOperation.get(url);
            library = JsonHandler.deSerializeJson2LibraryDTO1(jsonString);

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
        }
        return library;
    }

    public static Library getLibraryInfo(String url) {
        LibraryDTO libraryDTO = _getLibraryInformation(url);
        Library library= Mapper.librarysDTO2livrarysINFO(libraryDTO);
        return library;
    }

    private static  LibraryDTO  _getLibraryInformation(String url) {
        LibraryDTO library =null;
        try{

            String jsonString = HttpOperation.get(url);
            library = JsonHandler.deSerializeJson2LibraryDTO2(jsonString);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return library;
    }




    public static String get_RANDOM_ISBN(String url) {
        String ISBN = _getRANDOM_BOOOK(url);
        return ISBN;
    }

    private static  String  _getRANDOM_BOOOK(String url) {
        String ISBN = null;
        try{

            String jsonString = HttpOperation.get(url);
            ISBN = JsonHandler.deSerializeJson2ISBN(jsonString);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return ISBN;
    }

    public static Book getReviewsNOW(String url) {
        BookDTO bookDTODTO = _getReviewsNOW(url);
        Book book= Mapper.ReviewsDTO2Reviews(bookDTODTO);
        return book;
    }
    private static BookDTO _getReviewsNOW (String urlStr){
        BookDTO bookDTO=null;
        try{

            String jsonString = HttpOperation.get(urlStr);
            bookDTO = JsonHandler._deSerializeJson2REVIEWS(jsonString);


        }catch (Exception e) {
            e.printStackTrace();
        }
        return bookDTO;
    }

    public static Integer getNumOfReviews(String url) {
        int x = 0;
        try{

            String jsonString = HttpOperation.get(url);
            x = JsonHandler._justSeeHowManyReviewshas(jsonString);


        }catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }


    public static void addLIB_TO_API(String urlStr,String address,String closeTime,String name,String openDays,String openTime) {
        String requestBody = "{\n" +
                "  \"address\": \""+address+"\",\n" +
                "  \"closeTime\": \""+closeTime+"\",\n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"openDays\": \""+openDays+"\",\n" +
                "  \"openTime\": \""+openTime+"\"\n" +
                "}";


        HttpOperation.post(urlStr, requestBody);


    }
}
