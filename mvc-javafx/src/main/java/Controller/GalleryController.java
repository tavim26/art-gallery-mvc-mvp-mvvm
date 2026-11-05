package Controller;

import Model.ViewModel.GalleryViewModel;
import View.GalleryView;

import java.util.List;

public class GalleryController
{

    private final GalleryViewModel viewModel;
    private final GalleryView view;

    public GalleryController(GalleryViewModel viewModel, GalleryView view)
    {
        this.viewModel = viewModel;
        this.view = view;
        this.viewModel.addObserver(view);

        setupTableColumns();
        initializeEventHandlers();
        setupTableListeners();
        setupComboBoxes();

    }


    private void initializeEventHandlers()
    {

        view.getArtistButton().setOnAction(e -> showArtistPane());
        view.getArtworkButton().setOnAction(e -> showArtworkPane());
        view.getStatisticsButton().setOnAction(e -> showStatisticsPane());

        // Artist CRUD
        view.getAddArtistButton().setOnAction(e -> handleAddArtist());
        view.getEditArtistButton().setOnAction(e -> handleEditArtist());
        view.getDeleteArtistButton().setOnAction(e -> handleDeleteArtist());
        view.getClearArtistButton().setOnAction(e -> clearArtistFields());

        // Artwork CRUD
        view.getAddArtworkButton().setOnAction(e -> handleAddArtwork());
        view.getEditArtworkButton().setOnAction(e -> handleEditArtwork());
        view.getDeleteArtworkButton().setOnAction(e -> handleDeleteArtwork());
        view.getClearArtworkButton().setOnAction(e -> clearArtworkFields());

        // Export
        view.getSaveToCsvButton().setOnAction(e -> handleSaveToCsv());
        view.getSaveToDocButton().setOnAction(e -> handleSaveToDoc());


        // Imagini
        view.getAddImageButton().setOnAction(e -> handleAddImageToArtwork());

        // Filtrare/Search
        view.getSearchArtistTextField().textProperty().addListener((obs, old, val) -> searchArtist(val));
        view.getSearchArtworkTextField().textProperty().addListener((obs, old, val) -> searchArtwork(val));

        view.getFilterByArtistBox().valueProperty().addListener((obs, old, val) -> {
            if (val != null) filterArtworksByArtist(val);
        });
        view.getFilterByTypeBox().valueProperty().addListener((obs, old, val) -> {
            if (val != null) filterArtworksByType(val);
        });
        view.getPriceSlider().valueProperty().addListener((obs, old, val) -> {
            filterArtworksByPrice(val.doubleValue());
        });


    }


    private void setupTableColumns() {
        // ARTIST
        view.getNameColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        view.getBirthdayColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        view.getBirthplaceColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        view.getNationalityColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[3]));

        // ARTWORK
        view.getTitleColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        view.getArtistNameColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        view.getTypeColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        view.getPriceColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[3]));
        view.getCreationYearColumn().setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[4]));
    }


    private void setupTableListeners() {
        view.getArtistTable().getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                view.getNameTextField().setText(selected[0]);
                view.getBirthdayDatePicker().setValue(java.time.LocalDate.parse(selected[1]));
                view.getBirthplaceTextField().setText(selected[2]);
                view.getNationalityTextField().setText(selected[3]);

                String photoPath = selected[4];
                if (photoPath != null && !photoPath.isBlank()) {
                    view.getArtistPhoto().setImage(new javafx.scene.image.Image(photoPath));
                } else {
                    view.getArtistPhoto().setImage(null);
                }
            }
        });

        view.getArtworkTable().getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                view.getTitleTextField().setText(selected[0]);
                view.getArtistComboBox().setValue(selected[1]);
                view.getTypeComboBox().setValue(selected[2]);
                view.getPriceTextField().setText(selected[3]);
                view.getYearTextField().setText(selected[4]);

                List<String> paths = viewModel.getArtworkImagePaths(selected[0]);
                if (paths.size() > 0) {
                    view.getArtworkImage1().setImage(new javafx.scene.image.Image(paths.get(0)));

                } else {
                    view.getArtworkImage1().setImage(null);
                }

                if (paths.size() > 1) {
                    view.getArtworkImage2().setImage(new javafx.scene.image.Image(paths.get(1)));

                } else {
                    view.getArtworkImage2().setImage(null);
                }

            }
        });
    }

    private void setupComboBoxes()
    {
        List<String> artworkTypes = List.of("Painting", "Sculpture", "Photography");

        view.getTypeComboBox().setItems(javafx.collections.FXCollections.observableArrayList(artworkTypes));
        view.getFilterByTypeBox().setItems(javafx.collections.FXCollections.observableArrayList(artworkTypes));
    }





    private void showArtistPane()
    {
        view.getArtistPane().setVisible(true);
        view.getArtworkPane().setVisible(false);
        view.getStatisticsPane().setVisible(false);
    }

    private void showArtworkPane()
    {
        view.getArtistPane().setVisible(false);
        view.getArtworkPane().setVisible(true);
        view.getStatisticsPane().setVisible(false);
    }

    private void showStatisticsPane()
    {
        view.getArtistPane().setVisible(false);
        view.getArtworkPane().setVisible(false);
        view.getStatisticsPane().setVisible(true);
        generateStatistics();
    }

// === ARTIST ===

    private void handleAddArtist() {
        String photoPath = view.selectImageFile("Select Artist Photo");
        viewModel.addArtist(
                view.getNameTextField().getText(),
                view.getBirthdayDatePicker().getValue() != null ? view.getBirthdayDatePicker().getValue().toString() : "",
                view.getBirthplaceTextField().getText(),
                view.getNationalityTextField().getText(),
                photoPath
        );
    }


    private void handleEditArtist() {
        String[] selected = view.getArtistTable().getSelectionModel().getSelectedItem();
        if (selected == null) return;

        viewModel.updateArtist(
                selected[0],
                view.getNameTextField().getText(),
                view.getBirthdayDatePicker().getValue() != null ? view.getBirthdayDatePicker().getValue().toString() : "",
                view.getBirthplaceTextField().getText(),
                view.getNationalityTextField().getText()
        );
    }

    private void handleDeleteArtist()
    {
        viewModel.deleteArtist(view.getNameTextField().getText());
    }

    private void clearArtistFields()
    {
        view.getNameTextField().clear();
        view.getBirthdayDatePicker().setValue(null);
        view.getBirthplaceTextField().clear();
        view.getNationalityTextField().clear();
        view.getSearchArtistTextField().clear();
        view.getArtistPhoto().setImage(null);
    }

    private void searchArtist(String keyword)
    {
        List<String[]> results = (keyword == null || keyword.isBlank())
                ? viewModel.getArtist()
                : viewModel.searchArtist(keyword);
        view.getArtistTable().setItems(javafx.collections.FXCollections.observableArrayList(results));
    }

// === ARTWORK ===

    private void handleAddArtwork()
    {
        try {
            viewModel.addArtwork(
                    view.getTitleTextField().getText(),
                    view.getArtistComboBox().getValue(),
                    view.getTypeComboBox().getValue(),
                    Double.parseDouble(view.getPriceTextField().getText()),
                    Integer.parseInt(view.getYearTextField().getText())
            );
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void handleEditArtwork()
    {
        String[] selected = view.getArtworkTable().getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            viewModel.updateArtwork(
                    selected[0],
                    view.getTitleTextField().getText(),
                    view.getArtistComboBox().getValue(),
                    view.getTypeComboBox().getValue(),
                    Double.parseDouble(view.getPriceTextField().getText()),
                    Integer.parseInt(view.getYearTextField().getText())
            );
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void handleDeleteArtwork() {
        viewModel.deleteArtwork(view.getTitleTextField().getText());
    }

    private void clearArtworkFields() {
        view.getTitleTextField().clear();
        view.getArtistComboBox().setValue(null);
        view.getTypeComboBox().setValue(null);
        view.getPriceTextField().clear();
        view.getYearTextField().clear();
        view.getSearchArtworkTextField().clear();
        view.getFilterByArtistBox().setValue(null);
        view.getFilterByTypeBox().setValue(null);
        view.getArtworkImage1().setImage(null);
        view.getArtworkImage2().setImage(null);
    }

    private void searchArtwork(String keyword) {
        List<String[]> results = viewModel.searchArtwork(keyword);
        view.getArtworkTable().setItems(javafx.collections.FXCollections.observableArrayList(results));
    }

    private void filterArtworksByArtist(String artistName) {
        List<String[]> results = viewModel.filterArtworkByArtist(artistName);
        view.getArtworkTable().setItems(javafx.collections.FXCollections.observableArrayList(results));
    }

    private void filterArtworksByType(String type) {
        List<String[]> results = viewModel.filterArtworkByType(type);
        view.getArtworkTable().setItems(javafx.collections.FXCollections.observableArrayList(results));
    }

    private void filterArtworksByPrice(double maxPrice) {
        List<String[]> results = viewModel.filterArtworkByPrice(maxPrice);
        view.getArtworkTable().setItems(javafx.collections.FXCollections.observableArrayList(results));
    }

    private void handleAddImageToArtwork() {
        String artworkTitle = view.getTitleTextField().getText();
        String imagePath = view.selectImageFile("Select Artwork Image");
        if (!imagePath.isEmpty()) {
            viewModel.addImageToArtwork(artworkTitle, imagePath);
        }
    }


// === EXPORT ===

    private void handleSaveToCsv() {
        String path = view.selectSaveFile("Save as CSV", "*.csv", "CSV Files");
        if (!path.isEmpty()) viewModel.saveToCsv(path);
    }

    private void handleSaveToDoc() {
        String path = view.selectSaveFile("Save as Text Document", "*.txt", "Text Files");
        if (!path.isEmpty()) viewModel.saveToDoc(path);
    }


// === STATISTICS ===

    public void generateStatistics() {
        var typeData = viewModel.getArtworkCountsByType();
        var artistData = viewModel.getArtworkCountsByArtist();
        view.displayStatistics(typeData, artistData);
    }




}
