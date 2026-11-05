
namespace AssignmentMVVM.Model
{
    public class Artist
    {
        private string name;
        private DateTime birthDate;
        private string birthPlace;
        private string nationality;
        private string photo;
        private List<Artwork> artworks;


        public Artist()
        {

        }

        public Artist(string name, DateTime birthDate, string birthPlace, string nationality,string photo)
        {
            this.name = name;
            this.birthDate = birthDate;
            this.birthPlace = birthPlace;
            this.nationality = nationality;
            this.photo = photo;
        }

        
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        public DateTime BirthDate
        {
            get { return birthDate; }
            set { birthDate = value; }
        }

        public string BirthPlace
        {
            get { return birthPlace; }
            set { birthPlace = value; }
        }

        public string Nationality
        {
            get { return nationality; }
            set { nationality = value; }
        }

        public string Photo
        {
            get { return photo;  }
            set { photo = value; }
        }
        public List<Artwork> Artworks
        {
            get { return artworks; }
            set { artworks = value; }
        }




    }
}