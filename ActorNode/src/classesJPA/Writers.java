package classesJPA;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="WRITERS_TABLE")
public class Writers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "writersID")
	private Long id;
	private String writer;
//	@ManyToMany(mappedBy="writers")
//	private Set<Serie> serie;

	public Writers() {
		super();
		// TODO Auto-generated constructor stub
	}




//	public Set<Serie> getSerie() {
//		return serie;
//	}
//
//
//
//
//	public void setSerie(Set<Serie> serie) {
//		this.serie = serie;
//	}




	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public Writers(String writer) {
		super();
		this.writer = writer;
	}


	@Override
	public String toString() {
		return "Writers [writer=" + writer + "]";
	}

	
}
