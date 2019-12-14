package micv;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import micv.conocimiento.ConocimientoController;
import micv.contacto.ContactoController;
import micv.experiencia.ExperienciaController;
import micv.formacion.FormacionController;
import micv.personal.PersonalController;
import modelo.CV;

public class MainController implements Initializable {

	private PersonalController personalController;
	private ContactoController contactoController;
	private FormacionController formacionController;
	private ExperienciaController experienciaController;
	private ConocimientoController conocimientoController;

//	Model
	private static CV model;

	private File fichero;

//	View
	@FXML
	private BorderPane view;

	@FXML
	private Tab personalTab;

	@FXML
	private Tab contactoTab;

	@FXML
	private Tab formacionTab;

	@FXML
	private Tab experienciaTab;

	@FXML
	private Tab conocimientoTab;

	@FXML
	private MenuItem nuevoItem;

	@FXML
	private MenuItem abrirItem;

	@FXML
	private MenuItem guardarItem;

	@FXML
	private MenuItem guardarComoItem;

	@FXML
	private MenuItem salirItem;

	@FXML
	private MenuItem acercaItem;

	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new CV();

		try {
			personalController = new PersonalController();
			contactoController = new ContactoController();
			formacionController = new FormacionController();
			experienciaController = new ExperienciaController();
			conocimientoController = new ConocimientoController();

		} catch (IOException e) {
			alertException(e, "Inicializar");
		}

		personalTab.setContent(personalController.getView());
		contactoTab.setContent(contactoController.getView());
		formacionTab.setContent(formacionController.getView());
		experienciaTab.setContent(experienciaController.getView());
		conocimientoTab.setContent(conocimientoController.getView());
	}

	@FXML
	void onAbrirAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir...");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("Todo los Archivos", "*.*"));
		fichero = fileChooser.showOpenDialog(MiCVApp.getPrimaryStage());
		if (fichero != null) {
			try {
				model = CV.load(fichero);
				PersonalController.rellenar();
				FormacionController.rellenar();
				ExperienciaController.rellenar();
				ContactoController.rellenar();
				ConocimientoController.rellenar();
			} catch (JAXBException e) {
				alertException(e, "abrir el fichero : " + fichero.getPath());
			}
		}
	}

	public void alertException(Exception e, String accion) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("MiCVApp");
		alert.setHeaderText("Exepcion");
		alert.setContentText("No se pudo " + accion);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("La exepcion fue:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	@FXML
	void onGuardarAction(ActionEvent event) {
		if (fichero == null) {
			guardarComo();
		} else {
			try {
				model.save(fichero);
			} catch (JAXBException e) {
				alertException(e, "guardar el fichero : " + fichero.getPath());
			}
		}
	}

	@FXML
	void onGuardarComoAction(ActionEvent event) {
		guardarComo();

	}

	public void guardarComo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar como...");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("Todo los Archivos", "*.*"));
		fichero = fileChooser.showSaveDialog(MiCVApp.getPrimaryStage());
		if (fichero != null) {
			try {
				model.save(fichero);
			} catch (JAXBException e) {
				alertException(e, "guardar el fichero : " + fichero.getPath());
			}
		}
	}

	@FXML
	void onNuevoAction(ActionEvent event) {
		fichero = null;
		model = new CV();
		PersonalController.vaciar();
		FormacionController.vaciar();
		ExperienciaController.vaciar();
		ConocimientoController.vaciar();
		ContactoController.vaciar();
	}

	@FXML
	void onSalirItem(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("MiCVApp");
		alert.setHeaderText("Salir");
		alert.setContentText("Seguro que desea salir?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			MiCVApp.getPrimaryStage().close();
		}
	}

	public BorderPane getView() {
		return view;
	}

	public static CV getModel() {
		return model;
	}

}
