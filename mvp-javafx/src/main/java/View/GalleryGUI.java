package View;

import Presenter.GalleryPresenter;
import Presenter.IGalleryGUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GalleryGUI implements IGalleryGUI, Initializable {

    private final GalleryPresenter presenter;

    @FXML private Button artistButton;
    @FXML private Button artworkButton;
    @FXML private AnchorPane artistPane;
    @FXML private AnchorPane artworkPane;

    @FXML private TableView<String> artistTable;
    @FXML private TextField nameTextField;
    @FXML private TextField birthplaceTextField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private TextField nationalityTextField;
    @FXML private ImageView artistPhoto;
    @FXML private Button addArtistButton;
    @FXML private Button editArtistButton;
    @FXML private Button deleteArtistButton;
    @FXML private TextField searchArtistTextField;

    @FXML private TableView<String> artworkTable;
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
    @FXML private Button saveToCsvButton;
    @FXML private Button saveToDocButton;
    @FXML private ImageView artworkImage1;
    @FXML private ImageView artworkImage2;

    @FXML private Slider priceSlider;


    public GalleryGUI()
    {
        this.presenter = new GalleryPresenter(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        artistButton.setOnAction(event -> clickArtistButton());
        artworkButton.setOnAction(event -> clickArtworkButton());
        artistPane.setVisible(true);
        artworkPane.setVisible(false);

        configureArtistTable();
        configureArtworkTable();

        artistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String[] data = newSelection.split(",");
                try {
                    presenter.populateArtistFields(data[0]);
                } catch (SQLException e) {
                    showError("Error loading artist data: " + e.getMessage());
                }
            } else {
                presenter.clearArtistFields();
            }
        });
        artworkTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String[] data = newSelection.split(",");
                try {
                    presenter.populateArtworkFields(data[0]);
                    presenter.displayArtworkImages(data[0]);
                } catch (SQLException e) {
                    showError("Error loading artwork data: " + e.getMessage());
                }
            } else {
                presenter.clearArtworkFields();
                artworkImage1.setImage(null);
                artworkImage2.setImage(null);
            }
        });

        searchArtistTextField.textProperty().addListener((obs, oldValue, newValue) ->
                presenter.searchArtistsByName(newValue));
        searchArtworkTextField.textProperty().addListener((obs, oldValue, newValue) ->
                presenter.searchArtworksByTitle(newValue));

        filterByArtistBox.valueProperty().addListener((obs, oldValue, newValue) ->
                presenter.applyFilters(filterByArtistBox.getValue(), filterByTypeBox.getValue(), String.valueOf(priceSlider.getValue())));

        filterByTypeBox.valueProperty().addListener((obs, oldValue, newValue) ->
                presenter.applyFilters(filterByArtistBox.getValue(), filterByTypeBox.getValue(), String.valueOf(priceSlider.getValue())));

        priceSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                presenter.applyFilters(filterByArtistBox.getValue(), filterByTypeBox.getValue(), String.valueOf(newValue.doubleValue())));


        presenter.initializeComboBoxItems();

        try {
            presenter.refreshArtists();
            presenter.refreshArtworks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

      
    }

    public void configureArtistTable()
    {
        TableColumn<String, String> nameColumn = (TableColumn<String, String>) artistTable.getColumns().get(0);
        TableColumn<String, String> birthdateColumn = (TableColumn<String, String>) artistTable.getColumns().get(1);
        TableColumn<String, String> birthplaceColumn = (TableColumn<String, String>) artistTable.getColumns().get(2);
        TableColumn<String, String> nationalityColumn = (TableColumn<String, String>) artistTable.getColumns().get(3);
        TableColumn<String, String> artworksColumn = (TableColumn<String, String>) artistTable.getColumns().get(4);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[0]));
        birthdateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[1]));
        birthplaceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[2]));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[3]));
        artworksColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(String.join(", ", presenter.getArtworksByArtist(cellData.getValue().split(",")[0])));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void configureArtworkTable() {
        TableColumn<String, String> titleColumn = (TableColumn<String, String>) artworkTable.getColumns().get(0);
        TableColumn<String, String> artistNameColumn = (TableColumn<String, String>) artworkTable.getColumns().get(1);
        TableColumn<String, String> typeColumn = (TableColumn<String, String>) artworkTable.getColumns().get(2);
        TableColumn<String, String> priceColumn = (TableColumn<String, String>) artworkTable.getColumns().get(3);
        TableColumn<String, String> creationYearColumn = (TableColumn<String, String>) artworkTable.getColumns().get(4);

        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[0]));
        artistNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[1]));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[2]));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[3]));
        creationYearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[4]));
    }


    @FXML
    private void clickAddArtist()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(addArtistButton.getScene().getWindow());
        String photoPath = selectedFile != null ? selectedFile.toURI().toString() : "";
        presenter.onAddArtistClicked(photoPath);
    }

    @FXML
    private void clickAddArtwork() {
        presenter.onAddArtworkClicked();
    }


    @FXML
    private void clickClearArtist() {
        presenter.clearArtistFields();
    }

    @FXML
    private void clickClearArtwork() {
        presenter.clearArtworkFields();
    }

    @FXML
    private void clickArtistButton()
    {
        artistPane.setVisible(true);
        artworkPane.setVisible(false);
    }

    @FXML
    private void clickArtworkButton()
    {
        artistPane.setVisible(false);
        artworkPane.setVisible(true);
    }



    @FXML
    private void clickEditArtist() {
        String selected = artistTable.getSelectionModel().getSelectedItem();
        String oldName = selected != null ? selected.split(",")[0] : "";
        presenter.onEditArtistClicked(oldName);
    }

    @FXML
    private void clickEditArtwork() {
        String selected = artworkTable.getSelectionModel().getSelectedItem();
        String oldTitle = selected != null ? selected.split(",")[0] : "";
        presenter.onEditArtworkClicked(oldTitle);
    }

    @FXML
    private void clickDeleteArtist() {
        String selectedArtist = artistTable.getSelectionModel().getSelectedItem();
        String name = selectedArtist != null ? selectedArtist.split(",")[0] : "";
        presenter.deleteArtist(name);
    }



    @FXML
    private void clickDeleteArtwork() {
        String selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();
        String title = selectedArtwork != null ? selectedArtwork.split(",")[0] : "";
        presenter.deleteArtwork(title);
    }

    @FXML
    private void clickAddImage() {
        String selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();
        String title = selectedArtwork != null ? selectedArtwork.split(",")[0] : "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(addImageButton.getScene().getWindow());
        String imagePath = selectedFile != null ? selectedFile.toURI().toString() : "";
        presenter.addArtworkImage(title, imagePath);
    }

    @FXML
    private void clickSaveToCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(saveToCsvButton.getScene().getWindow());
        presenter.exportArtworksToCSV(file.getAbsolutePath());
    }

    @FXML
    private void clickSaveToDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(saveToDocButton.getScene().getWindow());
        presenter.exportArtworksToText(file.getAbsolutePath());
    }





    // IGalleryGUI methods
    @Override
    public void displayArtists(List<String> artistData) {
        artistTable.setItems(FXCollections.observableArrayList(artistData));
    }

    @Override
    public void displaySuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void displayArtworks(List<String> artworkData) {
        artworkTable.setItems(FXCollections.observableArrayList(artworkData));
    }

    @Override
    public void displayFilteredArtworks(List<String> artworkData) {
        artworkTable.setItems(FXCollections.observableArrayList(artworkData));
    }

    @Override
    public void populateArtistFields(String name, String birthDate, String birthPlace, String nationality, String photo) {
        nameTextField.setText(name);
        birthplaceTextField.setText(birthPlace);
        birthdayDatePicker.setValue(birthDate.isEmpty() ? null : java.time.LocalDate.parse(birthDate));
        nationalityTextField.setText(nationality);
        artistPhoto.setImage(photo != null && !photo.isEmpty() ? new Image(photo) : null);
    }

    @Override
    public void populateArtworkFields(String title, String artistName, String type, String price, String year) {
        titleTextField.setText(title);
        artistComboBox.setValue(artistName);
        typeComboBox.setValue(type);
        priceTextField.setText(price);
        yearTextField.setText(year);
    }

    @Override
    public void clearArtistFields() {
        nameTextField.clear();
        birthplaceTextField.clear();
        birthdayDatePicker.setValue(null);
        nationalityTextField.clear();
        artistPhoto.setImage(null);
    }

    @Override
    public void clearArtworkFields() {
        titleTextField.clear();
        artistComboBox.setValue(null);
        typeComboBox.setValue(null);
        priceTextField.clear();
        yearTextField.clear();
        artworkImage1.setImage(null);
        artworkImage2.setImage(null);
    }

    @Override
    public void displayArtworkImages(List<String> imagePaths) {
        artworkImage1.setImage(imagePaths.size() > 0 ? new Image(imagePaths.get(0)) : null);
        artworkImage2.setImage(imagePaths.size() > 1 ? new Image(imagePaths.get(1)) : null);
    }


    @Override
    public void confirmExportSuccess(String filePath) {
        displaySuccess("Export was successful in the path: " + filePath);
    }

    @Override
    public void confirmSuccess(String message) {
        displaySuccess(message);
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void setArtistComboBoxItems(List<String> artistNames) {
        artistComboBox.setItems(FXCollections.observableArrayList(artistNames));
    }

    @Override
    public void setFilterByArtistBoxItems(List<String> artistNames) {
        filterByArtistBox.setItems(FXCollections.observableArrayList(artistNames));
    }

    @Override
    public void setFilterByTypeBoxItems(List<String> types) {
        filterByTypeBox.setItems(FXCollections.observableArrayList(types));
    }

    @Override
    public void setTypeComboBoxItems(List<String> types) {
        typeComboBox.setItems(FXCollections.observableArrayList(types));
    }



    @Override
    public String getArtistName() {
        return nameTextField.getText();
    }
    @Override
    public String getArtistBirthplace() {
        return birthplaceTextField.getText();
    }
    @Override
    public String getArtistNationality() {
        return nationalityTextField.getText();
    }
    @Override
    public LocalDate getArtistBirthday() {
        return birthdayDatePicker.getValue();
    }
    @Override
    public String getArtistPhoto() {
        return artistPhoto.getImage() != null ? artistPhoto.getImage().getUrl() : "";
    }

    @Override
    public String getArtworkTitle() {
        return titleTextField.getText();
    }
    @Override
    public String getArtworkArtist() {
        return artistComboBox.getValue();
    }
    @Override
    public String getArtworkType() {
        return typeComboBox.getValue();
    }
    @Override
    public String getArtworkPrice() {
        return priceTextField.getText();
    }
    @Override
    public String getArtworkYear() {
        return yearTextField.getText();
    }



}