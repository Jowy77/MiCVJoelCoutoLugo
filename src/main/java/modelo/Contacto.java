package modelo;

import javax.xml.bind.annotation.XmlElement;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contacto {
	private ListProperty<Telefono> telefonos;
	private ListProperty<Email> emails;
	private ListProperty<Web> web;

	public Contacto() {
		telefonos = new SimpleListProperty<>(this, "telefonos", FXCollections.observableArrayList());
		emails = new SimpleListProperty<>(this, "emails", FXCollections.observableArrayList());
		web = new SimpleListProperty<>(this, "web", FXCollections.observableArrayList());
	}

	public final ListProperty<Telefono> telefonosProperty() {
		return this.telefonos;
	}

	@XmlElement
	public final ObservableList<Telefono> getTelefonos() {
		return this.telefonosProperty().get();
	}

	public final void setTelefonos(final ObservableList<Telefono> telefonos) {
		this.telefonosProperty().set(telefonos);
	}

	public final ListProperty<Email> emailsProperty() {
		return this.emails;
	}

	@XmlElement
	public final ObservableList<Email> getEmails() {
		return this.emailsProperty().get();
	}

	public final void setEmail(final ObservableList<Email> emails) {
		this.emailsProperty().set(emails);
	}

	public final ListProperty<Web> webProperty() {
		return this.web;
	}

	@XmlElement
	public final ObservableList<Web> getWeb() {
		return this.webProperty().get();
	}

	public final void setWeb(final ObservableList<Web> web) {
		this.webProperty().set(web);
	}

}
