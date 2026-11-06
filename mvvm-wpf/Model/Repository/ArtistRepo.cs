using System.Data.SqlClient;
using AssignmentMVVM.Model;

namespace AssignmentMVVM.Repository
{
    public class ArtistRepo
    {
        private static readonly string DB_URL = "Data Source=(LocalDB)\\MSSQLLocalDB;Initial Catalog=art_gallery;Integrated Security=True;";

        private const string SelectColumns = "SELECT name, birth_date, birth_place, nationality, photo FROM Artist";

        private static Artist Map(SqlDataReader rs)
        {
            return new Artist(
                rs.GetString(0),
                SqlHelper.GetDateOrNull(rs, 1),
                SqlHelper.GetStringOrEmpty(rs, 2),
                SqlHelper.GetStringOrEmpty(rs, 3),
                SqlHelper.GetStringOrNull(rs, 4)
            );
        }

        public void AddArtist(Artist artist)
        {
            string sql = "INSERT INTO Artist (name, birth_date, birth_place, nationality, photo) VALUES (@name, @birthDate, @birthPlace, @nationality, @photo)";
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@name", artist.Name);
                    cmd.Parameters.AddWithValue("@birthDate", SqlHelper.ToDb(artist.BirthDate));
                    cmd.Parameters.AddWithValue("@birthPlace", SqlHelper.ToDb(artist.BirthPlace));
                    cmd.Parameters.AddWithValue("@nationality", SqlHelper.ToDb(artist.Nationality));
                    cmd.Parameters.AddWithValue("@photo", SqlHelper.ToDb(artist.Photo));
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public List<Artist> GetAllArtists()
        {
            List<Artist> artists = new List<Artist>();
            using (var conn = new SqlConnection(DB_URL))
            {
                conn.Open();
                using (var cmd = new SqlCommand(SelectColumns, conn))
                using (var rs = cmd.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        artists.Add(Map(rs));
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
                    cmd.Parameters.AddWithValue("@birthDate", SqlHelper.ToDb(updatedArtist.BirthDate));
                    cmd.Parameters.AddWithValue("@birthPlace", SqlHelper.ToDb(updatedArtist.BirthPlace));
                    cmd.Parameters.AddWithValue("@nationality", SqlHelper.ToDb(updatedArtist.Nationality));
                    cmd.Parameters.AddWithValue("@photo", SqlHelper.ToDb(updatedArtist.Photo));
                    cmd.Parameters.AddWithValue("@oldName", oldName);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public Artist FindArtistByName(string name)
        {
            string sql = SelectColumns + " WHERE name = @name";
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
                            return Map(rs);
                        }
                    }
                }
            }
            return null;
        }

        public List<Artist> SearchByName(string name)
        {
            List<Artist> artists = new List<Artist>();
            string sql = SelectColumns + " WHERE name LIKE @name";
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
                            artists.Add(Map(rs));
                        }
                    }
                }
            }
            return artists;
        }
    }
}