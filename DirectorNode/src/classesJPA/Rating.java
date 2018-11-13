package classesJPA;

import java.io.Serializable;

import java.math.BigDecimal;

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
@Table(name="RATING_TABLE")
public class Rating implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ratingID")
	private Long id;
	private BigDecimal value;
	private String source;
//	@ManyToOne
//	private Serie serie;
	
	public Rating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getValue() {
		return value;
	}

	public String getSource() {
		return source;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

//	@XmlTransient
//	public Serie getSerie() {
//		return serie;
//	}
//	public void setSerie(Serie serie) {
//		this.serie = serie;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Rating(BigDecimal value, String source) {
		super();
		this.value = value;
		this.source = source;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "Rating [id=" + id + ", value=" + value + ", source=" + source + "]";
	}





	
	
}
