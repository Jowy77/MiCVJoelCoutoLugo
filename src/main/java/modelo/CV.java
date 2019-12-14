package modelo;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement
@XmlType
public class CV {
	private ObjectProperty<Personal> personal;
	private ObjectProperty<Contacto> contacto;
	private ListProperty<Titulo> formacion;
	private ListProperty<Experiencia> experiencias;
	private ListProperty<Conocimiento> habilidades;

	public CV() {
		personal = new SimpleObjectProperty<>(this, "personal", new Personal());
		contacto = new SimpleObjectProperty<>(this, "contacto", new Contacto());
		formacion = new SimpleListProperty<>(this, "formacion", FXCollections.observableArrayList());
		experiencias = new SimpleListProperty<>(this, "experiencias", FXCollections.observableArrayList());
		habilidades = new SimpleListProperty<>(this, "habilidades", FXCollections.observableArrayList());
	}

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}

	@XmlElement
	public final Personal getPersonal() {
		return this.personalProperty().get();
	}
	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}

	@XmlElement
	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}

	public final ListProperty<Titulo> FormacionProperty() {
		return this.formacion;
	}

	@XmlElement(name = "Titulo")
	@XmlElementWrapper(name = "Formacion")
	public final ObservableList<Titulo> getFormacion() {
		return this.FormacionProperty().get();
	}

	public final void setFormacion(final ObservableList<Titulo> titulo) {
		this.FormacionProperty().set(titulo);
	}

	public final ListProperty<Experiencia> experienciasProperty() {
		return this.experiencias;
	}

	@XmlElement(name = "Experiencia")
	@XmlElementWrapper(name = "Experiencias")
	public final ObservableList<Experiencia> getExperiencias() {
		return this.experienciasProperty().get();
	}

	public final void setExperiencias(final ObservableList<Experiencia> experiencia) {
		this.experienciasProperty().set(experiencia);
	}

	public final ListProperty<Conocimiento> habilidadesProperty() {
		return this.habilidades;
	}

	@XmlElement(name = "Conocimiento")
	@XmlElementWrapper(name = "Habilidades")
	public final ObservableList<Conocimiento> getHabilidades() {
		return this.habilidadesProperty().get();
	}

	public final void setHabilidades(final ObservableList<Conocimiento> conocimiento) {
		this.habilidadesProperty().set(conocimiento);
	}

	public void save(File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(CV.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, file);
	}

	public static CV load(File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(CV.class);
		Unmarshaller marshaller = context.createUnmarshaller();
		return (CV) marshaller.unmarshal(file);
	}
}
