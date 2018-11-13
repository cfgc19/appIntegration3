package classesJPA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import classesJPA.Cast;
import classesJPA.Episode;
import classesJPA.Rating;

@Entity
@Table(name="SERIE_TABLE")
public class Serie implements Serializable {
	private static final long serialVersionUID = 1L;
	// we use this generation type to match that of SQLWriteStudents
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serieid")
	private Long id;
	private String serieName;
	private String type;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Rating> rating;
    private String description;
    private String narrator;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Characters> characters;
    private String musicalTheme;
    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Writers> writers;
    private String nominations;
    private String spinOff;
    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProgramCreators> programCreators;
    private String awards;
    private String network;
    private String firstEpisode;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Episode> episode;
    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Languages> languages;
	@ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Profiles> profiles;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((serieName == null) ? 0 : serieName.hashCode());
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
		Serie other = (Serie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (serieName == null) {
			if (other.serieName != null)
				return false;
		} else if (!serieName.equals(other.serieName))
			return false;
		return true;
	}
	public Serie() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Serie(String serieName, String type, String description, String narrator, String musicalTheme,
			String nominations, String spinOff, String awards, String network, String firstEpisode) {
		super();
		this.serieName = serieName;
		this.type = type;
		this.description = description;
		this.narrator = narrator;
		this.musicalTheme = musicalTheme;
		this.nominations = nominations;
		this.spinOff = spinOff;
		this.awards = awards;
		this.network = network;
		this.firstEpisode = firstEpisode;
		episode = new HashSet();
		characters = new HashSet();
		rating = new HashSet();
		writers = new HashSet();
		programCreators = new HashSet();		
		profiles= new HashSet();
		languages = new HashSet();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSerieName() {
		return serieName;
	}
	public void setSerieName(String serieName) {
		this.serieName = serieName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getNarrator() {
		return narrator;
	}
	public void setNarrator(String narrator) {
		this.narrator = narrator;
	}
	
	public String getMusicalTheme() {
		return musicalTheme;
	}
	public void setMusicalTheme(String musicalTheme) {
		this.musicalTheme = musicalTheme;
	}

	public String getNominations() {
		return nominations;
	}
	public void setNominations(String nominations) {
		this.nominations = nominations;
	}
	public String getSpinOff() {
		return spinOff;
	}
	public void setSpinOff(String spinOff) {
		this.spinOff = spinOff;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getFirstEpisode() {
		return firstEpisode;
	}
	public void setFirstEpisode(String firstEpisode) {
		this.firstEpisode = firstEpisode;
	}

	public Set<Rating> getRating() {
		return rating;
	}
	public void setRating(Set<Rating> rating) {
		this.rating = rating;
	}

	public Set<Characters> getCharacter() {
		return characters;
	}
	public void setCharacter(Set<Characters> character) {
		this.characters = character;
	}
	public Set<Writers> getWriters() {
		return writers;
	}
	public void setWriters(Set<Writers> writers) {
		this.writers = writers;
	}
	public Set<ProgramCreators> getProgramCreators() {
		return programCreators;
	}
	public void setProgramCreators(Set<ProgramCreators> programCreators) {
		this.programCreators = programCreators;
	}
	public Set<Episode> getEpisode() {
		return episode;
	}
	public void setEpisode(Set<Episode> episode) {
		this.episode = episode;
	}
	public Set<Languages> getLanguages() {
		return languages;
	}
	public void setLanguages(Set<Languages> languages) {
		this.languages = languages;
	}
	public Set<Profiles> getProfiles() {
		return profiles;
	}
	public void setProfiles(Set<Profiles> profiles) {
		this.profiles = profiles;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Serie [id=" + id + ", serieName=" + serieName + ", type=" + type + ", description=" + description
				+ ", narrator=" + narrator + ", musicalTheme=" + musicalTheme + ", nominations=" + nominations
				+ ", spinOff=" + spinOff + ", awards=" + awards + ", network=" + network + ", firstEpisode="
				+ firstEpisode + "]";
	}   
	
}
