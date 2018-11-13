package classesJPA;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="EPISODE_TABLE")
public class Episode implements Serializable{
	private static final long serialVersionUID = 1L;
	// we use this generation type to match that of SQLWriteStudents
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "episodeID")
	private Long id;
	private String title;
	private BigInteger season;
	private BigInteger number;
	private String date;
//	@ManyToOne
//	private Serie serie;

	public Episode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigInteger getSeason() {
		return season;
	}

	public void setSeason(BigInteger season) {
		this.season = season;
	}

	public BigInteger getNumber() {
		return number;
	}

	public void setNumber(BigInteger number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

//	public Serie getSerie() {
//		return serie;
//	}
//
//	public void setSerie(Serie serie) {
//		this.serie = serie;
//	}
//	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Episode other = (Episode) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public Episode(String title, BigInteger season, BigInteger number, String date) {
		super();
		this.title = title;
		this.season = season;
		this.number = number;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Episode [id=" + id + ", title=" + title + ", season=" + season + ", number=" + number + ", date=" + date
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}




}
