package dto;

import model.Book;
import model.Library;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static Book bookDTO2book( BookDTO BOOKDTO) {
        Book book = new Book();
        String title = new String(BOOKDTO.getName());
        book.setName(title);
        book.setIsbn(BOOKDTO.getIsbn());
        book.setIcon(BOOKDTO.getIcon());
        book.setRiview(BOOKDTO.getRiview());
        return book;
    }
    public static Library libraryDTO2livrary(LibraryDTO libraryDTO) throws NullPointerException{
        List<Book> books = new ArrayList<>();
        List<BookDTO> booksDTO = libraryDTO.getBookDTOS();
        for (BookDTO bookDTO : booksDTO){
            Book book = bookDTO2book(bookDTO);
            books.add(book);
        }
        Library res = new Library(libraryDTO.getName(),books);
        return res;
    }
    public static Book BookDTO2Book(BookDTO bookDTO)throws NullPointerException{
        Book book =new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setIcon(bookDTO.getIcon());
        book.setAuthor(bookDTO.getAuthor());
        book.setRiview(bookDTO.getRiview());
        book.setTags(bookDTO.getTags());
        book.setDescription(bookDTO.getDescription());
        book.setNumberOfPages(bookDTO.getNumberOfPages());
        book.setPublishDate(bookDTO.getPublishDate());

        return book;
    }

    public static List<Library> librarysDTO2livrarys (List<LibraryDTO> libraryDTOS) throws NullPointerException {
        List<Library> libraries = new ArrayList<>();

        for (LibraryDTO libraryDTO : libraryDTOS) {
            List<Book> books = new ArrayList<>();
            List<BookDTO> booksDTO = libraryDTO.getBookDTOS();
            for (BookDTO bookDTO : booksDTO) {
                Book book = BookDTO2Book(bookDTO);
                books.add(book);
            }

            Library library = new Library(libraryDTO.getName(), books);
            library.setId(libraryDTO.getId());
            library.setOpenTime(libraryDTO.getOpenTime());
            library.setCloseTime(libraryDTO.getCloseTime());
            library.setOpenDays(libraryDTO.getOpenDays());

            libraries.add(library);
        }

        return libraries;
    }

    public static Library librarysDTO2livrarysINFO( LibraryDTO libraryDTO) {
        Library library =new Library();

        library.setName(libraryDTO.getName());
        library.setAddress(libraryDTO.getAddress());
        library.setOpenTime(libraryDTO.getOpenTime());
        library.setCloseTime(libraryDTO.getCloseTime());
        library.setOpenDays(libraryDTO.getOpenDays());
        return library;
    }
    public static Book ReviewsDTO2Reviews(BookDTO bookDTO)throws NullPointerException{
        Book book =new Book();
        book.setReviewers(bookDTO.getReviewers());
        return book;
    }
}