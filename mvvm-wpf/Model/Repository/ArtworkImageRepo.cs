using System.Data.SqlClient;
using AssignmentMVVM.Model;

namespace AssignmentMVVM.Repository
{
    public class ArtworkImageRepo
    {
        private static readonly string DB_URL = "Data Source=(LocalDB)\\MSSQLLocalDB;Initial Catalog=art_gallery;Integrated Security=True;";

        public void AddImage(string artworkTitle, ArtworkImage image)
        {
            string sql = "INSERT INTO Artwork_Images (id_artwork, image_path) " +
                         "VALUES ((SELECT id_artwork FROM Artwork WHERE title = @title), @imagePath)";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@title", artworkTitle);
                    cmd.Parameters.AddWithValue("@imagePath", image.ImagePath);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<ArtworkImage> GetImagesByArtwork(string artworkTitle)
        {
            List<ArtworkImage> images = new List<ArtworkImage>();
            string sql = "SELECT ai.image_path FROM Artwork_Images ai " +
                         "JOIN Artwork a ON ai.id_artwork = a.id_artwork WHERE a.title = @title";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@title", artworkTitle);
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            images.Add(new ArtworkImage(rs.GetString(0), null));
                        }
                    }
                }
            }
            return images;
        }
    }
}