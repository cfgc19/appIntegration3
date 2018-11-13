package classesJPA;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="LANGUAGES_TABLE")
public class Languages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "languagesID")
	private Long id;
	private String language;	
//	@ManyToMany(mappedBy="languages")
//	private Set<Serie> serie;
	
	public Languages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

//	@XmlTransient
//	public Set<Serie> getSerie() {
//		return serie;
//	}
//
//	public void setSerie(Set<Serie> serie) {
//		this.serie = serie;
//	}

	

	@Override
	public String toString() {
		return "Languages [language=" + language + "]";
	}

	public Languages(String language) {
		super();
		this.language = language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Languages other = (Languages) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		return true;
	}
	
	
}
