package Model;

public class ArtworkImage
{

    private String imagePath;
    private Artwork artwork;

    public ArtworkImage() {}

    public ArtworkImage(String imagePath, Artwork artwork) {
        this.imagePath = imagePath;
        this.artwork = artwork;
    }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }
}

