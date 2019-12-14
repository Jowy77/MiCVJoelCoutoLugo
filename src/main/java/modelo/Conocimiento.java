package modelo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlSeeAlso({Idioma.class})
public class Conocimiento {

	private StringProperty denominacion;
	private ObjectProperty<Nivel> nivel;

	public Conocimiento(String denominacion, Nivel nivel) {
		this.denominacion = new SimpleStringProperty(this, "denominacion", denominacion);
		this.nivel = new SimpleObjectProperty<>(this, "nivel", nivel);
	}

	public Conocimiento() {
		this(null, null);
	}

	public final StringProperty denominacionProperty() {
		return this.denominacion;
	}

	@XmlAttribute
	public final String getDenominacion() {
		return this.denominacionProperty().get();
	}

	public final void setDenominacion(final String denominacion) {
		this.denominacionProperty().set(denominacion);
	}

	public final ObjectProperty<Nivel> nivelProperty() {
		return this.nivel;
	}

	@XmlAttribute
	public final Nivel getNivel() {
		return this.nivelProperty().get();
	}

	public final void setNivel(final Nivel nivel) {
		this.nivelProperty().set(nivel);
	}

}
