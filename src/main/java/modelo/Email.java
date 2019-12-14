package modelo;

import javax.xml.bind.annotation.XmlAttribute;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Email {
	private StringProperty direccion;

	public Email(String direccion) {
		this.direccion = new SimpleStringProperty(this, "direccion", direccion);
	}

	public Email() {
		this(null);
	}

	public final StringProperty direccionProperty() {
		return this.direccion;
	}

	@XmlAttribute
	public final String getDireccion() {
		return this.direccionProperty().get();
	}

	public final void setDireccion(final String direccion) {
		this.direccionProperty().set(direccion);
	}

}
