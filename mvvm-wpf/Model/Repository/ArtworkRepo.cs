using System.Data.SqlClient; 
using AssignmentMVVM.Model;

namespace AssignmentMVVM.Repository
{
    public class ArtworkRepo
    {
        private static readonly string DB_URL = "Data Source=(LocalDB)\\MSSQLLocalDB;Initial Catalog=art_gallery;Integrated Security=True;";
        private ArtistRepo artistRepo;

        public ArtworkRepo()
        {
            this.artistRepo = new ArtistRepo();
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
                    cmd.Parameters.AddWithValue("@type", artwork.Type);
                    cmd.Parameters.AddWithValue("@price", artwork.Price);
                    cmd.Parameters.AddWithValue("@creationYear", artwork.CreationYear);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Artwork> GetAllArtworks()
        {
            List<Artwork> artworks = new List<Artwork>();
            string sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                         "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            artworks.Add(new Artwork(
                                rs.GetString(0),
                                new Artist(rs.GetString(4), DateTime.MinValue, "", "",""),
                                rs.GetString(1),
                                Convert.ToDouble(rs["price"]), 
                                rs.GetInt32(3)
                            ));

                        }
                    }
                }
            }
            return artworks;
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
                    cmd.Parameters.AddWithValue("@type", updatedArtwork.Type);
                    cmd.Parameters.AddWithValue("@price", updatedArtwork.Price);
                    cmd.Parameters.AddWithValue("@creationYear", updatedArtwork.CreationYear);
                    cmd.Parameters.AddWithValue("@oldTitle", oldTitle);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Artwork> FilterArtworkByArtist(string artistName)
        {
            List<Artwork> artworks = new List<Artwork>();
            string sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                         "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE ar.name = @artistName";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@artistName", artistName);
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            Artist artist = artistRepo.FindArtistByName(rs.GetString(4));
                            artworks.Add(new Artwork(
                                rs.GetString(0),
                                artist,
                                rs.GetString(1),
                                Convert.ToDouble(rs["price"]),
                                rs.GetInt32(3)
                            ));
                        }
                    }
                }
            }
            return artworks;
        }

        public List<Artwork> FilterArtworkByPrice(double maxPrice)
        {
            List<Artwork> artworks = new List<Artwork>();
            string sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                         "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE a.price < @maxPrice";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@maxPrice", maxPrice);
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            Artist artist = artistRepo.FindArtistByName(rs.GetString(4));
                            artworks.Add(new Artwork(
                                rs.GetString(0),
                                artist,
                                rs.GetString(1),
                                Convert.ToDouble(rs["price"]),
                                rs.GetInt32(3)
                            ));
                        }
                    }
                }
            }
            return artworks;
        }

        public List<Artwork> FilterArtworkByType(string type)
        {
            List<Artwork> artworks = new List<Artwork>();
            string sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                         "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE a.artwork_type = @type";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@type", type);
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            Artist artist = artistRepo.FindArtistByName(rs.GetString(4));
                            artworks.Add(new Artwork(
                                rs.GetString(0),
                                artist,
                                rs.GetString(1),
                                Convert.ToDouble(rs["price"]),
                                rs.GetInt32(3)
                            ));
                        }
                    }
                }
            }
            return artworks;
        }

        public List<Artwork> SearchByTitle(string title)
        {
            List<Artwork> artworks = new List<Artwork>();
            string sql = "SELECT a.title, a.artwork_type, a.price, a.creation_year, ar.name AS artist_name " +
                         "FROM Artwork a JOIN Artist ar ON a.id_artist = ar.id_artist WHERE a.title LIKE @title";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@title", $"%{title}%");
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            Artist artist = artistRepo.FindArtistByName(rs.GetString(4));
                            artworks.Add(new Artwork(
                                rs.GetString(0),
                                artist,
                                rs.GetString(1),
                                Convert.ToDouble(rs["price"]),
                                rs.GetInt32(3)
                            ));
                        }
                    }
                }
            }
            return artworks;
        }




    }
}