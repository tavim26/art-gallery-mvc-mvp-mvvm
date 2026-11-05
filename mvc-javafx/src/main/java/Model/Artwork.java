package Model;

import java.util.ArrayList;
import java.util.List;

public class Artwork {

    private String title;
    private Artist artist;
    private String type;
    private double price;
    private int creationYear;
    private List<ArtworkImage> images;



    public Artwork(String title, Artist artist, String type, double price, int creationYear) {
        this.title = title;
        this.artist = artist;
        this.type = type;
        this.price = price;
        this.creationYear = creationYear;
        this.images = new ArrayList<>();
    }


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Artist getArtist() { return artist; }

    public void setArtist(Artist artist) {
        this.artist = artist;
        if (!artist.getArtworks().contains(this)) {
            artist.addArtwork(this);
        }
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCreationYear() { return creationYear; }
    public void setCreationYear(int creationYear) { this.creationYear = creationYear; }

    public List<ArtworkImage> getImages() { return images; }
    public void setImages(List<ArtworkImage> images) { this.images = images; }
}
