using System.Data.SqlClient;

namespace AssignmentMVVM.Repository
{
    internal static class SqlHelper
    {
        // AddWithValue with a C# null throws; it must be DBNull.Value.
        public static object ToDb(object value) => value ?? DBNull.Value;

        public static string GetStringOrEmpty(SqlDataReader rs, int index)
            => rs.IsDBNull(index) ? string.Empty : rs.GetString(index);

        public static string GetStringOrNull(SqlDataReader rs, int index)
            => rs.IsDBNull(index) ? null : rs.GetString(index);

        public static DateTime? GetDateOrNull(SqlDataReader rs, int index)
            => rs.IsDBNull(index) ? null : rs.GetDateTime(index);
    }
}