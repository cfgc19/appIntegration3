import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.collection.internal.PersistentSet;

import classesJPA.Cast;
import classesJPA.Characters;
import classesJPA.Episode;
import classesJPA.Languages;
import classesJPA.Profiles;
import classesJPA.ProgramCreators;
import classesJPA.Rating;
import classesJPA.Serie;
import classesJPA.Writers;

public class Director implements MessageListener {

	private ConnectionFactory connectionFactory;
	private static Destination NotifyAll;
	private static Destination MenuList;
	private static ArrayList<TextMessage> acceptedInvitations = new ArrayList<TextMessage>();
	private static Destination NotifyDiretor;

	public Director() throws NamingException {

		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.NotifyAll = InitialContext.doLookup("jms/topic/NotifyAll");
		this.MenuList = InitialContext.doLookup("jms/queue/MenuList");
		this.NotifyDiretor = InitialContext.doLookup("jms/topic/NotifyDiretor");

	}

	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage textMsg = (TextMessage) msg;
				System.out.println(textMsg.getText());
				analyzeTextMessage(textMsg);
			} else {
				ObjectMessage textMsg = (ObjectMessage) msg;
				System.out.println("\n(NOTIFICAÇÃO:" + textMsg.getObject() + " )");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void analyzeTextMessage(TextMessage msg) throws JMSException {
		if (msg.getJMSType().equals("Invitations")) {
			acceptedInvitations.add(msg);
			System.out.println("\n(NOTIFICAÇÃO: " + "Tem " + acceptedInvitations.size() + " convites pendentes" + " )");
		}
	}

	public String menuAtualizar(Scanner scan) {
		System.out.println("Qual a ação que pretende efetuar: ");
		System.out.println("1 -> Eliminar");
		System.out.println("2 -> Adicionar");
		System.out.println("3 -> Atualizar já existente");
		String acaoAtr = scan.nextLine();
		String msg = "";
		int numberAcaoAtr = 0;
		try {
			numberAcaoAtr = Integer.parseInt(acaoAtr);
			if (numberAcaoAtr == 1) {
				msg = "eliminar";
			} else if (numberAcaoAtr == 2) {
				msg = "adicionar";
			} else if (numberAcaoAtr == 3) {
				msg = "atualizar";
			} else {
				System.out.println("Opção não se encontra no menu!");
			}
		} catch (NumberFormatException e) {
			System.out.println("Insira uma opção válida!");
		}
		return msg;
	}

	public String emptyList(Scanner scan) {
		String newInfo = "";
		System.out.println("Não existe nenhuma informação acerca deste atributo, pertende adicionar?");
		System.out.println(" 1 -> Sim");
		System.out.println(" 2 -> Não");
		String numOp = scan.nextLine();
		int intOp = 0;
		try {
			intOp = Integer.parseInt(numOp);
			if (intOp == 1) {
				System.out.println("Insira a informação a adicionar: ");
				newInfo = scan.nextLine();
			}

		} catch (NumberFormatException e) {
			System.out.println("Insira uma opção válida!");
		}
		return newInfo;

	}

	public Serie addSerieToDB(Scanner scan) {
		System.out.print("Insira o nome da série: ");
		String serieName = scan.nextLine();
		System.out.println("");

		System.out.print("Insira o tipo: ");
		String serieType = scan.nextLine();
		System.out.println("");

		System.out.print("Insira a descrição: ");
		String serieDescription = scan.nextLine();
		System.out.println("");

		System.out.print("Insira o nome do narrador (vazia se inexistente): ");
		String serieNarrator = scan.nextLine();
		System.out.println("");

		System.out.print("Insira o tema musical (vazia se inexistente): ");
		String serieMusicalTheme = scan.nextLine();
		System.out.println("");

		System.out.print("Introduza a Nomeação (vazia se inexistente): ");
		String nomination = scan.nextLine();
		System.out.println("");

		System.out.print("Introduza a spin-Off (vazia se inexistente): ");
		String spinOff = scan.nextLine();
		System.out.println("");

		System.out.print("Introduza o award (vazia se inexistente): ");
		String award = scan.nextLine();
		System.out.println("");

		System.out.print("Network : ");
		String net = scan.nextLine();
		System.out.println("");

		System.out.print("Insira a data do primeiro episódio: ");
		String serieFirstEp = scan.nextLine();
		System.out.println("");

		System.out.println("Episódios :");
		Boolean done = false;
		Set<Episode> epsList = new HashSet<Episode>();
		while (!done) {
			System.out.print("Introduza o título (vazia se pretender terminar): ");
			String title = scan.nextLine();
			System.out.print("Introduza a season (vazia se pretender terminar): ");
			String season = scan.nextLine();
			System.out.println("Introduza o número (vazia se pretender terminar): ");
			String number = scan.nextLine();
			System.out.print("Introduza a data (vazia se pretender terminar): ");
			String date = scan.nextLine();
			done = (title.equals("") && season.equals("") && number.equals("") && date.equals(""));
			if (!done) {
				int seasonInt = Integer.parseInt(season);
				int numberInt = Integer.parseInt(number);
				Episode episode = new Episode(title, BigInteger.valueOf(seasonInt), BigInteger.valueOf(numberInt),
						date);
				epsList.add(episode);
				System.out.println("");
			}
		}
		System.out.println("");

		System.out.println("Cast :");
		done = false;
		Set<Characters> charList = new HashSet<Characters>();
		while (!done) {
			System.out.print("Introduza o nome da personagem (vazia se pretender terminar): ");
			String charac = scan.nextLine();
			System.out.print("Introduza o nome do actor (vazia se pretender terminar): ");
			String actor = scan.nextLine();
			Cast cast = new Cast(actor, "", "", null, "");

			done = (charac.equals("") && actor.equals(""));

			if (!done) {
				Characters character = new Characters(charac);
				character.setCast(cast);
				charList.add(character);
				System.out.println("");
			}
		}
		System.out.println("");

		System.out.println("Rating :");
		done = false;
		Set<Rating> ratingList = new HashSet<Rating>();
		while (!done) {
			System.out.print("Introduza a source (vazia se pretender terminar): ");
			String source = scan.nextLine();
			System.out.print("Introduza o valor (vazia se pretender terminar): ");
			String value = scan.nextLine();
			done = (value.equals("") && source.equals(""));
			if (!done) {
				Double valueDouble = Double.parseDouble(value);
				Rating rating = new Rating(BigDecimal.valueOf(valueDouble), source);
				ratingList.add(rating);
			}
		}
		System.out.println("");

		System.out.println("Writers :");
		done = false;
		Set<Writers> writersList = new HashSet<Writers>();
		while (!done) {
			System.out.print("Introduza nome (vazia se pretender terminar): ");
			String writerName = scan.nextLine();

			done = writerName.equals("");
			if (!done) {
				Writers writer = new Writers(writerName);
				writersList.add(writer);
			}
		}
		System.out.println("");

		System.out.println("Program Creator :");
		done = false;
		Set<ProgramCreators> pcList = new HashSet<ProgramCreators>();
		while (!done) {
			System.out.print("Introduza nome (vazia se pretender terminar): ");
			String pcName = scan.nextLine();

			done = pcName.equals("");
			if (!done) {
				ProgramCreators pc = new ProgramCreators(pcName);
				pcList.add(pc);
			}
		}
		System.out.println("");

		System.out.println("Profiles :");
		done = false;
		Set<Profiles> profilesList = new HashSet<Profiles>();
		while (!done) {
			System.out.print("Introduza nome da rede social(vazia se pretender terminar): ");
			String profilesName = scan.nextLine();

			done = profilesName.equals("");
			if (!done) {

				Profiles prof = new Profiles(profilesName);
				profilesList.add(prof);
			}
		}
		System.out.println("");

		System.out.println("Languages :");
		done = false;
		Set<Languages> langList = new HashSet<Languages>();
		while (!done) {
			System.out.print("Introduza a linguagem (vazia se pretender terminar): ");
			String langName = scan.nextLine();

			done = langName.equals("");
			if (!done) {

				Languages lang = new Languages(langName);
				langList.add(lang);
			}
		}
		System.out.println("+++++++++++ Aguarde +++++++++++");
		Serie serie = new Serie(serieName, serieType, serieDescription, serieNarrator, serieMusicalTheme, nomination,
				spinOff, award, net, serieFirstEp);
		serie.setLanguages(langList);
		serie.setProfiles(profilesList);
		serie.setProgramCreators(pcList);
		serie.setWriters(writersList);
		serie.setRating(ratingList);
		serie.setEpisode(epsList);
		serie.setCharacter(charList);
		return serie;

	}

	private void MenuDir() throws JMSException, IOException {

		Scanner scan = new Scanner(System.in);

		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			// try (JMSContext context =
			// connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			String dirName = "director";

			context.setClientID(dirName);
			// JMSConsumer consumer = context.createConsumer(MenuList);
			// consumer.setMessageListener(this);
			JMSConsumer consumer = context.createConsumer(MenuList);
			consumer.setMessageListener(this);
			JMSConsumer consumidor2 = context.createDurableConsumer((Topic) NotifyDiretor, dirName);
			consumidor2.setMessageListener(this);
			// JMSConsumer consumer = context.createConsumer(SignInLogin,
			// "topico");
			// consumer.setMessageListener(this);
			while (true) {
				List<String> atributos = new ArrayList<>();
				atributos.add("Nome da série");
				atributos.add("Tipo");
				atributos.add("Descrição");
				atributos.add("Narrador");
				atributos.add("Rating");
				atributos.add("Musica");
				atributos.add("Escritores");
				atributos.add("Nomeações");
				atributos.add("Spin-Off");
				atributos.add("Prémios");
				atributos.add("Network");
				atributos.add("Primeiro Episódio");
				atributos.add("Idiomas");
				atributos.add("Redes sociais");

				boolean sendRequest = true;
				boolean optionWhile = true;
				int intOption = 0;

				System.out.println("");
				System.out.println("");
				System.out.println("MENU DO DIRETOR");
				System.out.println("1- Adicionar uma nova série");
				System.out.println("2- Atualizar série já existente");
				System.out.println("3- Remover uma série");
				System.out.println("4- Listar todos os atores");
				System.out.println("5- Convidar todos os atores a participarem numa determinada série");
				System.out.println("6- Consultar atores que aceitaram participar numa dada série");
				System.out.println("7- Selecionar ator para uma determinada série");
				System.out.println("8- Convidar um ator a participar numa nova série");
				System.out.println("9- Sair!");
				System.out.println("");
				System.out.print("Escolhe a opção: ");

				while (optionWhile) {
					try {
						String option = scan.nextLine();

						intOption = Integer.parseInt(option);

						optionWhile = false;
					} catch (NumberFormatException e) {
						System.out.println("Insira uma opção válida!");
					}
				}

				if (intOption == 1) {
					// ADICIONAR UMA NOVA SÉRIE
					Serie serie = addSerieToDB(scan);
					String msgReceived = newSerie(serie);
					System.out.println(msgReceived);

				} else if (intOption == 2) {
					// ATUALIZAR SERIE JA EXISTENTE

					String serieName = "";
					String atributo = "";

					String msgReceived = "";

					List<String> list = listAllSeries();

					System.out.println("***********SERIES************");
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
					System.out.print("Selecione a serie que pretende atualizar: ");

					optionWhile = true;
					while (optionWhile) {
						try {
							String serieNameNumber = scan.nextLine();
							int number = Integer.parseInt(serieNameNumber);
							if (number < list.size() && number >= 0) {
								serieName = list.get(number);
								optionWhile = false;
							} else

							{
								System.out.println("Opção inválida! Bye Bye");
								System.exit(0);
							}

						} catch (NumberFormatException e) {
							System.out.println("Insira uma opção válida!");
						}
					}
					for (int i = 0; i < atributos.size(); i++) {
						System.out.println(i + "-> " + atributos.get(i));
					}

					System.out.print("Selecione o atributo que pretende alterar: ");
					String NumberAtr = "";
					int numberAtr = 0;
					optionWhile = true;
					while (optionWhile) {
						try {
							NumberAtr = scan.nextLine();
							numberAtr = Integer.parseInt(NumberAtr);
							if (numberAtr < atributos.size() && numberAtr >= 0) {
								atributo = atributos.get(numberAtr);
								optionWhile = false;
							} else {
								System.out.println("Opção inválida!");
								break;
							}

						} catch (NumberFormatException e) {
							System.out.println("Insira uma opção válida!");
						}
					}
					HashMap<String, String> hmapMSG = new HashMap<String, String>();

					hmapMSG.put("SerieName", serieName);
					hmapMSG.put("Update", atributo);

					if (numberAtr == 0) {
						System.out.println("*********** Nome da série ************");
						System.out.print("Introduza a nova informação: ");
						String NewName = scan.nextLine();
						hmapMSG.put("NewName", NewName);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 1) {

						System.out.println("*********** Tipo ************");
						System.out.print("Introduza a nova informação: ");
						String NewType = scan.nextLine();

						hmapMSG.put("NewType", NewType);
						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);

					} else if (numberAtr == 2) {
						System.out.println("*********** Descrição ************");
						System.out.print("Introduza a nova informação: ");
						String NewDesc = scan.nextLine();
						hmapMSG.put("NewDescription", NewDesc);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);

					} else if (numberAtr == 3) {
						System.out.println("*********** Narrador ************");
						System.out.print("Introduza a nova informação: ");
						String NewNarr = scan.nextLine();
						hmapMSG.put("NewNarrator", NewNarr);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);

					} else if (numberAtr == 4) {
						System.out.println("*********** Rating ************");
						String source = "";
						String newValue = "";
						String oldValue = "";
						List<Rating> setMSG = new ArrayList<Rating>();
						setMSG = listSetsSerieRating(serieName);
						if (setMSG.isEmpty()) {
							source = emptyList(scan);

							System.out.println("Insira o valor do Rating: ");
							newValue = scan.nextLine();

							hmapMSG.put("Source", source);
							hmapMSG.put("Value", newValue);
							hmapMSG.put("Acao", "adicionar");
							
							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						} else {

							String msgAt = menuAtualizar(scan);
							hmapMSG.put("Acao", msgAt);

							if (msgAt.equals("atualizar") || msgAt.equals("eliminar")) {

								for (int i = 0; i < setMSG.size(); i++) {
									System.out.println(i + "-> " + setMSG.get(i).getSource());
								}
								System.out.println("Selecione a source que pretende " + msgAt + ": ");
								String numAt = scan.nextLine();
								int intAtr = 0;
								try {
									intAtr = Integer.parseInt(numAt);
									source = setMSG.get(intAtr).getSource();

								} catch (NumberFormatException e) {
									System.out.println("Insira uma opção válida!");
								}
								if (msgAt.equals("atualizar")) {

									System.out.println("Introduza o novo valor de rating: ");
									newValue = scan.nextLine();
									hmapMSG.put("Source", source);
									hmapMSG.put("Value", newValue);

								} else {
									hmapMSG.put("Source", newValue);
								}

							} else {
								System.out.println("Insira a source do novo Rating: ");
								source = scan.nextLine();

								System.out.println("Insira o valor do Rating: ");
								newValue = scan.nextLine();
								hmapMSG.put("Source", source);
								hmapMSG.put("Value", newValue);


							}
							
						}
						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);


					} else if (numberAtr == 5) {

						System.out.println("*********** Música de abertura ************");
						System.out.print("Introduza a nova informação: ");
						String NewMusic = scan.nextLine();
						hmapMSG.put("NewMusic", NewMusic);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 6) {

						System.out.println("*********** Escritores ************");
						List<Writers> setMSG = new ArrayList<Writers>();
						setMSG = listSetsSerieWriters(serieName);
						String writer = "";
						if (setMSG.isEmpty()) {
							writer = emptyList(scan);
							hmapMSG.put("Acao", "adicionar");
							hmapMSG.put("Writer", writer);
							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						} else {
							String msgAt = menuAtualizar(scan);
							hmapMSG.put("Acao", msgAt);


							if (msgAt.equals("atualizar") || msgAt.equals("eliminar")) {
								String oldWriter = "";
								String newWriter = "";

								for (int i = 0; i < setMSG.size(); i++) {
									System.out.println(i + "-> " + setMSG.get(i).getWriter());
								}

								System.out.println("Selecione o escritor que pretende " + msgAt + ": ");
								String numAt = scan.nextLine();
								int intAtr = 0;
								try {
									intAtr = Integer.parseInt(numAt);
									oldWriter = setMSG.get(intAtr).getWriter();
								} catch (NumberFormatException e) {
									System.out.println("Insira uma opção válida!");
								}
								if (msgAt.equals("atualizar")) {
									System.out.println("Introduza o novo nome para o escritor: ");
									newWriter = scan.nextLine();
									hmapMSG.put("oldWriter", oldWriter);
									hmapMSG.put("newWriter", newWriter);
								} else {
									hmapMSG.put("Writer", oldWriter);
								}

							} else {
								System.out.println("Insira o nome do novo escritor: ");
								String newWriter = scan.nextLine();
								hmapMSG.put("Writer", newWriter);

							}

							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						}
					} else if (numberAtr == 7) {

						System.out.println("*********** Nomeações ************");
						System.out.print("Introduza a nova informação: ");
						String NewNomin = scan.nextLine();
						hmapMSG.put("NewNomination", NewNomin);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 8) {

						System.out.println("*********** Spin-Off ************");
						System.out.print("Introduza a nova informação: ");
						String NewSpin = scan.nextLine();
						hmapMSG.put("NewSpinOff", NewSpin);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 9) {

						System.out.println("*********** Prémios ************");
						System.out.print("Introduza a nova informação: ");
						String NewAward = scan.nextLine();
						hmapMSG.put("NewAward", NewAward);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);

					} else if (numberAtr == 10) {
						System.out.println("*********** Network ************");
						System.out.print("Introduza a nova informação: ");
						String NewNet = scan.nextLine();
						hmapMSG.put("NewNetwork", NewNet);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 11) {
						System.out.println("*********** Primeiro Episódio ************");
						System.out.print("Introduza a nova informação: ");
						String New1stEp = scan.nextLine();
						hmapMSG.put("New1stEpisode", New1stEp);

						msgReceived = UpdateSerie(hmapMSG);
						System.out.println(msgReceived);
					} else if (numberAtr == 12) {
						System.out.println("*********** Idiomas ************");
						List<Languages> setMSG = new ArrayList<Languages>();
						setMSG = listSetsSerieLangs(serieName);
						String Lang = "";
						if (setMSG.isEmpty()) {
							Lang = emptyList(scan);
							hmapMSG.put("Acao", "adicionar");
							hmapMSG.put("Lang", Lang);
							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						} else {


							String msgAt = menuAtualizar(scan);
							hmapMSG.put("Acao", msgAt);
							String language = "";

							if (msgAt.equals("atualizar") || msgAt.equals("eliminar")) {
								String oldLang = "";
								String newLang = "";

								for (int i = 0; i < setMSG.size(); i++) {
									System.out.println(i + "-> " + setMSG.get(i).getLanguage());
								}

								System.out.println("Selecione o idioma que pretende " + msgAt + ": ");
								String numAt = scan.nextLine();
								int intAtr = 0;
								try {
									intAtr = Integer.parseInt(numAt);
									oldLang = setMSG.get(intAtr).getLanguage();
								} catch (NumberFormatException e) {
									System.out.println("Insira uma opção válida!");
								}
								if (msgAt.equals("atualizar")) {
									System.out.println("Introduza o novo nome para o idioma: ");
									newLang = scan.nextLine();
									hmapMSG.put("oldLang", oldLang);
									hmapMSG.put("newLang", newLang);
								} else {
									hmapMSG.put("Lang", oldLang);
								}

							} else {
								System.out.println("Insira o nome do novo idioma: ");
								String newLang = scan.nextLine();
								hmapMSG.put("Lang", newLang);

							}
							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						}
					} else if (numberAtr == 13) {

						System.out.println("*********** Redes Sociais ************");
						List<Profiles> setMSG = new ArrayList<Profiles>();
						setMSG = listSetsSerieProfs(serieName);
						String Profile = "" ;
						if (setMSG.isEmpty()) {
							Profile = emptyList(scan);
							hmapMSG.put("Acao", "adicionar");
							hmapMSG.put("Prof", Profile);
							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						} else {


							String msgAt = menuAtualizar(scan);
							hmapMSG.put("Acao", msgAt);
							String profile = "";

							if (msgAt.equals("atualizar") || msgAt.equals("eliminar")) {
								String oldProf = "";
								String newProf = "";

								for (int i = 0; i < setMSG.size(); i++) {
									System.out.println(i + "-> " + setMSG.get(i).getProfile());
								}

								System.out.println("Selecione o perfil que pretende " + msgAt + ": ");
								String numAt = scan.nextLine();
								int intAtr = 0;
								try {
									intAtr = Integer.parseInt(numAt);
									oldProf = setMSG.get(intAtr).getProfile();
								} catch (NumberFormatException e) {
									System.out.println("Insira uma opção válida!");
								}
								if (msgAt.equals("atualizar")) {
									System.out.println("Introduza o novo nome para o perfil: ");
									newProf = scan.nextLine();
									hmapMSG.put("oldProf", oldProf);
									hmapMSG.put("newProf", newProf);
								} else {
									hmapMSG.put("Prof", oldProf);
								}

							} else {
								System.out.println("Insira o nome do novo perfil: ");
								String newProf = scan.nextLine();
								hmapMSG.put("Prof", newProf);

							}

							msgReceived = UpdateSerie(hmapMSG);
							System.out.println(msgReceived);
						}
					} else {
						System.out.println("Bye Bye");
						break;
					}

				} else if (intOption == 3) {
					// REMOVER UMA SÉRIE
					List<String> list = listAllSeries();
					System.out.println("***********SERIES************");
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
					System.out.println("Selecione a serie que pretende remover: ");
					String serieNumber = "";
					optionWhile = true;
					while (optionWhile) {
						try {
							serieNumber = scan.nextLine();
							int number = Integer.parseInt(serieNumber);
							if (number < list.size() && number >= 0) {
								String msgReceived = removeSerie(list.get(number));
								System.out.println(msgReceived);
								optionWhile = false;
							} else {
								System.out.println("Opção inválida! ");
								break;
							}

							// método remover
						} catch (NumberFormatException e) {
							System.out.println("Insira uma opção válida!");
						}
					}
				} else if (intOption == 4) {
					// LISTAR TODOS OS ATORES
					System.out.println("***********ATORES************");
					List<String> list = listActorsNames();
					for (int i = 0; i < list.size(); i++) {
						System.out.println(list.get(i));
					}
					if (list.isEmpty()) {
						System.out.println("Não há atores!");
					}

				} else if (intOption == 5) {
					// CONVIDAR TODOS OS ATORES A PARTICIPAREM NUMA DADA SERIE
					List<String> list = listAllSeries();
					System.out.println("***********SERIES************");
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
					System.out.print("Selecione a serie a qual pretende convidar os atores: ");
					String serieNameNumber = scan.nextLine();

					try {
						int number = Integer.parseInt(serieNameNumber);
						if (number < list.size() && number >= 0) {
							String serieName = list.get(number);
							System.out.print("Escreva a personagem que quer: ");
							String positionCast = scan.nextLine();
							inviteAllActors(serieName, positionCast);
						} else {
							System.out.println("Não escolheu um numero valido!");
						}

					} catch (NumberFormatException e) {
						System.out.println("Insira uma opção válida!");
					}

				} else if (intOption == 6) {
					// CONSULTAR ATORES QUE ACEITARAM PARTICIPAR NUMA DADA SÉRIE
					System.out.println("********Pedidos Aceites pelos Atores***********");
					int i = 0;
					for (TextMessage text : acceptedInvitations) {
						String[] strings = text.getText().split(";;");
						System.out.println(
								i + "-> Actor: " + strings[2] + "; Serie: " + strings[0] + " ; Posicao: " + strings[1]);
						i++;
					}
					if (acceptedInvitations.isEmpty()) {
						System.out.println("Não existem novos pedidos aceites pelos atores");
					}
				} else if (intOption == 7) {
					// SELECIONAR ATOR PARA PARTICIPAR NUMA DADA SÉRIE
					System.out.println("********Pedidos Aceites pelos Atores***********");
					int i = 0;
					for (TextMessage text : acceptedInvitations) {
						String[] strings = text.getText().split(";;");
						System.out.println(
								i + "-> Actor: " + strings[2] + "; Serie: " + strings[0] + " ; Posicao: " + strings[1]);
						i++;
					}
					if (acceptedInvitations.isEmpty()) {
						System.out.println("Não existem novos pedidos aceites pelos atores");
					} else {
						System.out.print("Escolha o pedido a processar: ");
						String orderNumber = scan.nextLine();
						try {
							int number = Integer.parseInt(orderNumber);
							if (number < acceptedInvitations.size() && number >= 0) {
								TextMessage msg = acceptedInvitations.get(number);
								System.out.print("Quer Aceitar o pedido? (S/N) ");
								String response = scan.nextLine().toLowerCase();
								if (response.equals("s")) {
									String[] strings = msg.getText().split(";;");
									newPositionInSerie(strings[0], strings[1], strings[2]);
									acceptedInvitations.remove(number);
								} else if (response.toLowerCase().equals("n")) {
									acceptedInvitations.remove(number);
								} else {
									System.out.println("Não escolheu uma opcao valida.");
								}
							} else {
								System.out.println("Não escolheu um numero valido!");
							}
						} catch (NumberFormatException e) {
							System.out.println("Insira uma opção válida!");
						}
					}
				} else if (intOption == 8) {
					// Convidar um ator a participar numa nova série
					Serie serie = addSerieToDB(scan);
					System.out.println("Escreva o nome da personagem que quer representar: ");
					String positionCast = scan.nextLine();
					System.out.println("***********ATORES************");
					List<String> list = listActorsNames();
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
					System.out.print("Escolha o ator que quer convidar: ");
					String castNumber = scan.nextLine();
					try {
						int number = Integer.parseInt(castNumber);
						if (number < list.size() && number >= 0) {
							inviteAnActor(serie.getSerieName(), positionCast, list.get(number));
						} else {
							System.out.println("Não escolheu um numero valido!");
						}
					} catch (NumberFormatException e) {
						System.out.println("Insira uma opção válida!");
					}

				} else if (intOption == 9) {
					System.out.println("Bye Bye");
					break;
				} else {
					System.out.println("Opção inválida! Bye Bye");
					break;
				}
			}
		}
	}

	public void controlSerie() {

	}

	public List<String> listAllSeries() {
		List<String> list = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();
			msg.setText("");
			msg.setJMSType("List_Serie");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuList, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			list = cons.receiveBody(List.class);
		} catch (Exception re) {
			re.printStackTrace();
		}
		return list;
	}

	public void newPositionInSerie(String serieName, String position, String castName) {
		List<String> list = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();
			msg.setText(castName + ";;" + position + ";;" + serieName);
			msg.setJMSType("new_Position");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			String text = cons.receiveBody(String.class);
			System.out.println(text);
		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public void inviteAnActor(String seriename, String positionCast, String nameAtor) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			// try (JMSContext context =
			// connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText(seriename + ";;" + positionCast + ";;" + nameAtor);
			msg.setJMSType("Invite_An_Actor");

			msg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, msg);

			JMSConsumer cons = context.createConsumer(tmp);
			String cenas = cons.receiveBody(String.class);

			System.out.println(cenas);

		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public void inviteAllActors(String seriename, String positionCast) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			// try (JMSContext context =
			// connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText(seriename + ";;" + positionCast);
			msg.setJMSType("Invite_All_Actors");

			msg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, msg);

			JMSConsumer cons = context.createConsumer(tmp);
			String cenas = cons.receiveBody(String.class);

			System.out.println(cenas);

		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public static void main(String[] args) throws NamingException, JMSException, IOException {
		Director director = new Director();
		director.MenuDir();
	}

	public List<String> listActorsNames() {
		List<String> listActors = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText("");
			msg.setJMSType("List_Actors");

			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuList, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			listActors = cons.receiveBody(List.class);

			// System.out.println("I received the reply sent to the temporary
			// queue: " + listActors);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (listActors);

	}

	public String newSerie(Serie serie) {
		String msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			ObjectMessage ObMsg = context.createObjectMessage();

			ObMsg.setObject(serie);
			ObMsg.setJMSType("New_Serie");

			ObMsg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, ObMsg);

			JMSConsumer cons = context.createConsumer(tmp);

			msgReceived = cons.receiveBody(String.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);

	}

	public String UpdateSerie(HashMap<String, String> hmapMSG) {
		String msgReceived = null;

		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();

			ObjectMessage msg = context.createObjectMessage();

			// envio a série
			msg.setObject(hmapMSG);
			msg.setJMSType("Update_Serie");

			msg.setJMSReplyTo(tmp);

			messageProducer.send(NotifyAll, msg);

			JMSConsumer cons = context.createConsumer(tmp);
			msgReceived = cons.receiveBody(String.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return msgReceived;
	}

	public String removeSerie(String serieName) {
		String msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage TextMsg = context.createTextMessage();

			TextMsg.setText(serieName);
			TextMsg.setJMSType("Remove_Serie");

			TextMsg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, TextMsg);

			JMSConsumer cons = context.createConsumer(tmp);
			msgReceived = cons.receiveBody(String.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);

	}

	public List<Rating> listSetsSerieRating(String serieName) {
		List<Rating> msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {

		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage TextMsg = context.createTextMessage();

			TextMsg.setText(serieName);
			TextMsg.setJMSType("ListRating_Serie");
			TextMsg.setJMSReplyTo(tmp);
			messageProducer.send(MenuList, TextMsg);

			JMSConsumer cons = context.createConsumer(tmp);
			msgReceived = cons.receiveBody(List.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);
	}

	public List<Writers> listSetsSerieWriters(String serieName) {
		List<Writers> msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage TextMsg = context.createTextMessage();

			TextMsg.setText(serieName);
			TextMsg.setJMSType("ListWriters_Serie");
			TextMsg.setJMSReplyTo(tmp);

			messageProducer.send(MenuList, TextMsg);

			JMSConsumer cons = context.createConsumer(tmp);

			msgReceived = cons.receiveBody(List.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);
	}

	public List<Languages> listSetsSerieLangs(String serieName) {
		List<Languages> msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {

		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage TextMsg = context.createTextMessage();

			TextMsg.setText(serieName);

			TextMsg.setJMSType("ListLang_Serie");

			TextMsg.setJMSReplyTo(tmp);
			messageProducer.send(MenuList, TextMsg);

			JMSConsumer cons = context.createConsumer(tmp);
			msgReceived = cons.receiveBody(List.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);
	}

	public List<Profiles> listSetsSerieProfs(String serieName) {
		List<Profiles> msgReceived = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {

		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage TextMsg = context.createTextMessage();

			TextMsg.setText(serieName);

			TextMsg.setJMSType("ListProf_Serie");

			TextMsg.setJMSReplyTo(tmp);
			messageProducer.send(MenuList, TextMsg);

			JMSConsumer cons = context.createConsumer(tmp);
			msgReceived = cons.receiveBody(List.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return (msgReceived);
	}

}
