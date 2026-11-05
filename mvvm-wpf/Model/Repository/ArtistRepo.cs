using System.Data.SqlClient; 
using AssignmentMVVM.Model;

namespace AssignmentMVVM.Repository
{
    public class ArtistRepo
    {
        private static readonly string DB_URL = "Data Source=(LocalDB)\\MSSQLLocalDB;Initial Catalog=art_gallery;Integrated Security=True;";


        public void AddArtist(Artist artist)
        {
            string sql = "INSERT INTO Artist (name, birth_date, birth_place, nationality, photo) VALUES (@name, @birthDate, @birthPlace, @nationality, @photo)";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@name", artist.Name);
                    cmd.Parameters.AddWithValue("@birthDate", artist.BirthDate);
                    cmd.Parameters.AddWithValue("@birthPlace", artist.BirthPlace);
                    cmd.Parameters.AddWithValue("@nationality", artist.Nationality);
                    cmd.Parameters.AddWithValue("@photo", artist.Photo ?? (object)DBNull.Value);
                    cmd.ExecuteNonQuery();
                }
            }
        }


        public List<Artist> GetAllArtists()
        {
            List<Artist> artists = new List<Artist>();
            string sql = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist";

            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                using (var rs = cmd.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        var photoValue = rs.IsDBNull(4) ? null : rs.GetString(4);
                        // debug
                        Console.WriteLine($"Loaded artist photo path: {photoValue}");

                        artists.Add(new Artist(
                            rs.GetString(0),
                            rs.GetDateTime(1),
                            rs.GetString(2),
                            rs.GetString(3),
                            photoValue
                        ));
                    }
                }
            }

            return artists;
        }


        public void DeleteArtist(string name)
        {
            string sql = "DELETE FROM Artist WHERE name = @name";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@name", name);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void UpdateArtist(string oldName, Artist updatedArtist)
        {
            string sql = "UPDATE Artist SET name = @name, birth_date = @birthDate, birth_place = @birthPlace, nationality = @nationality, photo = @photo WHERE name = @oldName";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@name", updatedArtist.Name);
                    cmd.Parameters.AddWithValue("@birthDate", updatedArtist.BirthDate);
                    cmd.Parameters.AddWithValue("@birthPlace", updatedArtist.BirthPlace);
                    cmd.Parameters.AddWithValue("@nationality", updatedArtist.Nationality);
                    cmd.Parameters.AddWithValue("@photo", updatedArtist.Photo ?? (object)DBNull.Value);
                    cmd.Parameters.AddWithValue("@oldName", oldName);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public Artist FindArtistByName(string name)
        {
            string sql = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist WHERE name = @name";
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@name", name);
                    using (var rs = cmd.ExecuteReader())
                    {
                        if (rs.Read())
                        {
                            return new Artist(
                                rs.GetString(0),
                                rs.GetDateTime(1), 
                                rs.GetString(2),
                                rs.GetString(3),
                                rs.GetString(4)
                            );
                        }
                    }
                }
            }
            return null;
        }

        public List<Artist> SearchByName(string name)
        {
            List<Artist> artists = new List<Artist>();
            string sql = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist WHERE name LIKE @name"; 
            using (var conn = new SqlConnection(DB_URL)) 
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn)) 
                {
                    cmd.Parameters.AddWithValue("@name", $"%{name}%");
                    using (var rs = cmd.ExecuteReader())
                    {
                        while (rs.Read())
                        {
                            artists.Add(new Artist(
                                rs.GetString(0),
                                rs.GetDateTime(1), 
                                rs.GetString(2),
                                rs.GetString(3),
                                rs.GetString(4)
                            ));
                        }
                    }
                }
            }
            return artists;
        }
    }
}