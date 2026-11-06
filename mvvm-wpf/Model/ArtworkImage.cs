namespace AssignmentMVVM.Model
{
    public class ArtworkImage
    {
        private string imagePath;
        private Artwork artwork;

        public ArtworkImage() { }

        public ArtworkImage(string imagePath, Artwork artwork)
        {
            this.imagePath = imagePath;
            this.artwork = artwork;
        }

        public string ImagePath
        {
            get { return imagePath; }
            set { imagePath = value; }
        }

       
    }
}