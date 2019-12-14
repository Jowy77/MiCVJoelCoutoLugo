package micv.personal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import micv.MainController;
import modelo.Nacionalidad;
import modelo.Personal;

public class PersonalController implements Initializable {

//	Model
	private static ObjectProperty<Personal> model = new SimpleObjectProperty<>(PersonalController.class, "model");

	private ListProperty<String> paises;

//	View
	@FXML
	private GridPane view;
	@FXML
	private TextField DNIText;
	@FXML
	private TextField nombreText;
	@FXML
	private TextField apellidosText;
	@FXML
	private DatePicker fechaNacimientoPicker;
	@FXML
	private TextArea direccionText;
	@FXML
	private TextField codigoPostalText;
	@FXML
	private TextField localidadText;
	@FXML
	private ComboBox<String> paisComboBox;
	@FXML
	private ListView<Nacionalidad> nacionalidadList;
	@FXML
	private Button añadirButton;
	@FXML
	private Button quitarButton;

	public PersonalController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PersonalView.fxml"));
		loader.setController(this);

////		View
//		DNIText = new TextField();
//		nombreText = new TextField();
//		apellidosText = new TextField();
//		fechaNacimientoPicker = new DatePicker();
//		direccionText = new TextArea();
//		codigoPostalText = new TextField();
//		localidadText = new TextField();
//		paisComboBox = new ComboBox<>();
//		nacionalidadList = new ListView<>();

		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

//		Model
		model.bindBidirectional(MainController.getModel().personalProperty());

		paises = new SimpleListProperty<>(this, "paises", FXCollections.observableArrayList());

//		Bindings
		model.get().identificacionProperty().bindBidirectional(DNIText.textProperty());
		model.get().nombreProperty().bindBidirectional(nombreText.textProperty());
		model.get().apellidosProperty().bindBidirectional(apellidosText.textProperty());
		model.get().fechaNacimientoProperty().bindBidirectional(fechaNacimientoPicker.valueProperty());
		model.get().direccionProperty().bindBidirectional(direccionText.textProperty());
		model.get().codigoPostalProperty().bindBidirectional(codigoPostalText.textProperty());
		model.get().localidadProperty().bindBidirectional(localidadText.textProperty());
		model.get().paisProperty().bind(paisComboBox.getSelectionModel().selectedItemProperty());

		nacionalidadList.itemsProperty().bindBidirectional(model.get().nacionalidadProperty());

		paisComboBox.itemsProperty().bind(paises);

		refrescarPaises();

	}

	@FXML
	void onAñadirButtonAction(ActionEvent e) {
		dialogo();
	}

	private void dialogo() {
		List<Nacionalidad> nacionalidades = refrescarNacionalidades();

		ChoiceDialog<Nacionalidad> dialog = new ChoiceDialog<>(nacionalidades.get(0), nacionalidades);
		dialog.setTitle("Nueva nacionalidad");
		dialog.setHeaderText("Añadir nacionalidad");
		dialog.setContentText("Selecciona una nacionalidad: ");

		Optional<Nacionalidad> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (repetido(result) == false)
				model.get().getNacionalidad().add(result.get());
		}
	}

	private void refrescarPaises() {

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/recursos/paises.csv")));
			String linea;
			while ((linea = br.readLine()) != null) {
				paises.add(linea);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Nacionalidad> refrescarNacionalidades() {
		List<Nacionalidad> nacionalidades = new ArrayList<>();

		BufferedReader br;
		try {
			br = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/recursos/nacionalidades.csv")));
			String linea;
			while ((linea = br.readLine()) != null) {
				nacionalidades.add(new Nacionalidad(linea));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nacionalidades;
	}

	private boolean repetido(Optional<Nacionalidad> result) {
		boolean repetido = false;
		for (Nacionalidad nacionalidad : model.get().getNacionalidad()) {
			if (result.get().getDenominacion().equals(nacionalidad.getDenominacion())) {
				repetido = true;
				break;
			}
		}
		return repetido;
	}

	@FXML
	void onQuitarButtonAction(ActionEvent event) {
		try {
			model.get().getNacionalidad().remove(nacionalidadList.getSelectionModel().getSelectedIndex());
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public static void vaciar() {
		model.get().setIdentificacion("");
		model.get().setNombre("");
		model.get().setApellidos("");
		model.get().setFechaNacimiento(null);
		model.get().setDireccion("");
		model.get().setCodigoPostal("");
		model.get().setLocalidad("");
//		TODO limpiar paises
		model.get().getNacionalidad().clear();
	}
	
	public static void rellenar() {
		model.get().setIdentificacion(MainController.getModel().getPersonal().getIdentificacion());
		model.get().setNombre(MainController.getModel().getPersonal().getNombre());
		model.get().setApellidos(MainController.getModel().getPersonal().getApellidos());
		model.get().setFechaNacimiento(MainController.getModel().getPersonal().getFechaNacimiento());
		model.get().setDireccion(MainController.getModel().getPersonal().getDireccion());
		model.get().setCodigoPostal(MainController.getModel().getPersonal().getCodigoPostal());
		model.get().setLocalidad(MainController.getModel().getPersonal().getLocalidad());
//		TODO limpiar paises
		model.get().setNacionalidad(MainController.getModel().getPersonal().getNacionalidad());
	}

	public static ObjectProperty<Personal> getModel() {
		return model;
	}

	public GridPane getView() {
		return view;
	}
}
