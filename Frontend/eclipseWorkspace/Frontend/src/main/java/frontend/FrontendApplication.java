package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FrontendApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("OldView.fxml"));
		Scene scene = new Scene(parent);
		
		stage.setScene(scene);
		stage.setTitle("SafeDeleteForLaw");
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(); 
	}
}