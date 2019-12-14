package micv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MiCVApp extends Application {

	private static Stage primaryStage;

	private MainController controller;

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.primaryStage = primaryStage;
		
		controller = new MainController();

		primaryStage.setTitle("MiCVApp");
		primaryStage.setScene(new Scene(controller.getView()));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}