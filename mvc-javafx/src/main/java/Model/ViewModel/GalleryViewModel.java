package Model.ViewModel;

import Model.Artist;
import Model.Artwork;
import Model.ArtworkImage;
import Model.Observer.Observable;
import Model.Repository.ArtistRepo;
import Model.Repository.ArtworkRepo;
import Model.Repository.ArtworkImageRepo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryViewModel extends Observable {

    private List<Artist> artists;
    private List<Artwork> artworks;
    private List<ArtworkImage> artworkImages;

    private final ArtistRepo artistRepo;
    private final ArtworkRepo artworkRepo;
    private final ArtworkImageRepo artworkImageRepo;

    public GalleryViewModel() {
        artistRepo = new ArtistRepo();
        artworkRepo = new ArtworkRepo();
        artworkImageRepo = new ArtworkImageRepo();
        loadData();
    }

    private void loadData() {
        try {
            artists = artistRepo.getAllArtists();
            artworks = artworkRepo.getAllArtworks();
        } catch (SQLException e) {
            e.printStackTrace();
            artists = new ArrayList<>();
            artworks = new ArrayList<>();
        }
        notifyObservers();
    }

    // === Public GETTERS ===

    public List<String[]> getArtist() {
        List<String[]> data = new ArrayList<>();
        for (Artist a : artists) {
            data.add(new String[]{
                    a.getName(),
                    a.getBirthDate(),
                    a.getBirthPlace(),
                    a.getNationality(),
                    a.getPhoto()
            });
        }
        return data;
    }

    public List<String[]> getArtwork() {
        List<String[]> data = new ArrayList<>();
        for (Artwork aw : artworks) {
            data.add(new String[]{
                    aw.getTitle(),
                    aw.getArtist().getName(),
                    aw.getType(),
                    String.valueOf(aw.getPrice()),
                    String.valueOf(aw.getCreationYear())
            });
        }
        return data;
    }

    public List<String> getArtistNames() {
        return artists.stream().map(Artist::getName).toList();
    }

    public List<String[]> searchArtist(String keyword) {
        try {
            List<Artist> result = artistRepo.searchByName(keyword);
            List<String[]> data = new ArrayList<>();
            for (Artist a : result) {
                data.add(new String[]{
                        a.getName(),
                        a.getBirthDate(),
                        a.getBirthPlace(),
                        a.getNationality(),
                        a.getPhoto()
                });
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String[]> searchArtwork(String keyword) {
        try {
            List<Artwork> result = artworkRepo.searchByTitle(keyword);
            List<String[]> data = new ArrayList<>();
            for (Artwork aw : result) {
                data.add(new String[]{
                        aw.getTitle(),
                        aw.getArtist().getName(),
                        aw.getType(),
                        String.valueOf(aw.getPrice()),
                        String.valueOf(aw.getCreationYear())
                });
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String[]> filterArtworkByType(String type) {
        return artworks.stream()
                .filter(a -> a.getType().equalsIgnoreCase(type))
                .map(a -> new String[]{
                        a.getTitle(),
                        a.getArtist().getName(),
                        a.getType(),
                        String.valueOf(a.getPrice()),
                        String.valueOf(a.getCreationYear())
                }).toList();
    }

    public List<String[]> filterArtworkByArtist(String artistName) {
        return artworks.stream()
                .filter(a -> a.getArtist().getName().equalsIgnoreCase(artistName))
                .map(a -> new String[]{
                        a.getTitle(),
                        a.getArtist().getName(),
                        a.getType(),
                        String.valueOf(a.getPrice()),
                        String.valueOf(a.getCreationYear())
                }).toList();
    }

    public List<String[]> filterArtworkByPrice(double maxPrice) {
        return artworks.stream()
                .filter(a -> a.getPrice() <= maxPrice)
                .map(a -> new String[]{
                        a.getTitle(),
                        a.getArtist().getName(),
                        a.getType(),
                        String.valueOf(a.getPrice()),
                        String.valueOf(a.getCreationYear())
                }).toList();
    }

    // === ARTIST CRUD ===

    public void addArtist(String name, String birthDate, String birthPlace, String nationality, String photo) {
        try {
            artistRepo.addArtist(new Artist(name, birthDate, birthPlace, nationality, photo));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void updateArtist(String oldName, String name, String birthDate, String birthPlace, String nationality)
    {
        try {
            artistRepo.updateArtist(oldName, new Artist(name, birthDate, birthPlace, nationality,""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void deleteArtist(String name) {
        try {
            artistRepo.deleteArtist(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    // === ARTWORK CRUD ===

    public void addArtwork(String title, String artistName, String type, double price, int year) {
        try {
            Artist artist = artistRepo.findArtistByName(artistName);
            if (artist != null) {
                artworkRepo.addArtwork(new Artwork(title, artist, type, price, year), artistName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void updateArtwork(String oldTitle, String title, String artistName, String type, double price, int year) {
        try {
            Artist artist = artistRepo.findArtistByName(artistName);
            if (artist != null) {
                Artwork updated = new Artwork(title, artist, type, price, year);
                artworkRepo.updateArtwork(oldTitle, updated, artistName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public void deleteArtwork(String title) {
        try {
            artworkRepo.deleteArtwork(title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    // === IMAGE ===

    public void addImageToArtwork(String artworkTitle, String imagePath) {
        try {
            ArtworkImage image = new ArtworkImage(imagePath, null);
            artworkImageRepo.addImage(artworkTitle, image);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadData();
    }

    public List<String> getArtworkImagePaths(String artworkTitle) {
        try {
            List<ArtworkImage> images = artworkImageRepo.getImagesByArtwork(artworkTitle);
            return images.stream().map(ArtworkImage::getImagePath).toList();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // === EXPORT ===

    public void saveToCsv(String path) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("Title,Artist,Type,Price,Year");
            for (Artwork aw : artworks) {
                writer.printf("%s,%s,%s,%.2f,%d%n",
                        aw.getTitle(),
                        aw.getArtist().getName(),
                        aw.getType(),
                        aw.getPrice(),
                        aw.getCreationYear()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToDoc(String path) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            for (Artwork aw : artworks) {
                writer.println("Title: " + aw.getTitle());
                writer.println("Artist: " + aw.getArtist().getName());
                writer.println("Type: " + aw.getType());
                writer.println("Price: " + aw.getPrice());
                writer.println("Year: " + aw.getCreationYear());
                writer.println("-----");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public Map<String, Integer> getArtworkCountsByType()
    {
        Map<String, Integer> result = new HashMap<>();

        for (Artwork a : artworks)
        {
            result.put(a.getType(), result.getOrDefault(a.getType(), 0) + 1);
        }
        return result;
    }

    public Map<String, Integer> getArtworkCountsByArtist()
    {
        Map<String, Integer> result = new HashMap<>();

        for (Artwork a : artworks)
        {
            String artistName = a.getArtist().getName();
            result.put(artistName, result.getOrDefault(artistName, 0) + 1);
        }
        return result;
    }

}
