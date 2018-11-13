package ejbs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;
import javax.persistence.TypedQuery;

import classesJPA.Cast;
import classesJPA.Characters;
import classesJPA.Languages;
import classesJPA.Profiles;
import classesJPA.Serie;
import classesJPA.Writers;

@Remote
public interface BeanInterface {
	
	// METODOS DO REGISTO/LOGIN DO CAST
	public boolean emailAtorvalidation(String email);
	public void sendEmail(String addresses, String topic, String textMessage);
	public Cast actor(String email);
	public void changeNumberLogin(Cast cast);
	public boolean acceptRegist (String email);
	public void doRegist (Cast cast);
	
	//METODOS DE LISTAGEM
	public List<Serie> listSeries();
	public List<String> listSerienames();
	public List<String> listAllActors();
	public List<String> listAllLang();
	public List<String> listAllWriters();
	public List<String> listAllProfiles();
	public Serie allDataOfSerie(String name);
	public void newSerie(Serie serie);
	public void removeSerie(Serie serie); 
	public void UpdateSerie(HashMap<String, String> hmapMSG);
	public Cast allDataOfCast(String name);
	public void newCharacterinSerie(String castname, String character, String seriename);
	public void testing_atualizar(String name, String source, BigDecimal value);
	public void testing_remover(String name, String source, BigDecimal cenas);
	public void testing_adicionar(String name, String source, BigDecimal value);
	public Profiles allDataOfProfiles(String name);
	public Writers allDataOfWriters(String name);
	public Languages allDataOfLanguages(String name);
}
