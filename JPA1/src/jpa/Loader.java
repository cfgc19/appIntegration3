package jpa;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import classesJPA.Cast;
import classesJPA.Characters;
import classesJPA.Episode;
import classesJPA.Languages;
import classesJPA.Profiles;
import classesJPA.ProgramCreators;
import classesJPA.Rating;
import classesJPA.Serie;
import classesJPA.Writers;
import common.Project;
import common.unmarshall;

public class Loader {

	public static void main(String[] args) {
		
		Set<String> listEpisodes11 = new HashSet<String>();
		
		File xmlFile = new File("./src/common/series.xml");

		// Objeto projeto representa cada série
		Project project = unmarshall.unmarshalles_project(xmlFile);

		List<Episode> listEpisodes1 = new ArrayList<Episode>();
		List<Cast> listCast1 = new ArrayList<Cast>();
		List<Rating> listRating1 = new ArrayList<Rating>();
		List<Writers> listWriters1 = new ArrayList<Writers>();
		List<ProgramCreators> listCreators1 = new ArrayList<ProgramCreators>();
		List<Profiles> listProfiles1 = new ArrayList<Profiles>();
		List<Languages> listLanguages1 = new ArrayList<Languages>();
		List<Characters> listCharacters1 = new ArrayList<Characters>();
		
		Set<String> listEpisodesNames = new HashSet<String>();
		Set<String> listCastNames = new HashSet<String>();
		Set<String> listRatingNames = new HashSet<String>();
		Set<String> listWritersNames = new HashSet<String>();
		Set<String> listCreatorsNames = new HashSet<String>();
		Set<String> listProfilesNames = new HashSet<String>();
		Set<String> listLanguagesNames = new HashSet<String>();
		
		Set<Serie> seriesJPA = new HashSet<Serie>();
	
		List<common.Serie> series = project.getSerie();
	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (common.Serie st : series) {
			Serie serieJPA = new Serie();
			serieJPA = new Serie(st.getSerieName(), st.getType(), st.getDescription(), st.getNarrator(),
					st.getMusicalTheme(), st.getNominations(), st.getSpinOff(), st.getAwards(), st.getNetwork(),
					st.getFirstEpisode());

			Languages languageJPA = new Languages(null);
			Profiles profileJPA = new Profiles(null);
			Writers writerJPA = new Writers(null);
			ProgramCreators creatorsJPA = new ProgramCreators(null);

			Cast castJPA = new Cast(null,null,null,null,null);

			Characters charactersJPA = new Characters(null);
			Episode episodeJPA = new Episode(null, null, null, null);

			Set<Episode> listEpisodes = new HashSet<Episode>();
			Set<Cast> listCast = new HashSet<Cast>();
			Set<Rating> listRating = new HashSet<Rating>();
			Set<Writers> listWriters = new HashSet<Writers>();
			Set<ProgramCreators> listCreators = new HashSet<ProgramCreators>();
			Set<Profiles> listProfiles = new HashSet<Profiles>();
			Set<Languages> listLanguages = new HashSet<Languages>();
			Set<Characters> listCharacters = new HashSet<Characters>();

			for (int i = 0; i < st.getEpisode().size(); i++) {
				episodeJPA = new Episode(st.getEpisode().get(i).getTitle(), st.getEpisode().get(i).getNumber(),
						st.getEpisode().get(i).getNumber(), st.getEpisode().get(i).getDate());
				//episodeJPA.setSerie(serieJPA);
				listEpisodes.add(episodeJPA);
				listEpisodes1.add(episodeJPA);
			}
			for (int i = 0; i < st.getRating().size(); i++) {
				Rating ratingJPA = new Rating(st.getRating().get(i).getValue(), st.getRating().get(i).getSource());
				//ratingJPA.setSerie(serieJPA);
				listRating.add(ratingJPA);
				listRating1.add(ratingJPA);
			}
			for (int i = 0; i < st.getLanguages().size(); i++) {
				languageJPA = new Languages(st.getLanguages().get(i));
				

				if (!(listLanguagesNames.contains(st.getLanguages().get(i)))) {
					listLanguagesNames.add(st.getLanguages().get(i));
					listLanguages1.add(languageJPA);
				}
				else{
					languageJPA=listLanguages1.get(listLanguages1.indexOf(languageJPA));
					
				}
				listLanguages.add(languageJPA);
			}
			for (int i = 0; i < st.getProfiles().size(); i++) {
				profileJPA = new Profiles(st.getProfiles().get(i));

				if (!(listProfilesNames.contains(st.getProfiles().get(i)))) {
					listProfilesNames.add(st.getProfiles().get(i));
					listProfiles1.add(profileJPA);
				}
				else{
					profileJPA=listProfiles1.get(listProfiles1.indexOf(profileJPA));
				}
				listProfiles.add(profileJPA);
			}
			for (int i = 0; i < st.getWriters().size(); i++) {
				writerJPA = new Writers(st.getWriters().get(i));
				listWriters.add(writerJPA);

				if (!(listWritersNames.contains(st.getWriters().get(i)))) {
					listWritersNames.add(st.getWriters().get(i));
					listWriters1.add(writerJPA);
				}
			}
			for (int i = 0; i < st.getProgramCreators().size(); i++) {
				creatorsJPA = new ProgramCreators(st.getProgramCreators().get(i));
				listCreators.add(creatorsJPA);

				if (!(listCreatorsNames.contains(st.getProgramCreators().get(i)))) {
					listCreatorsNames.add(st.getProgramCreators().get(i));
					listCreators1.add(creatorsJPA);
				}
			}
			for (int i=0; i< st.getCast().size(); i++) {

				castJPA = new Cast(st.getCast().get(i).getName(),null,null,null,null);

				charactersJPA = new Characters(st.getCast().get(i).getCharacter());
				//charactersJPA.setSerie(serieJPA);
				if (!(listCastNames.contains(st.getCast().get(i).getName()))) {
					listCastNames.add(st.getCast().get(i).getName());
					charactersJPA.setCast(castJPA);
					listCast1.add(castJPA);
//					Set<Characters> characters_set = new HashSet<Characters>();
//					characters_set.add(charactersJPA);
//					castJPA.setCharacters(characters_set);
					
				}	
				else{
					castJPA=listCast1.get(listCast1.indexOf(castJPA));
//					Set<Characters> characters_set = new HashSet<Characters>();
//					characters_set.add(charactersJPA);
//					castJPA.setCharacters(characters_set);
					charactersJPA.setCast(castJPA);
				}
				listCast.add(castJPA);
				listCharacters1.add(charactersJPA);
				listCharacters.add(charactersJPA);
				
			}
			 serieJPA.setEpisode(listEpisodes);
			 serieJPA.setLanguages(listLanguages);
			 serieJPA.setProfiles(listProfiles);
			 serieJPA.setProgramCreators(listCreators);
			 serieJPA.setWriters(listWriters);
			 serieJPA.setRating(listRating);
			 serieJPA.setCharacter(listCharacters);
			 serieJPA.setRating(listRating);
			seriesJPA.add(serieJPA);
		}
		for(Serie st : seriesJPA)
			em.persist(st);
		for (Episode ep : listEpisodes1) {
			em.persist(ep);
		}
		for (Rating rt : listRating1) {
			em.persist(rt);
		}
		for (Languages lg : listLanguages1) {
			em.persist(lg);
		}
		for (Profiles pf : listProfiles1) {
			em.persist(pf);
		}
		for (ProgramCreators pc : listCreators1) {
			em.persist(pc);
		}
		for (Writers wr : listWriters1) {
			em.persist(wr);
		}
		for (Cast wr : listCast1) {
			em.persist(wr);
		}
		for (Characters wr : listCharacters1) {
			em.persist(wr);
		}
		tx.commit();
		
		
		for(Rating st: listRating1)
			System.out.println(st);
		for(Episode st: listEpisodes1)
			System.out.println(st);
		// after commit we have ids:
		// Close an application-managed entity manager.

		em.close();
		emf.close();
	}
}
