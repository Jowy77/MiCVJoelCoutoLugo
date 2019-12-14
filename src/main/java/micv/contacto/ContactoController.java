package micv.contacto;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import micv.MainController;
import modelo.Contacto;
import modelo.Email;
import modelo.Telefono;
import modelo.TipoTelefono;
import modelo.Web;

public class ContactoController implements Initializable {

//	Model
	private static ObjectProperty<Contacto> model = new SimpleObjectProperty<>(ContactoController.class, "model");

//	View
	@FXML
	private SplitPane view;

	@FXML
	private TableView<Telefono> telefonosTable;

	@FXML
	private TableColumn<Telefono, String> numeroColumn;

	@FXML
	private TableColumn<Telefono, TipoTelefono> tipoColumn;

	@FXML
	private Button telefonoAñadirButton;

	@FXML
	private Button telefonoEliminarButton;

	@FXML
	private TableView<Email> emailsTable;

	@FXML
	private TableColumn<Email, String> emailColumn;

	@FXML
	private Button emailAñadirButton;

	@FXML
	private Button emailEliminarButton;

	@FXML
	private TableView<Web> websTable;

	@FXML
	private TableColumn<Web, String> urlColumn;

	@FXML
	private Button webAñadirButton;

	@FXML
	private Button webEliminarButton;

	public ContactoController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoView.fxml"));
		loader.setController(this);		
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		model.bindBidirectional(MainController.getModel().contactoProperty());

//		Bindings
		telefonosTable.itemsProperty().bindBidirectional(model.get().telefonosProperty());
		numeroColumn.setCellValueFactory(v -> v.getValue().numeroProperty());
		tipoColumn.setCellValueFactory(v -> v.getValue().tipoTelefonoProperty());

		emailsTable.itemsProperty().bindBidirectional(model.get().emailsProperty());
		emailColumn.setCellValueFactory(v -> v.getValue().direccionProperty());

		websTable.itemsProperty().bindBidirectional(model.get().webProperty());
		urlColumn.setCellValueFactory(v -> v.getValue().urlProperty());

	}

	@FXML
	void onAddEmailAction(ActionEvent event) {
		añadirEmailDialog();
	}

	private void añadirEmailDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nuevo email");
		dialog.setHeaderText("Crear nueva direccion de correo");
		dialog.setContentText("E-mail: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (!result.get().equals("") && emailsRepetidos(result.get()) == false) {
				model.get().getEmails().add(new Email(result.get()));
			}
		}
	}

	private boolean emailsRepetidos(String direccion) {
		boolean repe = false;
		for (Email email : model.get().getEmails()) {
			if (direccion.equals(email.getDireccion())) {
				repe = true;
				break;
			}
		}
		return repe;
	}

	@FXML
	void onRemoveEmailAction(ActionEvent event) {
		eliminarEmailAlert();

	}

	private void eliminarEmailAlert() {
		if (emailsTable.getSelectionModel().getSelectedItem() != null) {
			ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
			ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar este email?", eliminar,
					cancelar);
			alert.showAndWait();
			if (alert.getResult() == eliminar) {
				model.get().getEmails().remove(emailsTable.getSelectionModel().getSelectedItem());
			}
		}
	}

	@FXML
	void onAddTelefonoAction(ActionEvent event) {
		añadirTelefonoDialog();
	}

	private void añadirTelefonoDialog() {
		Dialog<Telefono> dialog = new Dialog<>();
		dialog.setTitle("Nuevo telefono");
		dialog.setHeaderText("Introduzca el nuevo numero de telefono");

		ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
		ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		gridPane.setPadding(new Insets(5, 5, 5, 5));

		TextField numeroText = new TextField();
		numeroText.setPromptText("Numero de telefono");
		ComboBox<TipoTelefono> tipoComboBox = new ComboBox<TipoTelefono>();
		tipoComboBox.setPromptText("Seleccione un tipo");
		tipoComboBox.getItems().addAll(TipoTelefono.values());

		gridPane.addRow(0, new Label("Numero: "), numeroText);
		gridPane.addRow(1, new Label("Tipo: "), tipoComboBox);

		dialog.getDialogPane().setContent(gridPane);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == aceptarButtonType) {
				if (!numeroText.getText().equals("") && !tipoComboBox.getSelectionModel().isEmpty())
					return new Telefono(numeroText.getText(), tipoComboBox.getSelectionModel().getSelectedItem());
			}
			return null;
		});

		Optional<Telefono> result = dialog.showAndWait();
		result.ifPresent(telefono -> {
			;
			if (telefonosRepetidos(telefono) == false)
				model.get().getTelefonos().add(telefono);
		});
	}

	private boolean telefonosRepetidos(Telefono telefono) {
		boolean repe = false;
		for (Telefono t : model.get().getTelefonos()) {
			if (telefono.getNumero().equals(t.getNumero()) && telefono.getTipoTelefono().equals(t.getTipoTelefono())) {
				repe = true;
				break;
			}
		}
		return repe;
	}

	@FXML
	void onRemoveTelefonoAction(ActionEvent event) {
		eliminarTelefonoAlert();
	}

	private void eliminarTelefonoAlert() {
		if (telefonosTable.getSelectionModel().getSelectedItem() != null) {
			ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
			ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar este telefono?", eliminar,
					cancelar);
			alert.showAndWait();
			if (alert.getResult() == eliminar) {
				model.get().getTelefonos().remove(telefonosTable.getSelectionModel().getSelectedItem());
			}
		}
	}

	@FXML
	void onAddWebAction(ActionEvent event) {
		añadirWebDialog();
	}

	private void añadirWebDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nuevo web");
		dialog.setHeaderText("Crear nueva direccion web");
		dialog.setContentText("URL: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (!result.get().equals("") && websRepetidas(result.get()) == false) {
				model.get().getWeb().add(new Web(result.get()));
			}
		}

	}

	private boolean websRepetidas(String url) {
		boolean repe = false;
		for (Web web : model.get().getWeb()) {
			if (url.equals(web.getUrl())) {
				repe = true;
				break;
			}
		}
		return repe;
	}

	@FXML
	void onRemoveWebAction(ActionEvent event) {
		eliminarWebAlert();
	}

	private void eliminarWebAlert() {
		if (websTable.getSelectionModel().getSelectedItem() != null) {
			ButtonType eliminar = new ButtonType("Eliminar", ButtonData.OK_DONE);
			ButtonType cancelar = new ButtonType("Cancelar", ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "Seguro que quieres eliminar este web?", eliminar,
					cancelar);
			alert.showAndWait();
			if (alert.getResult() == eliminar) {
				model.get().getWeb().remove(websTable.getSelectionModel().getSelectedItem());
			}
		}
	}
	
	public static void vaciar() {
		model.get().getEmails().clear();
		model.get().getTelefonos().clear();
		model.get().getWeb().clear();
	}

	public static void rellenar() {
		model.get().setEmail(MainController.getModel().getContacto().getEmails());
		model.get().setTelefonos(MainController.getModel().getContacto().getTelefonos());
		model.get().setWeb(MainController.getModel().getContacto().getWeb());
	}
	
	public SplitPane getView() {
		return view;
	}
}
