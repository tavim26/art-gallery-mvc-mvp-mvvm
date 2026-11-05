package Presenter;

import java.time.LocalDate;
import java.util.List;

public interface IGalleryGUI {

    // Artist input
    String getArtistName();
    String getArtistBirthplace();
    String getArtistNationality();
    LocalDate getArtistBirthday();
    String getArtistPhoto();

    // Artwork input
    String getArtworkTitle();
    String getArtworkArtist();
    String getArtworkType();
    String getArtworkPrice();
    String getArtworkYear();

    // Display/update methods
    void displayArtists(List<String> artistData);
    void displaySuccess(String message);
    void displayArtworks(List<String> artworkData);
    void displayFilteredArtworks(List<String> artworkData);

    void populateArtistFields(String name, String birthDate, String birthPlace, String nationality, String photo);
    void populateArtworkFields(String title, String artistName, String type, String price, String year);
    void confirmExportSuccess(String filePath);
    void confirmSuccess(String message);
    void showError(String message);
    void clearArtistFields();
    void clearArtworkFields();
    void displayArtworkImages(List<String> imagePaths);

    void setArtistComboBoxItems(List<String> artistNames);
    void setFilterByArtistBoxItems(List<String> artistNames);
    void setFilterByTypeBoxItems(List<String> types);
    void setTypeComboBoxItems(List<String> types);
}