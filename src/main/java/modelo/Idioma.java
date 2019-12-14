package modelo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType
public class Idioma extends Conocimiento {

	private StringProperty certificacion;

	public Idioma(String denominacion, Nivel nivel, String certificacion) {
		super(denominacion, nivel);
		this.certificacion = new SimpleStringProperty(this, "certificacion", certificacion);
	}

	public Idioma() {
		this(null, null, null);
	}

	public final StringProperty certificacionProperty() {
		return this.certificacion;
	}

	@XmlAttribute(name = "certificado")
	public final String getCertificacion() {
		return this.certificacionProperty().get();
	}

	public final void setCertificacion(final String certificacion) {
		this.certificacionProperty().set(certificacion);
	}

}
