package json;



import dto.BookDTO;
import dto.LibraryDTO;
import model.Reviewer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JsonHandler {

    public static LibraryDTO deSerializeJson2LibraryDTO(String resp) {
        LibraryDTO library = new LibraryDTO();

        try {
            JSONArray array = new JSONArray(resp);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj2 = array.getJSONObject(i);
                BookDTO bookDTO = new BookDTO();
                bookDTO.setName(obj2.getString("title"));
                bookDTO.setIsbn(obj2.getString("isbn"));
                library.addbookDTO(bookDTO);
            }

            return library;
        } catch (Exception ee) {
            return null;
        }


    }

    public static BookDTO _deSerializeJson2BookDTO(String resp) {
        BookDTO bookDTO = new BookDTO();
        try {

            JSONObject mResponseObject = new JSONObject(resp);
            String name = mResponseObject.getString("title");
            bookDTO.setName(name);
            String isbn = mResponseObject.getString("isbn");
            bookDTO.setIsbn(isbn);
            bookDTO.setIconUrl(isbn);
            JSONArray array = mResponseObject.getJSONArray("authors");
            JSONObject obj1 = array.getJSONObject(0);
            bookDTO.setAuthor(obj1.getString("name"));
            String description = mResponseObject.getString("description");
            bookDTO.setDescription(description);

            String publishDate = mResponseObject.getString("publishDate");
            bookDTO.setPublishDate(publishDate);
            String numberOfPages = mResponseObject.getString("numberOfPages");
            bookDTO.setNumberOfPages(numberOfPages);


            if (!mResponseObject.isNull("subjects")) {
                JSONArray subjectsArray = mResponseObject.getJSONArray("subjects");
                List<String> tagsList = new ArrayList<>();
                for (int i = 0; i < Math.min(5, subjectsArray.length()); i++) {
                    tagsList.add(subjectsArray.getString(i));
                }
                bookDTO.setTags(tagsList);
            } else {
                // O campo "subjects" não existe ou é nulo na resposta da API.
                // Definir uma lista vazia para tagsList.
                bookDTO.setTags(new ArrayList<>());
            }
/*
            JSONArray array1 = new JSONArray(resp1);
            JSONObject obj2 = array1.getJSONObject(0);
            bookDTO.setRiview(obj2.getString("review"));
*/
            return bookDTO;
        } catch (Exception ee) {
            return null;
        }

    }

    public static List<LibraryDTO> _deSerializeJson2LibDTO (String resp) {
        List<LibraryDTO> libraries = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resp);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                LibraryDTO library = new LibraryDTO();
                library.setId(obj.getString("id"));
                library.setName(obj.getString("name"));
                library.setOpenTime(obj.getString("openTime"));
                library.setCloseTime(obj.getString("closeTime"));
                library.setOpenDays(obj.getString("openDays"));
                libraries.add(library);
            }
        } catch (Exception ee) {
            return null;
        }

        return libraries;

    }

    public static String _deSerializeID(String resp) {
        String id=new String();
        try {
            JSONArray array1 = new JSONArray(resp);
            JSONObject obj2 = array1.getJSONObject(0);
            id=obj2.getString("id");
                return id;
        } catch (Exception e) {
            return null;
        }


    }


    public static LibraryDTO deSerializeJson2LibraryDTO1(String resp) {
        LibraryDTO library = new LibraryDTO();

        try {
            JSONArray array = new JSONArray(resp);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                JSONObject bookObject = obj.getJSONObject("book");
                BookDTO bookDTO = new BookDTO();
                bookDTO.setName(bookObject.getString("title"));
                bookDTO.setIsbn(bookObject.getString("isbn"));
                // Preencha outros atributos do livro, se necessário, como autor, capa etc.
                library.addbookDTO(bookDTO);
            }

            return library;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static LibraryDTO deSerializeJson2LibraryDTO2(String resp) {
        LibraryDTO libraryDTO = new LibraryDTO();

        try {


            JSONObject mResponseObject = new JSONObject(resp);
            String name = mResponseObject.getString("name");
            libraryDTO.setName(name);
            String address = mResponseObject.getString("address");
            libraryDTO.setAddress(address);
            String openTime = mResponseObject.getString("openTime");
            libraryDTO.setOpenTime(openTime);
            String closeTime = mResponseObject.getString("closeTime");
            libraryDTO.setCloseTime(closeTime);
            String openDays = mResponseObject.getString("openDays");
            libraryDTO.setOpenDays(openDays);

            return libraryDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deSerializeJson2ISBN(String resp) {
        List<String> isbnList = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resp);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String isbn = obj.optString("isbn", "");
                isbnList.add(isbn);
            }

            if (!isbnList.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(isbnList.size());
                return isbnList.get(randomIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    public static BookDTO _deSerializeJson2REVIEWS(String resp) {
        BookDTO bookDTO = new BookDTO();
        List<Reviewer> reviewers = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resp);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Reviewer reviewer = new Reviewer();

                reviewer.setReviewer(obj.getString("reviewer"));
                reviewer.setRecommended(obj.getBoolean("recommended"));
                reviewer.setReview(obj.getString("review"));
                reviewer.setCreatedDate(obj.getString("createdDate"));

                reviewers.add(reviewer);
            }

            bookDTO.setReviewers(reviewers);
            return bookDTO;
        } catch (Exception ee) {
            return null;
        }
    }
    public static Integer _justSeeHowManyReviewshas (String resp) {

        try {
            JSONArray array = new JSONArray(resp);
            return array.length();
        } catch (Exception ee) {
            return null;
        }


    }
}
