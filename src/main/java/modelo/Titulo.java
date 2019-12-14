package modelo;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType
public class Titulo {

	private ObjectProperty<LocalDate> desde;
	private ObjectProperty<LocalDate> hasta;
	private StringProperty denominacion;
	private StringProperty organizador;

	public Titulo(LocalDate desde, LocalDate hasta, String denominacion, String organizador) {
		this.desde = new SimpleObjectProperty<>(this, "desde", desde);
		this.hasta = new SimpleObjectProperty<>(this, "hasta", hasta);
		this.denominacion = new SimpleStringProperty(this, "denominacion", denominacion);
		this.organizador = new SimpleStringProperty(this, "organizador", organizador);
	}

	public Titulo() {
		this(null, null, null, null);
	}

	public final ObjectProperty<LocalDate> desdeProperty() {
		return this.desde;
	}
	
	@XmlAttribute
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public final LocalDate getDesde() {
		return this.desdeProperty().get();
	}

	public final void setDesde(final LocalDate desde) {
		this.desdeProperty().set(desde);
	}

	public final ObjectProperty<LocalDate> hastaProperty() {
		return this.hasta;
	}
	
	@XmlAttribute
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public final LocalDate getHasta() {
		return this.hastaProperty().get();
	}

	public final void setHasta(final LocalDate hasta) {
		this.hastaProperty().set(hasta);
	}

	public final StringProperty denominacionProperty() {
		return this.denominacion;
	}

	@XmlElement
	public final String getDenominacion() {
		return this.denominacionProperty().get();
	}

	public final void setDenominacion(final String denominacion) {
		this.denominacionProperty().set(denominacion);
	}

	public final StringProperty organizadorProperty() {
		return this.organizador;
	}

	@XmlElement
	public final String getOrganizador() {
		return this.organizadorProperty().get();
	}

	public final void setOrganizador(final String organizador) {
		this.organizadorProperty().set(organizador);
	}

}
