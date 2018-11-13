import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber;
import com.sun.javafx.collections.MappingChange.Map;

import classesJPA.Cast;
import classesJPA.Episode;
import classesJPA.ProgramCreators;
import classesJPA.Serie;

public class Actor implements MessageListener {

	private ConnectionFactory connectionFactory;
	private static Destination NotifyAll;
	private static Destination MenuAtor;
	private static String nameCast;
	private static Destination NotifyDiretor;
	static Scanner scan = new Scanner(System.in);
	private static ArrayList<TextMessage> pendentsInvitations = new ArrayList<TextMessage>();
	private static ArrayList<TextMessage> pendentsMessages = new ArrayList<TextMessage>();
	private static HashMap<String, ArrayList<TextMessage>> pendentsMap = new HashMap<String, ArrayList<TextMessage>>();
	private static HashMap<String, ArrayList<TextMessage>> pendentsInviteMap = new HashMap<String, ArrayList<TextMessage>>();

	public Actor() throws NamingException {

		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.NotifyAll = InitialContext.doLookup("jms/topic/NotifyAll");
		this.MenuAtor = InitialContext.doLookup("jms/queue/MenuAtor");
		this.NotifyDiretor = InitialContext.doLookup("jms/topic/NotifyDiretor");
	}

	public static void main(String[] args) throws NamingException, JMSException, IOException {

		Actor actor = new Actor();
		actor.menu1(actor);
	}

	public void menu1(Actor actor) throws JMSException, IOException {

		while (true) {
			System.out.print("\nBEM-VINDO! \n1-> Registo\n2-> Login\n3-> Sair\nEscolha a opção: ");
			String option = scan.nextLine();
			String email = "";
			String name = "";
			String pass = "";
			// REGISTO
			if (option.equals("1")) {
				System.out.print("\nEmail: ");
				email = scan.nextLine();
				while (!email.contains("@")) {
					System.out.print("\nEmail: ");
					email = scan.nextLine();
				}
				System.out.print("Nome: ");
				name = scan.nextLine();
				System.out.print("Pass: ");
				pass = scan.nextLine();
				String access_token = getAccessToken();
				registo(new Cast(name, pass, email, Long.valueOf(0), access_token));
			} else if (option.equals("2")) {

				System.out.print("Email: ");
				email = scan.nextLine();
				while (!email.contains("@")) {
					System.out.print("\nEmail: ");
					email = scan.nextLine();
				}
				Boolean emailValidation = checkEmail(email);
				if (emailValidation == true) {
					System.out.print("Password: ");
					pass = scan.nextLine();

					Cast cast = obtainCast(email);
					if (cast.getEmail().equals(email) & cast.getPassword().equals(pass)) {
						if (cast.getLogin().equals(Long.valueOf(0))) {
							System.out.print("Access Token: ");
							String access_token = scan.nextLine();
							if (access_token.equals(cast.getAccess_token())) {
								changeNumberLogin(cast);
								System.out.println("Login efetuado com sucesso!");
								actor.menu2(actor, email);
								break;
							} else {
								System.out.println("Login Inválido!");
							}
						} else {
							System.out.println("Login efetuado com sucesso!");
							actor.menu2(actor, email);
							break;
						}
					}
					else{
						System.out.println("Login Inválido!");
					}
				}
			} else if (option.equals("3")) {
				System.out.println("Bye Bye");
				break;

			} else {
				System.out.println("\nA opçcao está incorreta. Tente novamente!");
			}
		}
	}

	public void menu2(Actor actor, String email) throws JMSException, IOException {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {

		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			Cast cast = obtainCast(email);
			nameCast = cast.getName();
			context.setClientID(nameCast);
			JMSConsumer consumidor2 = context.createDurableConsumer((Topic) NotifyAll, nameCast);
			consumidor2.setMessageListener(this);
			if (pendentsMap.containsKey(nameCast)) {
				pendentsMessages = pendentsMap.get(nameCast);
			} else {
				pendentsMap.put(nameCast, new ArrayList<TextMessage>());
				pendentsMessages = new ArrayList<TextMessage>();
			}
			if (pendentsInviteMap.containsKey(nameCast)) {
				pendentsInvitations = pendentsInviteMap.get(nameCast);
			} else {
				pendentsInviteMap.put(nameCast, new ArrayList<TextMessage>());
				pendentsInvitations = new ArrayList<TextMessage>();
			}
			while (true) {
				System.out.println("**********************MENU**************************");
				System.out.println("1-> Listar todas as Séries");
				System.out.println("2-> Informação sobre uma Série");
				System.out.println("3-> Listar convites pendentes");
				System.out.println("4-> Mandar mensagem/Responder a um actor");
				System.out.println("5-> Aceitar ou Rejeitar Convites");
				System.out.println("6-> Sair");

				System.out.print("Escolha a opção: ");

				String option = scan.nextLine();

				if (option.equals("1")) {

					System.out.println("***********SERIES************");
					List<String> list = listAllSeries();
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
				} else if (option.equals("2")) {
					List<String> list = listAllSeries();
					System.out.println("***********SERIES************");
					for (int i = 0; i < list.size(); i++) {
						System.out.println(i + "-> " + list.get(i));
					}
					System.out.print("Escolha o numero da Serie: ");
					String serieNameNumber = scan.nextLine();
					try {
						int number = Integer.parseInt(serieNameNumber);
						if (number < list.size() && number >= 0) {
							Serie serie = informationSerie(list.get(number));
						} else {
							System.out.println("Nao escolhei um numero valido!");
						}
					} catch (NumberFormatException e) {
						System.out.println("Isso nao e um numero burro.");
					}
				} else if (option.equals("3")) {
					// Listar convites pendentes
					System.out.println("Mensagens Pendentes para ti: ");
					int i = 0;
					for (TextMessage text : pendentsInvitations) {
						String[] strings = text.getText().split(";;");
						System.out.println(i + "-> Serie: " + strings[0] + " ; Posicao: " + strings[1]);
						i++;
					}
					if (pendentsInvitations.isEmpty()) {
						System.out.println("Não tem convites pendentes.");
					}
				} else if (option.equals("4")) {
					// Mandar mensagem a um actor

					System.out.print("Quer responder a uma das mensagens ou mandar uma mensagem a outro ator? (R/M) ");
					String option1 = scan.nextLine().toLowerCase();
					if (option1.equals("r")) {
						int i = 0;
						System.out.println("*******Mensagens por Responder*******");
						String[] strings = null;
						for (TextMessage text : pendentsMessages) {
							strings = text.getText().split(";;");
							System.out.println(i + "-> Nome do Ator: " + strings[2] + " ; Mensagem: " + strings[1]);
							i++;
						}
						if (pendentsMessages.isEmpty())
							System.out.println("Nao tem mensagens por ler");
						else {
							System.out.println("Escolha a que mensagem que quer processar: ");
							String numberOrder = scan.nextLine();
							try {
								int number = Integer.parseInt(numberOrder);
								if (number < pendentsMessages.size() && number >= 0) {
									System.out.print("Quer Responder ou Eliminar? (R/E)");
									String responseMessage = scan.nextLine().toLowerCase();
									if (responseMessage.equals("r")) {
										System.out.print("Escreva a mensagem que quer enviar: ");
										String chatMessage = scan.nextLine();
										chatCast(strings[2], chatMessage);
										pendentsMessages.remove(number);
										pendentsMap.get(nameCast).remove(number);
									} else if (responseMessage.equals("e")) {
										pendentsMessages.remove(number);
										pendentsMap.get(nameCast).remove(number);
									} else {
										System.out.println("Opção inválida!");
									}
								} else {
									System.out.println("Nao escolheu um numero valido!");
								}
							} catch (NumberFormatException e) {
								System.out.println("Isso nao e um numero burro.");
							}
						}

					} else if (option1.equals("m")) {
						// MANDAR UMA NOVA MENSAGEM
						System.out.println("***********ATORES************");
						List<String> list = listActorsNames();
						for (int i = 0; i < list.size(); i++) {
							System.out.println(i + "-> " + list.get(i));
						}
						System.out.print("Escolha o ator que quer mandar mensagem: ");
						String castNumber = scan.nextLine();
						try {
							int number = Integer.parseInt(castNumber);
							if (number < list.size() && number >= 0) {
								System.out.print("Escreva a mensagem que quer enviar: ");
								String chatMessage = scan.nextLine();
								chatCast(list.get(number), chatMessage);
							} else {
								System.out.println("Nao escolheu um numero valido!");
							}
						} catch (NumberFormatException e) {
							System.out.println("Isso nao e um numero burro.");
						}
					} else {
						System.out.println("Não escolhei uma opção válida.");
					}
				} else if (option.equals("5")) {
					// ACEITAR OU REJEITAR CONVITES
					System.out.println("Convites Pendentes: ");
					int i = 0;
					for (TextMessage text : pendentsInvitations) {
						String[] strings = text.getText().split(";;");
						System.out.println(i + "-> Serie: " + strings[0] + " ; Posicao: " + strings[1]);
						i++;
					}
					if (pendentsInvitations.isEmpty()) {
						System.out.println("Não tem convites pendentes.");
					} else {
						System.out.print("Escolha o pedido a processar: ");
						String orderNumber = scan.nextLine();
						try {
							int number = Integer.parseInt(orderNumber);
							if (number < pendentsInvitations.size() && number >= 0) {
								TextMessage msg = pendentsInvitations.get(number);
								System.out.print("Quer Aceitar o pedido? (S/N) ");
								String response = scan.nextLine().toLowerCase();

								if (response.equals("s")) {
									acceptedInvitation(msg);
									pendentsInvitations.remove(number);
									pendentsInviteMap.get(nameCast).remove(number);
								} else if (response.toLowerCase().equals("n")) {
									pendentsInvitations.remove(number);
									pendentsInviteMap.get(nameCast).remove(number);
								} else {
									System.out.println("Nao escolhei uma opcao valida.");
								}
							} else {
								System.out.println("Nao escolhei um numero valido!");
							}
						} catch (NumberFormatException e) {
							System.out.println("Isso nao e um numero burro.");
						}
					}
				} else if (option.equals("6")) {
					System.out.println("Bye Bye");
					consumidor2.close();
					context.close();
					actor.menu1(actor);
					break;
				} else {
					System.out.println("\nA opçcao está incorreta. Tente novamente!");
				}
			}
		} catch (JMSRuntimeException e) {
			e.printStackTrace();
		}

	}

	// METODOS PARA O REGISTO/LOGIN
	public String getAccessToken() {
		String characthers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder string = new StringBuilder();
		Random random = new Random();
		while (string.length() < 9) { // length of the random string.
			int index = (int) (random.nextFloat() * characthers.length());
			string.append(characthers.charAt(index));
		}
		String accessToken = string.toString();
		return accessToken;
	}

	public void registo(Cast cast) {
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			ObjectMessage msg = context.createObjectMessage();
			msg.setObject(cast);
			msg.setJMSType("Registo_Ator");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			String response = cons.receiveBody(String.class);
			System.out.println(response);
		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public boolean checkEmail(String email) {
		Boolean response = null;
		//try (JMSContext context = connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			 try (JMSContext context =
			 connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();
			msg.setText(email);
			msg.setJMSType("Check_Email");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			response = cons.receiveBody(Boolean.class);

			if (response == false) {
				System.out.println("Email Inválido!");
			}
		} catch (Exception re) {
			re.printStackTrace();
		}
		return response;
	}

	public void changeNumberLogin(Cast cast) {
		Boolean response = null;
		//try (JMSContext context = connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			try (JMSContext context =
			 connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			ObjectMessage msg = context.createObjectMessage();
			msg.setObject(cast);
			msg.setJMSType("Number_Login");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuAtor, msg);

		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public Cast obtainCast(String email) {
		Cast cast = new Cast();

		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();
			msg.setText(email);
			msg.setJMSType("Obtain_Ator");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			cast = cons.receiveBody(Cast.class);

		} catch (Exception re) {
			re.printStackTrace();
		}
		return cast;
	}

	// METODOS DO MENU DO ATOOOOOR
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
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			listActors = cons.receiveBody(List.class);
		} catch (Exception re) {
			re.printStackTrace();
		}
		return (listActors);
	}

	public Serie informationSerie(String serieName) {
		Serie serie = new Serie();
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText(serieName);
			msg.setJMSType("Info_Serie");
			msg.setJMSReplyTo(tmp);
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			serie = cons.receiveBody(Serie.class);

			System.out.println("*********SERIE**********");
			System.out.println("Nome: " + serie.getSerieName());
			System.out.println("Descrição: " + serie.getDescription());
			System.out.println("Tipo: " + serie.getType());
			System.out.println("Narrador: " + serie.getNarrator());
			System.out.println("Tema Musical: " + serie.getMusicalTheme());
			System.out.println("Nomeações: " + serie.getNominations());
			System.out.println("Network: " + serie.getNetwork());
		} catch (Exception re) {
			re.printStackTrace();
		}
		return serie;
	}

	public void chatCast(String actorname, String message) {
		List<String> listActors = null;
		// try (JMSContext context = connectionFactory.createContext("filipa",
		// "Cfilipacosta19!");) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText(actorname + ";;" + message + ";;" + nameCast);
			msg.setJMSType("Chat");

			msg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyAll, msg);

			// JMSConsumer cons = context.createConsumer(tmp);

			// listActors = cons.receiveBody(List.class);

			// System.out.println("I received the reply sent to the temporary
			// queue: " + listActors);

		} catch (Exception re) {
			re.printStackTrace();
		}
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
			messageProducer.send(MenuAtor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			list = cons.receiveBody(List.class);
		} catch (Exception re) {
			re.printStackTrace();
		}
		return list;
	}

	public void acceptedInvitation(TextMessage text) {
		try (JMSContext context = connectionFactory.createContext("adriana", "Adriana1");) {

			// try (JMSContext context =
			// connectionFactory.createContext("filipa", "Cfilipacosta19!");) {
			JMSProducer messageProducer = context.createProducer();

			Destination tmp = context.createTemporaryQueue();
			TextMessage msg = context.createTextMessage();

			msg.setText(text.getText() + ";;" + nameCast);
			msg.setJMSType("Invitations");

			msg.setJMSReplyTo(tmp);
			messageProducer.send(NotifyDiretor, msg);

			JMSConsumer cons = context.createConsumer(tmp);

			String string = cons.receiveBody(String.class);
			System.out.println(string);
		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public void analyzeTextMessage(TextMessage message) throws JMSException {

		if (message.getJMSType().equals("Remove_Serie")) {
			System.out.println("\n(NOTIFICAÇÃO: A série " + message.getText() + " foi removida da base de dados.)");
		} else {

			String[] strings = message.getText().split(";;");
			if (message.getJMSType().equals("Invite_All_Actors")
					|| (message.getJMSType().equals("Invite_An_Actor") & strings[2].equals(nameCast))) {
				pendentsInvitations.add(message);
				pendentsInviteMap.get(nameCast).add(message);
				System.out.println("\n(NOTIFICAÇÃO: Foste convidado para prencher a posicao " + strings[1]
						+ " na serie chamada " + strings[0] + " )");
			} else if (message.getJMSType().equals("Chat") & strings[0].equals(nameCast)) {
				pendentsMessages.add(message);
				pendentsMap.get(nameCast).add(message);
				System.out.println("\n(NOTIFICAÇÃO: Tens uma nova mensagem de " + strings[2] + " por ler" + " )");
			} else if (message.getJMSType().equals("new_Position") & strings[0].equals(nameCast)) {
				System.out.println("\n(NOTIFICAÇÃO: PARABÉNS! Foi aceite para representar o papel de " + strings[1]
						+ " na série " + strings[2] + " )");
			}
		}

	}

	public void analyzeObjMessage(ObjectMessage message) throws JMSException {
		if (message.getJMSType().equals("New_Serie")) {
			Serie ObjMessage = (Serie) ((ObjectMessage) message).getObject();
			System.out.println("\n(NOTIFICAÇÃO: Foi adicionada uma nova série à base de dados. Nome --> " + ObjMessage.getSerieName() + ")");
			
		}
		else if (message.getJMSType().equals("Update_Serie"))
		{
			HashMap ObjMessage = (HashMap) ((ObjectMessage) message).getObject();
			System.out.println("\n(NOTIFICAÇÃO: A série "+ObjMessage.get("SerieName")+" foi atualizada. Foram feitas alterações no atributo " + ObjMessage.get("Update") + ")");
			

		}
	}

	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage textMsg = (TextMessage) msg;
				analyzeTextMessage(textMsg);
			} else {
				ObjectMessage ObjMsg = (ObjectMessage) msg;
				analyzeObjMessage(ObjMsg);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
