package ejbs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;

import classesJPA.Cast;
import classesJPA.Characters;
import classesJPA.Languages;
import classesJPA.Profiles;
import classesJPA.ProgramCreators;
import classesJPA.Rating;
import classesJPA.Serie;
import classesJPA.Writers;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Session Bean implementation class Bean
 */
@Stateless
public class Bean implements BeanInterface {

	@PersistenceContext(name = "TestPersistence")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public Bean() {
		// TODO Auto-generated constructor stub
	}

	@Resource(name = "java:jboss/mail/gmail")
	private Session session;

	// METODOS UTILIZADOS PARA O REGISTO/LOGIN DO CAST

	public void sendEmail(String addresses, String topic, String textMessage) {

		try {

			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
			message.setSubject(topic);
			message.setText(textMessage);

			Transport.send(message);

		} catch (MessagingException e) {
			System.out.println("Email nao enviado.");
		}
	}

	public boolean acceptRegist(String email) {
		String jpql = "SELECT c.name FROM Cast c WHERE c.email=:email";

		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);

		typedQuery.setParameter("email", email);

		List<String> mylist = typedQuery.getResultList();
		System.out.println(mylist.size());
		boolean validation;
		if (mylist.size() == 0) {
			validation = true;
		} else {
			validation = false;
		}
		return validation;
	}

	public void doRegist(Cast cast) {

		em.persist(cast);

	}

	public void changeNumberLogin(Cast cast) {
		cast.setLogin(Long.valueOf(1));
		em.merge(cast);
	}

	public boolean emailAtorvalidation(String email) {
		String jpql = "SELECT c FROM Cast c WHERE c.email=:email";

		TypedQuery<Cast> typedQuery = em.createQuery(jpql, Cast.class);

		typedQuery.setParameter("email", email);
		List<Cast> list = typedQuery.getResultList();
		boolean login;
		if (list.size() == 0) {
			login = false;
		} else {
			login = true;
		}
		return login;
	}

	// METODOS PARA LISTAR COISAS

	public List<Serie> listSeries() {
		String jpql = "SELECT s FROM Serie s";
		// Create a (typed) query
		TypedQuery<Serie> typedQuery = em.createQuery(jpql, Serie.class);
		// Query and get result
		List<Serie> mylist = typedQuery.getResultList();
		return mylist;
	}

	public List<String> listSerienames() {
		String jpql = "SELECT s.serieName FROM Serie s";
		// Create a (typed) query
		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);
		// Query and get result
		List<String> mylist = typedQuery.getResultList();
		return mylist;
	}

	public List<String> listAllActors() {
		String jpql = "SELECT c.name FROM Cast c";
		// Create a (typed) query
		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);
		// Query and get result
		List<String> mylist = typedQuery.getResultList();
		return mylist;
	}
	
	public List<String> listAllWriters() {
		String jpql = "SELECT c.writer FROM Writers c";
		// Create a (typed) query
		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);
		// Query and get result
		List<String> mylist = typedQuery.getResultList();
		return mylist;
	}
	
	public List<String> listAllLang() {
		String jpql = "SELECT c.language FROM Languages c";
		// Create a (typed) query
		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);
		// Query and get result
		List<String> mylist = typedQuery.getResultList();
		return mylist;
	}
	public List<String> listAllProfiles() {
		String jpql = "SELECT c.profile FROM Profiles c";
		// Create a (typed) query
		TypedQuery<String> typedQuery = em.createQuery(jpql, String.class);
		// Query and get result
		List<String> mylist = typedQuery.getResultList();
		return mylist;
	}
	
	public Serie allDataOfSerie(String name) {
		String jpql = "SELECT s FROM Serie s WHERE s.serieName=:name";
		// Create a (typed) query
		TypedQuery<Serie> typedQuery = em.createQuery(jpql, Serie.class);
		// Set parameter
		typedQuery.setParameter("name", name);
		// Query and get result
		Serie serie = typedQuery.getSingleResult();

		return serie;
	}

	public Cast allDataOfCast(String name) {
		String jpql = "SELECT s FROM Cast s WHERE s.name=:name";
		// Create a (typed) query
		TypedQuery<Cast> typedQuery = em.createQuery(jpql, Cast.class);
		// Set parameter
		typedQuery.setParameter("name", name);
		// Query and get result
		Cast cast = typedQuery.getSingleResult();
		return cast;
	}

	public Cast actor(String email) {
		String jpql = "SELECT c FROM Cast c WHERE c.email=:email";

		TypedQuery<Cast> typedQuery = em.createQuery(jpql, Cast.class);

		typedQuery.setParameter("email", email);
		Cast cast = typedQuery.getSingleResult();

		return cast;
	}

	public void newSerie(Serie serie) {

		em.persist(serie);
	}

	public void removeSerie(Serie serie) {

		Serie serieRem = em.find(Serie.class, serie.getId());
		em.remove(serieRem);

	}
	public Writers allDataOfWriters(String name) {
		String jpql = "SELECT s FROM Writers s WHERE s.writer=:name";
		// Create a (typed) query
		TypedQuery<Writers> typedQuery = em.createQuery(jpql, Writers.class);
		// Set parameter
		typedQuery.setParameter("name", name);
		// Query and get result
		Writers writers = typedQuery.getSingleResult();

		return writers;
	}
	public Languages allDataOfLanguages(String name) {
		String jpql = "SELECT s FROM Languages s WHERE s.language=:name";
		// Create a (typed) query
		TypedQuery<Languages> typedQuery = em.createQuery(jpql, Languages.class);
		// Set parameter
		typedQuery.setParameter("name", name);
		// Query and get result
		Languages languages = typedQuery.getSingleResult();

		return languages;
	}
	
	
	public Profiles allDataOfProfiles(String name) {
		String jpql = "SELECT s FROM Profiles s WHERE s.profile=:name";
		// Create a (typed) query
		TypedQuery<Profiles> typedQuery = em.createQuery(jpql, Profiles.class);
		// Set parameter
		typedQuery.setParameter("name", name);
		// Query and get result
		Profiles profiles = typedQuery.getSingleResult();

		return profiles;
	}
	public void newCharacterinSerie(String castname, String character, String seriename) {
		Cast cast = allDataOfCast(castname);
		Characters chars = new Characters(character);
		chars.setCast(cast);
		em.persist(cast);
		em.persist(chars);

		Serie serie2 = allDataOfSerie(seriename);
		Set<Characters> characters = serie2.getCharacter();
		characters.add(chars);
		serie2.setCharacter(characters);
		em.merge(serie2);

	}

	public void UpdateSerie(HashMap<String, String> hmapMSG) {

		String serieName = hmapMSG.get("SerieName");
		System.out.println(serieName);
		Serie serie = allDataOfSerie(serieName);

		if (hmapMSG.get("Update").equals("Nome da série")) {
			String newName = hmapMSG.get("NewName");
			System.out.println(newName);
			serie.setSerieName(newName);
		} else if (hmapMSG.get("Update").equals("Tipo")) {
			String newType = hmapMSG.get("NewType");
			System.out.println(newType);
			serie.setType(newType);
		} else if (hmapMSG.get("Update").equals("Descrição")) {
			String newDescr = hmapMSG.get("NewDescription");
			System.out.println(newDescr);
			serie.setDescription(newDescr);
		} else if (hmapMSG.get("Update").equals("Narrador")) {
			String newNarr = hmapMSG.get("NewNarrator");
			System.out.println(newNarr);
			serie.setNarrator(newNarr);
		} else if (hmapMSG.get("Update").equals("Musica")) {
			String newMusic = hmapMSG.get("NewMusic");
			System.out.println(newMusic);
			serie.setMusicalTheme(newMusic);
		} else if (hmapMSG.get("Update").equals("Escritores")) {
			System.out.println(hmapMSG);
			Set<Writers> writersSet = serie.getWriters();
			List<String> allWriters=listAllWriters();
			if (hmapMSG.get("Acao").equals("adicionar")) {

				String writerNew = hmapMSG.get("Writer");
				
				Writers newWriter = new Writers(writerNew);
				if (!allWriters.contains(writerNew))
				{
					
					System.out.println(newWriter.getWriter());
					writersSet.add(newWriter);
					serie.setWriters(writersSet);
					em.persist(newWriter);
				}
				else
				{
					newWriter= allDataOfWriters(writerNew);
					writersSet.add(newWriter);
					serie.setWriters(writersSet);
					
					
				}
				

			} else if (hmapMSG.get("Acao").equals("eliminar")) {

				String writerOld = hmapMSG.get("Writer");

				for (Writers writer : writersSet) {
					if (writer.getWriter().equals(writerOld)) {
						writersSet.remove(writer);
						em.find(Writers.class, writer.getId());
						em.remove(writer);
					}
				}
				serie.setWriters(writersSet);

			} else if (hmapMSG.get("Acao").equals("atualizar")) {
				Set<Writers> writersSetNew = new HashSet<Writers>();
				String writerOld = hmapMSG.get("oldWriter");
				String writerNew = hmapMSG.get("newWriter");

				for (Writers writer : writersSet) {
					if (writer.getWriter().equals(writerOld)) {
						writer.setWriter(writerNew);
						em.merge(writer);
					}
				}
			}
		} else if (hmapMSG.get("Update").equals("Nomeações")) {
			String newNomin = hmapMSG.get("NewNomination");
			System.out.println(newNomin);
			serie.setNominations(newNomin);

		} else if (hmapMSG.get("Update").equals("Spin-Off")) {
			String newSpinOff = hmapMSG.get("NewSpinOff");
			System.out.println(newSpinOff);
			serie.setSpinOff(newSpinOff);

		} else if (hmapMSG.get("Update").equals("Prémios")) {
			String newAward = hmapMSG.get("NewAward");
			System.out.println(newAward);
			serie.setAwards(newAward);

		} else if (hmapMSG.get("Update").equals("Network")) {
			String newNet = hmapMSG.get("NewNetwork");
			System.out.println(newNet);
			serie.setNetwork(newNet);

		} else if (hmapMSG.get("Update").equals("Primeiro Episódio")) {
			String new1stEp = hmapMSG.get("New1stEpisode");
			System.out.println(new1stEp);
			serie.setFirstEpisode(new1stEp);

		} else if (hmapMSG.get("Update").equals("Idiomas")) {
			Set<Languages> langSet = serie.getLanguages();
			List<String> allLangs=listAllLang();
			if (hmapMSG.get("Acao").equals("adicionar")) {

				String langNew = hmapMSG.get("Lang");
				Languages newLanguage = new Languages(langNew);

				if (!allLangs.contains(langNew)){
					em.persist(newLanguage);
					langSet.add(newLanguage);
					serie.setLanguages(langSet);
					
				}
				else{
					newLanguage=allDataOfLanguages(langNew);
					langSet.add(newLanguage);
					serie.setLanguages(langSet);
				}
				
				

				

			} else if (hmapMSG.get("Acao").equals("eliminar")) {

				String langOld = hmapMSG.get("Lang");

				for (Languages lang : langSet) {
					if (lang.getLanguage().equals(langOld)) {
						langSet.remove(lang);
						em.find(Languages.class, lang.getId());
						em.remove(lang);
					}
				}

				serie.setLanguages(langSet);
			} else if (hmapMSG.get("Acao").equals("atualizar")) {

				String langOld = hmapMSG.get("oldLang");
				String langNew = hmapMSG.get("newLang");

				for (Languages lang : langSet) {
					if (lang.getLanguage().equals(langOld)) {
						lang.setLanguage(langNew);
						em.merge(lang);
					}
				}
			}
		} else if (hmapMSG.get("Update").equals("Redes sociais")) {
			Set<Profiles> profSet = serie.getProfiles();
			List<String> allProfiles=listAllProfiles();
			if (hmapMSG.get("Acao").equals("adicionar")) {
				String profNew = hmapMSG.get("Prof");
				Profiles new_profile = new Profiles(profNew);

				if (!allProfiles.contains(profNew)){
					profSet.add(new_profile);
					serie.setProfiles(profSet);
					em.persist(new_profile);
				}
				else{
					new_profile=allDataOfProfiles(profNew);
					profSet.add(new_profile);
					serie.setProfiles(profSet);

				}
				

			} else if (hmapMSG.get("Acao").equals("eliminar")) {

				String profOld = hmapMSG.get("Prof");

				for (Profiles profile : profSet) {
					if (profile.getProfile().equals(profOld)) {
						profSet.remove(profile);
						em.find(Profiles.class, profile.getId());
						em.remove(profile);
						
					}
				}
				serie.setProfiles(profSet);

			} else if (hmapMSG.get("Acao").equals("atualizar")) {

				String profOld = hmapMSG.get("oldProf");
				String profNew = hmapMSG.get("newProf");

				for (Profiles profile : profSet) {
					if (profile.getProfile().equals(profOld)) {
						profile.setProfile(profNew);
						em.merge(profile);
					}
				}
			}

		} else if (hmapMSG.get("Update").equals("Rating")) {

			Set<Rating> setRating = serie.getRating();
			if (hmapMSG.get("Acao").equals("adicionar")) {

				String sourceNew = hmapMSG.get("Source");
				String valueNew = hmapMSG.get("Value");
				BigDecimal valueNew1 = new BigDecimal(valueNew);

				Rating new_rating = new Rating(valueNew1, sourceNew);
				setRating.add(new_rating);
				serie.setRating(setRating);
				em.persist(new_rating);

			} else if (hmapMSG.get("Acao").equals("eliminar")) {

				String SourceOld = hmapMSG.get("Source");

				for (Rating rating : setRating) {
					if (rating.getSource().equals(SourceOld)) {
						setRating.remove(rating);
						em.find(Rating.class, rating.getId());
						em.remove(rating);
					}
				}
				serie.setRating(setRating);
			} else if (hmapMSG.get("Acao").equals("atualizar")) {

				String source = hmapMSG.get("Source");
				System.out.println("source: " + source);
				String valueNew = hmapMSG.get("Value");
				BigDecimal valueNew1 = new BigDecimal(valueNew);
				String name = serieName;
				System.out.println("value:" + valueNew1);
				for (Rating rating : setRating) {
					if (rating.getSource().equals(source)) {
						rating.setValue(valueNew1);
						em.merge(rating);
					}
				}
			}
		}
		em.merge(serie);
	}

	public void testing_atualizar(String name, String source, BigDecimal value) {
		String jpql = "SELECT r FROM Serie s join s.rating r WHERE s.serieName=:name AND r.source=:source";
		TypedQuery<Rating> typedQuery = em.createQuery(jpql, Rating.class);

		typedQuery.setParameter("name", name);
		typedQuery.setParameter("source", source);
		// typedQuery.setParameter("cenas", cenas);
		// typedQuery.executeUpdate();
		Rating rating = typedQuery.getSingleResult();
		rating.setValue(value);
		em.merge(rating);

	}

	public void testing_remover(String name, String source, BigDecimal cenas) {
		Serie serie = allDataOfSerie(name);

		Set<Rating> set_rating = serie.getRating();
		System.out.println(set_rating.size());
		for (Rating rating : set_rating) {
			if (rating.getSource().equals(source)) {
				set_rating.remove(rating);
				em.find(Rating.class, rating.getId());
				em.remove(rating);
			}
		}
		System.out.println(set_rating.size());
		serie.setRating(set_rating);
		em.merge(serie);
	}

	public void testing_adicionar(String name, String source, BigDecimal value) {
		Serie serie = allDataOfSerie(name);
		Set<Rating> set_rating = serie.getRating();

		Rating new_rating = new Rating(value, source);
		set_rating.add(new_rating);
		serie.setRating(set_rating);
		em.persist(new_rating);
		em.merge(serie);
	}
}