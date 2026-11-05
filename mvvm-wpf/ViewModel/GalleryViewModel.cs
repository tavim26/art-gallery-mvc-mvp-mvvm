using System.ComponentModel;
using System.IO;
using System.Text;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;

using AssignmentMVVM.Model;
using AssignmentMVVM.Repository;
using AssignmentMVVM.ViewModel.Commands;
using Microsoft.Win32;

namespace AssignmentMVVM.ViewModel
{
    public class GalleryViewModel : INotifyPropertyChanged
    {
        private ArtistRepo artistRepo;
        private ArtworkRepo artworkRepo;
        private ArtworkImageRepo artworkImageRepo;

        private Artist crtArtist;
        private Artwork crtArtwork;

        private List<Artist> artistsList;
        private List<Artwork> artworksList;

        private string message;
        private string searchedArtist;
        private string searchedArtwork;

        private string selectedArtistName;
        private string selectedArtworkType;
        private string priceFilter;

        private List<string> artistNames;

        private ICommand addArtistCommand;
        private ICommand updateArtistCommand;
        private ICommand deleteArtistCommand;
        private ICommand searchArtistCommand;

        private ICommand addArtworkCommand;
        private ICommand updateArtworkCommand;
        private ICommand deleteArtworkCommand;
        private ICommand searchArtworkCommand;

        private ICommand filterByArtistCommand;
        private ICommand filterByTypeCommand;
        private ICommand filterByPriceCommand;

        private ICommand clearArtistCommand;
        private ICommand clearArtworkCommand;


        private ICommand addImageCommand;
        private ICommand saveToCsvCommand;
        private ICommand saveToDocCommand;

        public GalleryViewModel()
        {
            this.crtArtist = new Artist();
            this.crtArtwork = new Artwork();

            this.artistRepo = new ArtistRepo();
            this.artworkRepo = new ArtworkRepo();
            this.artworkImageRepo = new ArtworkImageRepo();

            this.artistsList = artistRepo.GetAllArtists();
            this.artworksList = artworkRepo.GetAllArtworks();

            ArtistNames = ArtistsList.Select(a => a.Name).ToList();

            this.addArtistCommand = new GalleryCommand(AddArtist);
            this.updateArtistCommand = new GalleryCommand(UpdateArtist);
            this.deleteArtistCommand = new GalleryCommand(DeleteArtist);
            this.searchArtistCommand = new GalleryCommand(SearchArtists);

            this.addArtworkCommand = new GalleryCommand(AddArtwork);
            this.updateArtworkCommand = new GalleryCommand(UpdateArtwork);
            this.deleteArtworkCommand = new GalleryCommand(DeleteArtwork);
            this.searchArtworkCommand = new GalleryCommand(SearchArtwork);

            this.filterByArtistCommand = new GalleryCommand(FilterArtworkByArtist);
            this.filterByTypeCommand = new GalleryCommand(FilterArtworkByType);
            this.filterByPriceCommand = new GalleryCommand(FilterArtworkByPrice);

            clearArtistCommand = new GalleryCommand(ClearArtist);
            clearArtworkCommand = new GalleryCommand(ClearArtwork);



            this.addImageCommand = new GalleryCommand(AddImage);
            this.saveToCsvCommand = new GalleryCommand(SaveToCsv);
            this.saveToDocCommand = new GalleryCommand(SaveToDoc);
        }

        public event PropertyChangedEventHandler PropertyChanged;


        private void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }



        
        public List<string> ArtistNames
        {
            get => artistNames;
            set
            {
                artistNames = value;
                OnPropertyChanged(nameof(ArtistNames));
            }
        }



        public List<string> ArtworkTypes { get; } = new List<string>
        {
            "Painting", "Sculpture", "Photography"
        };


        public string SelectedArtistName
        {
            get => selectedArtistName;
            set { selectedArtistName = value; OnPropertyChanged(nameof(SelectedArtistName)); }
        }

        public string SelectedArtworkType
        {
            get => selectedArtworkType;
            set { selectedArtworkType = value; OnPropertyChanged(nameof(SelectedArtworkType)); }
        }

        
        public string PriceFilter
        {
            get => priceFilter;
            set { priceFilter = value; OnPropertyChanged(nameof(PriceFilter)); }
        }


        public List<Artist> ArtistsList
        {
            get => artistsList;
            set { artistsList = value; OnPropertyChanged(nameof(ArtistsList)); }
        }

        public List<Artwork> ArtworksList
        {
            get => artworksList;
            set { artworksList = value; OnPropertyChanged(nameof(ArtworksList)); }
        }

        public string Message
        {
            get => message;
            set { message = value; OnPropertyChanged(nameof(Message)); }
        }

        public string SearchedArtist
        {
            get => searchedArtist;
            set
            {
                searchedArtist = value;
                
            }
        }


        public string SearchedArtwork
        {
            get => searchedArtwork;
            set => searchedArtwork = value;
        }


        public Artist CrtArtist
        {
            get => crtArtist;
            set
            {
                crtArtist = value;
                OnPropertyChanged(nameof(CrtArtist));
                LoadArtistImage();
            }
        }


        private void LoadArtistImage()
        {
            if (!string.IsNullOrWhiteSpace(crtArtist?.Photo))
            {
                string fullPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Images", crtArtist.Photo);

                if (File.Exists(fullPath))
                {
                    try
                    {
                        ArtistImageSource = new BitmapImage(new Uri(fullPath));
                        return;
                    }
                    catch
                    {
                    }
                }
            }

            ArtistImageSource = null;
            Message = "No image assigned.";
        }






        public Artwork CrtArtwork
        {
            get => crtArtwork;
            set
            {
                crtArtwork = value;

                if (!string.IsNullOrWhiteSpace(crtArtwork?.Title))
                {
                    crtArtwork.Images = artworkImageRepo.GetImagesByArtwork(crtArtwork.Title);
                    LoadArtworkImages();
                }
                else
                {
                    crtArtwork.Images = new List<ArtworkImage>(); 
                    ArtworkImage1 = null;
                    ArtworkImage2 = null;
                }

                OnPropertyChanged(nameof(CrtArtwork));
            }
        }







        private ImageSource artistImageSource;
        public ImageSource ArtistImageSource
        {
            get => artistImageSource;
            set { artistImageSource = value; OnPropertyChanged(nameof(ArtistImageSource)); }
        }



        private ImageSource artworkImage1;
        public ImageSource ArtworkImage1
        {
            get => artworkImage1;
            set { artworkImage1 = value; OnPropertyChanged(nameof(ArtworkImage1)); }
        }

        private ImageSource artworkImage2;
        public ImageSource ArtworkImage2
        {
            get => artworkImage2;
            set { artworkImage2 = value; OnPropertyChanged(nameof(ArtworkImage2)); }
        }


        private void LoadArtworkImages()
        {
            ArtworkImage1 = null;
            ArtworkImage2 = null;

            if (crtArtwork?.Images == null || crtArtwork.Images.Count == 0)
            {
                Message = "No images for this artwork.";
                return;
            }

            try
            {
                string folder = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Images");

                for (int i = 0; i < crtArtwork.Images.Count && i < 2; i++)
                {
                    string fullPath = Path.Combine(folder, crtArtwork.Images[i].ImagePath);
                    if (File.Exists(fullPath))
                    {
                        var image = new BitmapImage(new Uri(fullPath));
                        if (i == 0) ArtworkImage1 = image;
                        else if (i == 1) ArtworkImage2 = image;
                    }
                }
            }
            catch (Exception ex)
            {
                Message = $"Error loading artwork images: {ex.Message}";
            }
        }




        public ICommand AddArtistCommand => addArtistCommand;
        public ICommand UpdateArtistCommand => updateArtistCommand;
        public ICommand DeleteArtistCommand => deleteArtistCommand;
        public ICommand SearchArtistCommand => searchArtistCommand;

        public ICommand AddArtworkCommand => addArtworkCommand;
        public ICommand UpdateArtworkCommand => updateArtworkCommand;
        public ICommand DeleteArtworkCommand => deleteArtworkCommand;
        public ICommand SearchArtworkCommand => searchArtworkCommand;

        public ICommand FilterByArtistCommand => filterByArtistCommand;
        public ICommand FilterByTypeCommand => filterByTypeCommand;
        public ICommand FilterByPriceCommand => filterByPriceCommand;


        public ICommand ClearArtistCommand => clearArtistCommand;
        public ICommand ClearArtworkCommand => clearArtworkCommand;

        public ICommand AddImageCommand => addImageCommand;
        public ICommand SaveToCsvCommand => saveToCsvCommand;
        public ICommand SaveToDocCommand => saveToDocCommand;

        public void AddArtist()
        {
            try
            {
                OpenFileDialog openFileDialog = new OpenFileDialog();
                openFileDialog.Filter = "Image files (*.jpg, *.jpeg, *.png)|*.jpg;*.jpeg;*.png";

                if (openFileDialog.ShowDialog() == true)
                {
                    string selectedFile = openFileDialog.FileName;

                    
                    string imagesFolder = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Images");
                    if (!Directory.Exists(imagesFolder))
                        Directory.CreateDirectory(imagesFolder);

                    
                    string fileName = Path.GetFileName(selectedFile);
                    string destinationPath = Path.Combine(imagesFolder, fileName);
                    File.Copy(selectedFile, destinationPath, overwrite: true);

                    
                    crtArtist.Photo = fileName;

                    artistRepo.AddArtist(crtArtist);
                    ArtistsList = artistRepo.GetAllArtists();
                    Message = "Artist added successfully!";
                }
                else
                {
                    Message = "No image selected. Artist was not added.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }





        public void UpdateArtist()
        {
            try
            {
                artistRepo.UpdateArtist(crtArtist.Name, crtArtist);
                ArtistsList = artistRepo.GetAllArtists();

                ArtistNames = ArtistsList.Select(a => a.Name).ToList();

                Message = "Artist updated successfully!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void DeleteArtist()
        {
            try
            {
                artistRepo.DeleteArtist(crtArtist.Name);
                ArtistsList = artistRepo.GetAllArtists();

                ArtistNames = ArtistsList.Select(a => a.Name).ToList();

                Message = "Artist deleted successfully!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void SearchArtists()
        {
            try
            {
                if (string.IsNullOrWhiteSpace(searchedArtist))
                    ArtistsList = artistRepo.GetAllArtists(); 
                else
                    ArtistsList = artistRepo.SearchByName(searchedArtist);

                Message = "Search completed!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }


        public void AddArtwork()
        {
            try
            {
                artworkRepo.AddArtwork(crtArtwork, crtArtwork.Artist.Name);
                ArtworksList = artworkRepo.GetAllArtworks();
                Message = "Artwork added successfully!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void UpdateArtwork()
        {
            try
            {
                artworkRepo.UpdateArtwork(crtArtwork.Title, crtArtwork, crtArtwork.Artist.Name);
                ArtworksList = artworkRepo.GetAllArtworks();
                Message = "Artwork updated successfully!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void DeleteArtwork()
        {
            try
            {
                artworkRepo.DeleteArtwork(crtArtwork.Title);
                ArtworksList = artworkRepo.GetAllArtworks();
                Message = "Artwork deleted successfully!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void SearchArtwork()
        {
            try
            {
                ArtworksList = artworkRepo.SearchByTitle(searchedArtwork);
                Message = "Search completed!";
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }

        public void FilterArtworkByArtist()
        {
            try
            {
                if (!string.IsNullOrWhiteSpace(SelectedArtistName))
                {
                    ArtworksList = artworkRepo.FilterArtworkByArtist(SelectedArtistName);
                    Message = "Filtered by artist.";
                }
                else
                {
                    Message = "Select an artist to filter.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }


        public void FilterArtworkByType()
        {
            try
            {
                if (!string.IsNullOrWhiteSpace(SelectedArtworkType))
                {
                    ArtworksList = artworkRepo.FilterArtworkByType(SelectedArtworkType);
                    Message = "Filtered by type.";
                }
                else
                {
                    Message = "Select a type to filter.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }


        public void FilterArtworkByPrice()
        {
            try
            {
                if (double.TryParse(PriceFilter, out double price))
                {
                    ArtworksList = artworkRepo.FilterArtworkByPrice(price);
                    Message = "Filtered by price.";
                }
                else
                {
                    Message = "Enter a valid price.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }


        public void AddImage()
        {
            try
            {
                OpenFileDialog openFileDialog = new OpenFileDialog();
                openFileDialog.Filter = "Image files (*.jpg, *.jpeg, *.png)|*.jpg;*.jpeg;*.png";

                if (openFileDialog.ShowDialog() == true)
                {
                    string imageName = Path.GetFileName(openFileDialog.FileName);
                    string destPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Images", imageName);

                    if (!File.Exists(destPath))
                        File.Copy(openFileDialog.FileName, destPath);

                    var image = new ArtworkImage(imageName, crtArtwork);
                    artworkImageRepo.AddImage(crtArtwork.Title, image);

                    crtArtwork.Images = artworkImageRepo.GetImagesByArtwork(crtArtwork.Title);
                    LoadArtworkImages();

                    Message = "Image added to artwork!";
                }
                else
                {
                    Message = "No image selected.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error: " + ex.Message;
            }
        }





        public void ClearArtist()
        {
            CrtArtist = new Artist();
            Message = "Artist fields cleared.";
        }

        public void ClearArtwork()
        {
            CrtArtwork = new Artwork();
            Message = "Artwork fields cleared.";
        }


        public void SaveToCsv()
        {
            try
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog
                {
                    Filter = "CSV files (*.csv)|*.csv",
                    DefaultExt = "csv",
                    FileName = "artworks.csv"
                };

                if (saveFileDialog.ShowDialog() == true)
                {
                    var sb = new StringBuilder();
                    sb.AppendLine("Title,Artist,Type,Price,CreationYear");

                    foreach (var artwork in ArtworksList)
                    {
                        sb.AppendLine($"{artwork.Title},{artwork.Artist.Name},{artwork.Type},{artwork.Price},{artwork.CreationYear}");
                    }

                    File.WriteAllText(saveFileDialog.FileName, sb.ToString());
                    Message = "Saved to CSV.";
                }
            }
            catch (Exception ex)
            {
                Message = "Error saving CSV: " + ex.Message;
            }
        }


        public void SaveToDoc()
        {
            try
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog
                {
                    Filter = "Text files (*.txt)|*.txt",
                    DefaultExt = "txt",
                    FileName = "artworks.txt"
                };

                if (saveFileDialog.ShowDialog() == true)
                {
                    var sb = new StringBuilder();

                    foreach (var artwork in ArtworksList)
                    {
                        sb.AppendLine("=====================================");
                        sb.AppendLine($"Title: {artwork.Title}");
                        sb.AppendLine($"Artist: {artwork.Artist.Name}");
                        sb.AppendLine($"Type: {artwork.Type}");
                        sb.AppendLine($"Price: {artwork.Price}");
                        sb.AppendLine($"Year: {artwork.CreationYear}");
                        sb.AppendLine();
                    }

                    File.WriteAllText(saveFileDialog.FileName, sb.ToString());
                    Message = "Saved to DOC (TXT).";
                }
            }
            catch (Exception ex)
            {
                Message = "Error saving TXT: " + ex.Message;
            }
        }

    }
}
