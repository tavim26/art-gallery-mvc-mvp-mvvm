package Model.Repository;

import Model.Artwork;
import Model.Artist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtworkRepo {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";

    private ArtistRepo artistRepo;


    public ArtworkRepo() {
        this.artistRepo = new ArtistRepo();
    }

    public void addArtwork(Artwork artwork, String artistName) throws SQLException {
        String sql = "INSERT INTO Artwork (title, id_artist, artwork_type, price, creation_year) " +
                "VALUES (?, (SELECT id_artist FROM Artist WHERE name = ?), ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artistName);
            stmt.setString(3, artwork.getType());
            stmt.setDouble(4, artwork.getPrice());
            stmt.setInt(5, artwork.getCreationYear());
            stmt.executeUpdate();
        }
    }

    public List<Artwork> getAllArtworks() throws SQLException {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                artworks.add(new Artwork(
                        rs.getString("title"),
                        new Artist(rs.getString("artist_name"), "", "", "", ""),
                        rs.getString("artwork_type"),
                        rs.getDouble("price"),
                        rs.getInt("creation_year")
                ));
            }
        }
        return artworks;
    }

    public void deleteArtwork(String title) throws SQLException {
        String sql = "DELETE FROM Artwork WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        }
    }

    public void updateArtwork(String oldTitle, Artwork updatedArtwork, String artistName) throws SQLException {
        String sql = "UPDATE Artwork SET title = ?, id_artist = (SELECT id_artist FROM Artist WHERE name = ?), " +
                "artwork_type = ?, price = ?, creation_year = ? WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedArtwork.getTitle());
            stmt.setString(2, artistName);
            stmt.setString(3, updatedArtwork.getType());
            stmt.setDouble(4, updatedArtwork.getPrice());
            stmt.setInt(5, updatedArtwork.getCreationYear());
            stmt.setString(6, oldTitle);
            stmt.executeUpdate();
        }
    }

    public Artwork findArtworkByTitle(String title) throws SQLException {
        String sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE a.title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Artwork(rs.getString("title"),
                        new Artist(rs.getString("artist_name"), "", "", "", ""),
                        rs.getString("artwork_type"), rs.getDouble("price"),
                        rs.getInt("creation_year"));
            }
            return null;
        }
    }





    public List<String> getArtworksByArtist(String artistName) throws SQLException {
        String sql = "SELECT a.title " +
                "FROM Artwork a " +
                "JOIN Artist ar ON a.id_artist = ar.id_artist " +
                "WHERE ar.name = ?";

        List<String> artworkTitles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artistName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                artworkTitles.add(rs.getString("title"));
            }
        }
        return artworkTitles;
    }

    public List<Artwork> searchByTitle(String title) throws SQLException {
        String sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE a.title LIKE ?";
        List<Artwork> artworks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Artist artist = artistRepo.findArtistByName(rs.getString("artist_name"));
                artworks.add(new Artwork(
                        rs.getString("title"),
                        artist,
                        rs.getString("artwork_type"),
                        rs.getDouble("price"),
                        rs.getInt("creation_year")
                ));
            }
        }
        return artworks;
    }
}