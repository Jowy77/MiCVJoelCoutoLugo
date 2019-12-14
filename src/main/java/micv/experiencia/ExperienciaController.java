package micv.experiencia;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import micv.MainController;
import modelo.Experiencia;

public class ExperienciaController implements Initializable {

//	Model
	private static ListProperty<Experiencia> experiencias = new SimpleListProperty<>(ExperienciaController.class, "experiencias", FXCollections.observableArrayList());

//	View

	@FXML
	private HBox view;

	@FXML
	private TableView<Experiencia> experiencaTable;

	@FXML
	private TableColumn<Experiencia, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Experiencia, String> denominacionColumn;

	@FXML
	private TableColumn<Experiencia, String> empleadorColumn;

	@FXML
	private Button añadirButton;

	@FXML
	private Button eliminarButton;

	public ExperienciaController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExperiencaView.fxml"));
		loader.setController(this);		
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Model
		experiencias.bindBidirectional(MainController.getModel().experienciasProperty());
		
//		Bindings
		experiencaTable.itemsProperty().bindBidirectional(experiencias);
		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		empleadorColumn.setCellValueFactory(v -> v.getValue().empleadorProperty());

	}

	@FXML
	void onAñadirAction(ActionEvent event) {
		añadirDialog();
	}

	private void añadirDialog() {
		Dialog<Experiencia> dialog = new Dialog<>();
		dialog.setTitle("Nueva Experienca");
		dialog.setResizable(true);
		dialog.getDialogPane().setHeader(null);

		ButtonType aceptarButtonType = new ButtonType("Crear", ButtonData.OK_DONE);
		ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		gridPane.setPadding(new Insets(5, 5, 5, 5));

		TextField denominacionText = new TextField();
		denominacionText.maxWidth(Double.MAX_VALUE);
		denominacionText.setPromptText("Denominacion");
		GridPane.setHgrow(denominacionText, Priority.ALWAYS);
		TextField empleadorText = new TextField();
		empleadorText.maxWidth(Double.MAX_VALUE);
		empleadorText.setPromptText("Empleador");
		GridPane.setHgrow(empleadorText, Priority.ALWAYS);
		DatePicker desde = new DatePicker();
		DatePicker hasta = new DatePicker();

		gridPane.addRow(0, new Label("Denominacion: "), denominacionText);
		gridPane.addRow(1, new Label("Empleador: "), empleadorText);
		gridPane.addRow(2, new Label("Desde: "), desde);
		gridPane.addRow(3, new Label("Hasta: "), hasta);

		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == aceptarButtonType) {
				return new Experiencia(desde.getValue(), hasta.getValue(), denominacionText.getText(),
						empleadorText.getText());
			}
			return null;
		});

		Optional<Experiencia> result = dialog.showAndWait();

		if (!denominacionText.getText().equals("") && !empleadorText.getText().equals("") && desde.getValue() != null
				&& hasta.getValue() != null) {
			result.ifPresent(titulo -> {
				experiencias.add(result.get());
			});
		}
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		if (experiencaTable.getSelectionModel().getSelectedItem() != null) {
			ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
			ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar esta esperiencia?", eliminar,
					cancelar);
			alert.showAndWait();
			if (alert.getResult() == eliminar) {
				experiencias.remove(experiencaTable.getSelectionModel().getSelectedItem());
			}
		}
	}
	
	public static void vaciar() {
		experiencias.clear();
	}

	public static void rellenar() {
		experiencias.set(MainController.getModel().getExperiencias());
	}
	
	public HBox getView() {
		return view;
	}

}
