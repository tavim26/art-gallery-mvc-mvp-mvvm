using AssignmentMVVM.ViewModel;
using System.Windows.Controls;

namespace AssignmentMVVM.View
{
    public partial class GalleryGUI : UserControl
    {
        private GalleryViewModel viewModel;

        public GalleryGUI()
        {
                InitializeComponent();
                this.viewModel = new GalleryViewModel();
                this.DataContext = this.viewModel;

        }

        
    }
}