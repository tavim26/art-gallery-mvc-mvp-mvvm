package View;

import Model.ViewModel.GalleryViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenebuilder/main_view.fxml"));
        Parent root = loader.load();

        GalleryView view = loader.getController();
        GalleryViewModel viewModel = new GalleryViewModel();

        new Controller.GalleryController(viewModel, view);

        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    public static void main(String[] args)
    {
        launch(args);
    }
}