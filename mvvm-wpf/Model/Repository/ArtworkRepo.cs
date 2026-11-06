using System.Data.SqlClient;
using AssignmentMVVM.Model;

namespace AssignmentMVVM.Repository
{
    public class ArtworkRepo
    {
        private static readonly string DB_URL = "Data Source=(LocalDB)\\MSSQLLocalDB;Initial Catalog=art_gallery;Integrated Security=True;";

        private const string BaseSelect =
            "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
            "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist";

        private static Artwork Map(SqlDataReader rs)
        {
            return new Artwork(
                rs.GetString(0),
                new Artist(rs.GetString(4), null, "", "", ""),
                SqlHelper.GetStringOrEmpty(rs, 1),
                rs.IsDBNull(2) ? 0.0 : Convert.ToDouble(rs.GetValue(2)),
                rs.IsDBNull(3) ? 0 : rs.GetInt32(3)
            );
        }

        private static List<Artwork> Query(string whereClause = "", string paramName = null, object paramValue = null)
        {
            List<Artwork> artworks = new List<Artwork>();
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(BaseSelect + " " + whereClause, conn))
                {
                    if (paramName != null)
                    {
                        cmd.Parameters.AddWithValue(paramName, paramValue);
                    }
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            artworks.Add(Map(rs));
                        }
                    }
                }
            }
            return artworks;
        }

        public void AddArtwork(Artwork artwork, string artistName)
        {
            string sql = "INSERT INTO Artwork (title, id_artist, artwork_type, price, creation_year) " +
                         "VALUES (@title, (SELECT id_artist FROM Artist WHERE name = @artistName), @type, @price, @creationYear)";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@title", artwork.Title);
                    cmd.Parameters.AddWithValue("@artistName", artistName);
                    cmd.Parameters.AddWithValue("@type", SqlHelper.ToDb(artwork.Type));
                    cmd.Parameters.AddWithValue("@price", artwork.Price);
                    cmd.Parameters.AddWithValue("@creationYear", artwork.CreationYear);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Artwork> GetAllArtworks()
        {
            return Query();
        }

        public void DeleteArtwork(string title)
        {
            string sql = "DELETE FROM Artwork WHERE title = @title";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@title", title);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void UpdateArtwork(string oldTitle, Artwork updatedArtwork, string artistName)
        {
            string sql = "UPDATE Artwork SET title = @title, id_artist = (SELECT id_artist FROM Artist WHERE name = @artistName), " +
                         "artwork_type = @type, price = @price, creation_year = @creationYear WHERE title = @oldTitle";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@title", updatedArtwork.Title);
                    cmd.Parameters.AddWithValue("@artistName", artistName);
                    cmd.Parameters.AddWithValue("@type", SqlHelper.ToDb(updatedArtwork.Type));
                    cmd.Parameters.AddWithValue("@price", updatedArtwork.Price);
                    cmd.Parameters.AddWithValue("@creationYear", updatedArtwork.CreationYear);
                    cmd.Parameters.AddWithValue("@oldTitle", oldTitle);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Artwork> FilterArtworkByArtist(string artistName)
        {
            return Query("WHERE ar.name = @artistName", "@artistName", artistName);
        }

        public List<Artwork> FilterArtworkByPrice(double maxPrice)
        {
            return Query("WHERE a.price <= @maxPrice", "@maxPrice", maxPrice);
        }

        public List<Artwork> FilterArtworkByType(string type)
        {
            return Query("WHERE a.artwork_type = @type", "@type", type);
        }

        public List<Artwork> SearchByTitle(string title)
        {
            return Query("WHERE a.title LIKE @title", "@title", $"%{title}%");
        }
    }
}