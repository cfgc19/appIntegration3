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

@Entity
@Table(name="PROGRAMCREATORS_TABLE")
public class ProgramCreators implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "creatorsID")
	private Long id;
	private String programCreators;
//	@ManyToMany(mappedBy="programCreators")
//	private Set<Serie> serie;
	
	
	public ProgramCreators() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public Set<Serie> getSerie() {
//		return serie;
//	}
//
//	public void setSerie(Set<Serie> serie) {
//		this.serie = serie;
//	}

	public String getProgramCreators() {
		return programCreators;
	}

	public void setProgramCreators(String programCreators) {
		this.programCreators = programCreators;
	}

	public ProgramCreators(String programCreators) {
		super();
		this.programCreators = programCreators;
	}


	@Override
	public String toString() {
		return "ProgramCreators [programCreators=" + programCreators +  "]";
	}


	 
	
}
