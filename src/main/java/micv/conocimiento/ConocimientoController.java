package micv.conocimiento;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import modelo.Conocimiento;
import modelo.Idioma;
import modelo.Nivel;

public class ConocimientoController implements Initializable {
//	Model
	private static ListProperty<Conocimiento> conocimientos = new SimpleListProperty<>(ConocimientoController.class, "conocimientos", FXCollections.observableArrayList());

//	View
	@FXML
	private HBox view;

	@FXML
	private TableView<Conocimiento> conocimientoTable;

	@FXML
	private TableColumn<Conocimiento, String> denominacionColumn;

	@FXML
	private TableColumn<Conocimiento, Nivel> nivelColumn;

	@FXML
	private Button añadirConocimientoButton;

	@FXML
	private Button añadirIdiomaButton;

	@FXML
	private Button eliminarButton;

	public ConocimientoController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConocimientoView.fxml"));
		loader.setController(this);
		
		
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Model
		conocimientos.bindBidirectional(MainController.getModel().habilidadesProperty());

//		Bindings		
		conocimientoTable.itemsProperty().bindBidirectional(conocimientos);
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		nivelColumn.setCellValueFactory(v -> v.getValue().nivelProperty());

	}

	@FXML
	void onAñadirConocimientoAction(ActionEvent event) {
		Dialog<Conocimiento> dialog = new Dialog<>();
		dialog.setTitle("Nuevo conocimiento");
		dialog.getDialogPane().setHeader(null);
		dialog.setResizable(true);

		ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
		ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

		TextField denominacion = new TextField();
		denominacion.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(denominacion, Priority.ALWAYS);

		ComboBox<Nivel> nivel = new ComboBox<Nivel>();
		nivel.getItems().addAll(Nivel.values());
		Button x = new Button("X");
		x.setOnAction(e -> nivel.getSelectionModel().select(null));
		HBox box = new HBox(5, nivel, x);

		GridPane gridPane = new GridPane();
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.addRow(0, new Label("Denominacion"), denominacion);
		gridPane.addRow(1, new Label("Nivel"), box);

		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == aceptarButtonType) {
				return new Conocimiento(denominacion.getText(), nivel.getSelectionModel().getSelectedItem());
			}
			return null;
		});

		Optional<Conocimiento> result = dialog.showAndWait();
		if (!denominacion.getText().equals("") && !nivel.getSelectionModel().isEmpty())
			result.ifPresent(Conocimiento -> {
				conocimientos.add(result.get());
			});

	}

	@FXML
	void onAñadirIdiomaAction(ActionEvent event) {

		Dialog<Idioma> dialog = new Dialog<>();
		dialog.setTitle("Nuevo conocimiento");
		dialog.getDialogPane().setHeader(null);
		dialog.setResizable(true);

		ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
		ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

		TextField denominacion = new TextField();
		denominacion.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(denominacion, Priority.ALWAYS);

		ComboBox<Nivel> nivel = new ComboBox<Nivel>();
		nivel.getItems().addAll(Nivel.values());
		Button x = new Button("X");
		x.setOnAction(e -> nivel.getSelectionModel().select(null));
		HBox box = new HBox(5, nivel, x);
		
		TextField certificacion = new TextField();

		GridPane gridPane = new GridPane();
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.addRow(0, new Label("Denominacion"), denominacion);
		gridPane.addRow(1, new Label("Nivel"), box);
		gridPane.addRow(2, new Label("Certificacion"), certificacion);

		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == aceptarButtonType) {
				return new Idioma(denominacion.getText(), nivel.getSelectionModel().getSelectedItem(), certificacion.getText());
			}
			return null;
		});

		Optional<Idioma> result = dialog.showAndWait();
		if (!denominacion.getText().equals("") && !nivel.getSelectionModel().isEmpty() && !certificacion.getText().equals(""))
			result.ifPresent(Conocimiento -> {
				conocimientos.add(result.get());
			});
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		if(conocimientoTable.getSelectionModel().getSelectedItem() != null) {
		ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
		ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
		Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar este conocimiento?", eliminar,
				cancelar);
		alert.showAndWait();
		if (alert.getResult() == eliminar) {
			conocimientos.remove(conocimientoTable.getSelectionModel().getSelectedItem());
		}
		}
	}
	
	public static void vaciar() {
		conocimientos.clear();
	}

	public static void rellenar() {
		conocimientos.set(MainController.getModel().getHabilidades());
	}
	
	public HBox getView() {
		return view;
	}
}
