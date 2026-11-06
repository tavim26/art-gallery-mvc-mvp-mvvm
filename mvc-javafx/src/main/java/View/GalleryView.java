package View;

import Model.Observer.Observable;
import Model.Observer.Observer;

import Model.ViewModel.GalleryViewModel;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;


public class GalleryView implements Observer
{

    // Left pane buttons
    @FXML private Button artistButton;
    @FXML private Button artworkButton;
    @FXML private Button statisticsButton;

    // Artist pane components
    @FXML private AnchorPane artistPane;
    @FXML private TableView<String[]> artistTable;
    @FXML private TableColumn<String[], String> nameColumn;
    @FXML private TableColumn<String[], String> birthdayColumn;
    @FXML private TableColumn<String[], String> birthplaceColumn;
    @FXML private TableColumn<String[], String> nationalityColumn;
    @FXML private TableColumn<String[], String> artworkListColumn;
    @FXML private TextField nameTextField;
    @FXML private TextField birthplaceTextField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private TextField nationalityTextField;
    @FXML private TextField searchArtistTextField;
    @FXML private Button addArtistButton;
    @FXML private Button editArtistButton;
    @FXML private Button deleteArtistButton;
    @FXML private Button clearArtistButton;
    @FXML private ImageView artistPhoto;

    // Artwork pane components
    @FXML private AnchorPane artworkPane;
    @FXML private TableView<String[]> artworkTable;
    @FXML private TableColumn<String[], String> titleColumn;
    @FXML private TableColumn<String[], String> artistNameColumn;
    @FXML private TableColumn<String[], String> typeColumn;
    @FXML private TableColumn<String[], String> priceColumn;
    @FXML private TableColumn<String[], String> creationYearColumn;
    @FXML private TextField titleTextField;
    @FXML private ComboBox<String> artistComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField priceTextField;
    @FXML private TextField yearTextField;
    @FXML private TextField searchArtworkTextField;
    @FXML private ComboBox<String> filterByArtistBox;
    @FXML private ComboBox<String> filterByTypeBox;
    @FXML private Button addArtworkButton;
    @FXML private Button editArtworkButton;
    @FXML private Button deleteArtworkButton;
    @FXML private Button addImageButton;
    @FXML private Button clearArtworkButton;
    @FXML private Button saveToCsvButton;
    @FXML private Button saveToDocButton;
    @FXML private Slider priceSlider;
    @FXML private ImageView artworkImage1;
    @FXML private ImageView artworkImage2;

    // Statistics pane components
    @FXML private AnchorPane statisticsPane;
    @FXML private BarChart<String, Number> typeChart;
    @FXML private CategoryAxis artworkTypeAxis;
    @FXML private NumberAxis nrOfArtworksAxis;

    @FXML private BarChart<String, Number> artistChart;
    @FXML private CategoryAxis artistAxis;
    @FXML private NumberAxis numberOfArtworksAxis;




    @Override
    public void update(Observable obs)
    {
        if (!(obs instanceof GalleryViewModel viewModel)) return;

        var artistData = javafx.collections.FXCollections.observableArrayList(viewModel.getArtist());
        artistTable.setItems(artistData);

        var artworkData = javafx.collections.FXCollections.observableArrayList(viewModel.getArtwork());
        artworkTable.setItems(artworkData);

        var artistNames = javafx.collections.FXCollections.observableArrayList(viewModel.getArtistNames());
        artistComboBox.setItems(artistNames);
        filterByArtistBox.setItems(artistNames);
    }


    // Left pane buttons
    public Button getArtistButton() {
        return artistButton;
    }

    public void setArtistButton(Button artistButton) {
        this.artistButton = artistButton;
    }

    public Button getArtworkButton() {
        return artworkButton;
    }

    public void setArtworkButton(Button artworkButton) {
        this.artworkButton = artworkButton;
    }

    public Button getStatisticsButton() {
        return statisticsButton;
    }

    public void setStatisticsButton(Button statisticsButton) {
        this.statisticsButton = statisticsButton;
    }

    // Artist pane components
    public AnchorPane getArtistPane() {
        return artistPane;
    }

    public void setArtistPane(AnchorPane artistPane) {
        this.artistPane = artistPane;
    }

    public TableView<String[]> getArtistTable() {
        return artistTable;
    }

    public void setArtistTable(TableView<String[]> artistTable) {
        this.artistTable = artistTable;
    }

    public TableColumn<String[], String> getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(TableColumn<String[], String> nameColumn) {
        this.nameColumn = nameColumn;
    }

    public TableColumn<String[], String> getBirthdayColumn() {
        return birthdayColumn;
    }

    public void setBirthdayColumn(TableColumn<String[], String> birthdayColumn) {
        this.birthdayColumn = birthdayColumn;
    }

    public TableColumn<String[], String> getBirthplaceColumn() {
        return birthplaceColumn;
    }

    public void setBirthplaceColumn(TableColumn<String[], String> birthplaceColumn) {
        this.birthplaceColumn = birthplaceColumn;
    }

    public TableColumn<String[], String> getNationalityColumn() {
        return nationalityColumn;
    }

    public void setNationalityColumn(TableColumn<String[], String> nationalityColumn) {
        this.nationalityColumn = nationalityColumn;
    }

    public TableColumn<String[], String> getArtworkListColumn() {
        return artworkListColumn;
    }

    public void setArtworkListColumn(TableColumn<String[], String> artworkListColumn) {
        this.artworkListColumn = artworkListColumn;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public void setNameTextField(TextField nameTextField) {
        this.nameTextField = nameTextField;
    }

    public TextField getBirthplaceTextField() {
        return birthplaceTextField;
    }

    public void setBirthplaceTextField(TextField birthplaceTextField) {
        this.birthplaceTextField = birthplaceTextField;
    }

    public DatePicker getBirthdayDatePicker() {
        return birthdayDatePicker;
    }

    public void setBirthdayDatePicker(DatePicker birthdayDatePicker) {
        this.birthdayDatePicker = birthdayDatePicker;
    }

    public TextField getNationalityTextField() {
        return nationalityTextField;
    }

    public void setNationalityTextField(TextField nationalityTextField) {
        this.nationalityTextField = nationalityTextField;
    }

    public TextField getSearchArtistTextField() {
        return searchArtistTextField;
    }

    public void setSearchArtistTextField(TextField searchArtistTextField) {
        this.searchArtistTextField = searchArtistTextField;
    }

    public Button getAddArtistButton() {
        return addArtistButton;
    }

    public void setAddArtistButton(Button addArtistButton) {
        this.addArtistButton = addArtistButton;
    }

    public Button getEditArtistButton() {
        return editArtistButton;
    }

    public void setEditArtistButton(Button editArtistButton) {
        this.editArtistButton = editArtistButton;
    }

    public Button getDeleteArtistButton() {
        return deleteArtistButton;
    }

    public void setDeleteArtistButton(Button deleteArtistButton) {
        this.deleteArtistButton = deleteArtistButton;
    }

    public Button getClearArtistButton() {
        return clearArtistButton;
    }

    public void setClearArtistButton(Button clearArtistButton) {
        this.clearArtistButton = clearArtistButton;
    }

    public ImageView getArtistPhoto() {
        return artistPhoto;
    }

    public void setArtistPhoto(ImageView artistPhoto) {
        this.artistPhoto = artistPhoto;
    }

    // Artwork pane components
    public AnchorPane getArtworkPane() {
        return artworkPane;
    }

    public void setArtworkPane(AnchorPane artworkPane) {
        this.artworkPane = artworkPane;
    }

    public TableView<String[]> getArtworkTable() {
        return artworkTable;
    }

    public void setArtworkTable(TableView<String[]> artworkTable) {
        this.artworkTable = artworkTable;
    }

    public TableColumn<String[], String> getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(TableColumn<String[], String> titleColumn) {
        this.titleColumn = titleColumn;
    }

    public TableColumn<String[], String> getArtistNameColumn() {
        return artistNameColumn;
    }

    public void setArtistNameColumn(TableColumn<String[], String> artistNameColumn) {
        this.artistNameColumn = artistNameColumn;
    }

    public TableColumn<String[], String> getTypeColumn() {
        return typeColumn;
    }

    public void setTypeColumn(TableColumn<String[], String> typeColumn) {
        this.typeColumn = typeColumn;
    }

    public TableColumn<String[], String> getPriceColumn() {
        return priceColumn;
    }

    public void setPriceColumn(TableColumn<String[], String> priceColumn) {
        this.priceColumn = priceColumn;
    }

    public TableColumn<String[], String> getCreationYearColumn() {
        return creationYearColumn;
    }

    public void setCreationYearColumn(TableColumn<String[], String> creationYearColumn) {
        this.creationYearColumn = creationYearColumn;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(TextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public ComboBox<String> getArtistComboBox() {
        return artistComboBox;
    }

    public void setArtistComboBox(ComboBox<String> artistComboBox) {
        this.artistComboBox = artistComboBox;
    }

    public ComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    public void setTypeComboBox(ComboBox<String> typeComboBox) {
        this.typeComboBox = typeComboBox;
    }

    public TextField getPriceTextField() {
        return priceTextField;
    }

    public void setPriceTextField(TextField priceTextField) {
        this.priceTextField = priceTextField;
    }

    public TextField getYearTextField() {
        return yearTextField;
    }

    public void setYearTextField(TextField yearTextField) {
        this.yearTextField = yearTextField;
    }

    public TextField getSearchArtworkTextField() {
        return searchArtworkTextField;
    }

    public void setSearchArtworkTextField(TextField searchArtworkTextField) {
        this.searchArtworkTextField = searchArtworkTextField;
    }

    public ComboBox<String> getFilterByArtistBox() {
        return filterByArtistBox;
    }

    public void setFilterByArtistBox(ComboBox<String> filterByArtistBox) {
        this.filterByArtistBox = filterByArtistBox;
    }

    public ComboBox<String> getFilterByTypeBox() {
        return filterByTypeBox;
    }

    public void setFilterByTypeBox(ComboBox<String> filterByTypeBox) {
        this.filterByTypeBox = filterByTypeBox;
    }

    public Button getAddArtworkButton() {
        return addArtworkButton;
    }

    public void setAddArtworkButton(Button addArtworkButton) {
        this.addArtworkButton = addArtworkButton;
    }

    public Button getEditArtworkButton() {
        return editArtworkButton;
    }

    public void setEditArtworkButton(Button editArtworkButton) {
        this.editArtworkButton = editArtworkButton;
    }

    public Button getDeleteArtworkButton() {
        return deleteArtworkButton;
    }

    public void setDeleteArtworkButton(Button deleteArtworkButton) {
        this.deleteArtworkButton = deleteArtworkButton;
    }

    public Button getAddImageButton() {
        return addImageButton;
    }

    public void setAddImageButton(Button addImageButton) {
        this.addImageButton = addImageButton;
    }

    public Button getClearArtworkButton() {
        return clearArtworkButton;
    }

    public void setClearArtworkButton(Button clearArtworkButton) {
        this.clearArtworkButton = clearArtworkButton;
    }

    public Button getSaveToCsvButton() {
        return saveToCsvButton;
    }

    public void setSaveToCsvButton(Button saveToCsvButton) {
        this.saveToCsvButton = saveToCsvButton;
    }

    public Button getSaveToDocButton() {
        return saveToDocButton;
    }

    public void setSaveToDocButton(Button saveToDocButton) {
        this.saveToDocButton = saveToDocButton;
    }

    public Slider getPriceSlider() {
        return priceSlider;
    }

    public void setPriceSlider(Slider priceSlider) {
        this.priceSlider = priceSlider;
    }

    public ImageView getArtworkImage1() {
        return artworkImage1;
    }

    public void setArtworkImage1(ImageView artworkImage1) {
        this.artworkImage1 = artworkImage1;
    }

    public ImageView getArtworkImage2() {
        return artworkImage2;
    }

    public void setArtworkImage2(ImageView artworkImage2) {
        this.artworkImage2 = artworkImage2;
    }

    // Statistics pane components
    public AnchorPane getStatisticsPane() {
        return statisticsPane;
    }

    public void setStatisticsPane(AnchorPane statisticsPane) {
        this.statisticsPane = statisticsPane;
    }

    public BarChart<String, Number> getTypeChart() {
        return typeChart;
    }

    public void setTypeChart(BarChart<String, Number> typeChart) {
        this.typeChart = typeChart;
    }

    public CategoryAxis getArtworkTypeAxis() {
        return artworkTypeAxis;
    }

    public void setArtworkTypeAxis(CategoryAxis artworkTypeAxis) {
        this.artworkTypeAxis = artworkTypeAxis;
    }

    public NumberAxis getNrOfArtworksAxis() {
        return nrOfArtworksAxis;
    }

    public void setNrOfArtworksAxis(NumberAxis nrOfArtworksAxis) {
        this.nrOfArtworksAxis = nrOfArtworksAxis;
    }

    public BarChart<String, Number> getArtistChart() {
        return artistChart;
    }

    public void setArtistChart(BarChart<String, Number> artistChart) {
        this.artistChart = artistChart;
    }

    public CategoryAxis getArtistAxis() {
        return artistAxis;
    }

    public void setArtistAxis(CategoryAxis artistAxis) {
        this.artistAxis = artistAxis;
    }

    public NumberAxis getNumberOfArtworksAxis() {
        return numberOfArtworksAxis;
    }

    public void setNumberOfArtworksAxis(NumberAxis numberOfArtworksAxis) {
        this.numberOfArtworksAxis = numberOfArtworksAxis;
    }


    public String selectImageFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        return file != null ? file.toURI().toString() : "";
    }


    public String selectSaveFile(String title, String ext, String desc) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(desc, ext));
        File file = fileChooser.showSaveDialog(null);
        return file != null ? file.getAbsolutePath() : "";
    }

    public void displayStatistics(Map<String, Integer> typeData, Map<String, Integer> artistData) {
        typeChart.getData().clear();
        artistChart.getData().clear();

        XYChart.Series<String, Number> typeSeries = new XYChart.Series<>();
        typeData.forEach((type, count) -> typeSeries.getData().add(new XYChart.Data<>(type, count)));
        typeChart.getData().add(typeSeries);

        XYChart.Series<String, Number> artistSeries = new XYChart.Series<>();
        artistData.forEach((artist, count) -> artistSeries.getData().add(new XYChart.Data<>(artist, count)));
        artistChart.getData().add(artistSeries);
    }








}
