package classesJPA;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="CHARACTERS_TABLE")
public class Characters implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "charactersID")
	private Long id;
	private String name;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Cast cast;
//	@ManyToOne
//	private Serie serie;
	
//	@XmlTransient
//	public Serie getSerie() {
//		return serie;
//	}
//
//	public void setSerie(Serie serie) {
//		this.serie = serie;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Characters() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Characters(String name) {
		super();
		this.name = name;

	}

	public Cast getCast() {
		return cast;
	}

	public void setCast(Cast cast) {
		this.cast = cast;
	}

	

	@Override
	public String toString() {
		return "Characters [name=" + name +  "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
