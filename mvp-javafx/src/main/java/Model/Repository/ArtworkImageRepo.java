package Model.Repository;


import Model.ArtworkImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtworkImageRepo
{
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";


    public void addImage(String artworkTitle, ArtworkImage image) throws SQLException
    {
        String sql = "INSERT INTO Artwork_Images (id_artwork, image_path) " +
                "VALUES ((SELECT id_artwork FROM Artwork WHERE title = ?), ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artworkTitle);
            stmt.setString(2, image.getImagePath());
            stmt.executeUpdate();
        }
    }


    public List<ArtworkImage> getImagesByArtwork(String artworkTitle) throws SQLException
    {
        List<ArtworkImage> images = new ArrayList<>();
        String sql = "SELECT ai.image_path FROM Artwork_Images ai " +
                "JOIN Artwork a ON ai.id_artwork = a.id_artwork WHERE a.title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artworkTitle);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                images.add(new ArtworkImage(rs.getString("image_path"), null));
            }
        }
        return images;
    }
}

