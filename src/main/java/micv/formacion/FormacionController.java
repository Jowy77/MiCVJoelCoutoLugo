package micv.formacion;

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
import modelo.Titulo;

public class FormacionController implements Initializable {

//	Model
	private static ListProperty<Titulo> titulos = new SimpleListProperty<>(FormacionController.class, "titulos", FXCollections.observableArrayList());

	
//	View
	@FXML
	private HBox view;
	@FXML
	private TableView<Titulo> formacionTable;
	@FXML
	private TableColumn<Titulo, LocalDate> desdeColumn;
	@FXML
	private TableColumn<Titulo, LocalDate> hastaColumn;
	@FXML
	private TableColumn<Titulo, String> denominacionColumn;
	@FXML
	private TableColumn<Titulo, String> organizadorColumn;
	@FXML
	private Button añadirButton;
	@FXML
	private Button quitarButton;

	public FormacionController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormacionView.fxml"));
		loader.setController(this);

		
//		View
		formacionTable = new TableView<Titulo>();
		desdeColumn = new TableColumn<Titulo, LocalDate>();
		hastaColumn = new TableColumn<Titulo, LocalDate>();
		denominacionColumn = new TableColumn<Titulo, String>();
		organizadorColumn = new TableColumn<Titulo, String>();
		añadirButton = new Button();
		quitarButton = new Button();

		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Model
		titulos.bindBidirectional(MainController.getModel().FormacionProperty());

//		Bindings
		formacionTable.itemsProperty().bindBidirectional(titulos);
		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		organizadorColumn.setCellValueFactory(v -> v.getValue().organizadorProperty());

	}

	@FXML
	void onAñadirButtonAction(ActionEvent e) {
		añadirDialog();
	}

	private void añadirDialog() {
		Dialog<Titulo> dialog = new Dialog<>();
		dialog.setTitle("Nuevo titulo");
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
		TextField organizadorText = new TextField();
		organizadorText.maxWidth(Double.MAX_VALUE);
		organizadorText.setPromptText("Organizador");
		GridPane.setHgrow(organizadorText, Priority.ALWAYS);
		DatePicker desde = new DatePicker();
		DatePicker hasta = new DatePicker();

		gridPane.addRow(0, new Label("Denominacion: "), denominacionText);
		gridPane.addRow(1, new Label("Organizador: "), organizadorText);
		gridPane.addRow(2, new Label("Desde: "), desde);
		gridPane.addRow(3, new Label("Hasta: "), hasta);

		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == aceptarButtonType) {
				return new Titulo(desde.getValue(), hasta.getValue(), denominacionText.getText(),
						organizadorText.getText());
			}
			return null;
		});

		Optional<Titulo> result = dialog.showAndWait();
		if (!denominacionText.getText().equals("") && !organizadorText.getText().equals("") && desde.getValue() != null
				&& hasta.getValue() != null) {
			result.ifPresent(titulo -> {

				titulos.add(result.get());

			});
		}
	}

	@FXML
	void onQuitarButtonAction(ActionEvent e) {
		if (formacionTable.getSelectionModel().getSelectedItem() != null) {
			ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
			ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar este titulo?", eliminar,
					cancelar);
			alert.showAndWait();
			if (alert.getResult() == eliminar) {
				titulos.remove(formacionTable.getSelectionModel().getSelectedItem());
			}
		}
	}
	
	public static void vaciar() {
		titulos.clear();
	}
	
	public static void rellenar() {
		titulos.set(MainController.getModel().getFormacion());
	}
	
	public HBox getView() {
		return view;
	}
}
