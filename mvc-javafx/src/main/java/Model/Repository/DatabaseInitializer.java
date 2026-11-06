package Model.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer
{
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";

    private DatabaseInitializer() {}

    public static void init()
    {
        String[] statements = {
                "CREATE TABLE IF NOT EXISTS Artist (" +
                        "id_artist INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "birth_date TEXT, " +
                        "birth_place TEXT, " +
                        "nationality TEXT, " +
                        "photo TEXT)",

                "CREATE TABLE IF NOT EXISTS Artwork (" +
                        "id_artwork INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "id_artist INTEGER, " +
                        "artwork_type TEXT, " +
                        "price REAL, " +
                        "creation_year INTEGER, " +
                        "FOREIGN KEY (id_artist) REFERENCES Artist(id_artist) ON DELETE CASCADE)",

                "CREATE TABLE IF NOT EXISTS Artwork_Images (" +
                        "id_image INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "id_artwork INTEGER, " +
                        "image_path TEXT, " +
                        "FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork) ON DELETE CASCADE)"
        };

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement())
        {
            for (String sql : statements) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not initialize the database", e);
        }
    }
}