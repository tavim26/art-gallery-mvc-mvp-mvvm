namespace AssignmentMVVM.Model
{
    public class Artwork
    {
        private string title;
        private Artist artist;
        private string type;
        private double price;
        private int creationYear;

        private List<ArtworkImage> images;

        public Artwork() {

        }

        public Artwork(string title, Artist artist, string type, double price, int creationYear)
        {
            this.title = title;
            this.artist = artist;
            this.type = type;
            this.price = price;
            this.creationYear = creationYear;
            this.images = new List<ArtworkImage>();
        }

        public string Title
        {
            get { return title; }
            set { title = value; }
        }

        public Artist Artist
        {
            get { return artist; }
            set { artist = value; }
        }

        public string Type
        {
            get { return type; }
            set { type = value; }
        }

        public double Price
        {
            get { return price; }
            set { price = value; }
        }

        public int CreationYear
        {
            get { return creationYear; }
            set { creationYear = value; }
        }


        public List<ArtworkImage> Images
        {
            get { return images; }
            set { images = value; }
        }


        

    }
}