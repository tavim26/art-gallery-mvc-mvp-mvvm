package Model.Repository;

import Model.Artist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepo {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";

    public void addArtist(Artist artist) throws SQLException {
        String sql = "INSERT INTO Artist (name, birth_date, birth_place, nationality, photo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getBirthDate());
            stmt.setString(3, artist.getBirthPlace());
            stmt.setString(4, artist.getNationality());
            stmt.setString(5, artist.getPhoto());
            stmt.executeUpdate();
        }
    }

    public List<Artist> getAllArtists() throws SQLException {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                artists.add(new Artist(
                        rs.getString("name"),
                        rs.getString("birth_date"),
                        rs.getString("birth_place"),
                        rs.getString("nationality"),
                        rs.getString("photo")
                ));
            }
        }
        return artists;
    }

    public void deleteArtist(String name) throws SQLException {
        String sql = "DELETE FROM Artist WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public void updateArtist(String oldName, Artist updatedArtist) throws SQLException {
        String sql = "UPDATE Artist SET name = ?, birth_date = ?, birth_place = ?, nationality = ?, photo = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedArtist.getName());
            stmt.setString(2, updatedArtist.getBirthDate());
            stmt.setString(3, updatedArtist.getBirthPlace());
            stmt.setString(4, updatedArtist.getNationality());
            stmt.setString(5, updatedArtist.getPhoto());
            stmt.setString(6, oldName);
            stmt.executeUpdate();
        }
    }

    public Artist findArtistByName(String name) throws SQLException {
        String sql = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Artist(
                        rs.getString("name"),
                        rs.getString("birth_date"),
                        rs.getString("birth_place"),
                        rs.getString("nationality"),
                        rs.getString("photo")
                );
            }
            return null;
        }
    }


    public List<Artist> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM Artist WHERE name LIKE ?";
        List<Artist> artists = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                artists.add(new Artist(rs.getString("name"), rs.getString("birth_date"),
                        rs.getString("birth_place"), rs.getString("nationality"),
                        rs.getString("photo")));
            }
        }
        return artists;
    }



}