package ejbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import classesJPA.Cast;
import classesJPA.Characters;
import classesJPA.Episode;
import classesJPA.Languages;
import classesJPA.Profiles;
import classesJPA.ProgramCreators;
import classesJPA.Rating;
import classesJPA.Serie;
import classesJPA.Writers;

/**
 * Message-Driven Bean implementation class for: MDB1
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destination", propertyValue = "MenuList"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, mappedName = "MenuList")
public class MDBDirector implements MessageListener {
	@Inject
	private JMSContext context;

	@EJB
	private BeanInterface beanInterface;

	/**
	 * Default constructor.
	 */
	public MDBDirector() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			if (message instanceof TextMessage) {
				String textMessage = ((TextMessage) message).getText();
				String typeMessage = ((TextMessage) message).getJMSType();


				System.out.println("Mensagem recebida: "+textMessage);
				
				if (typeMessage.equals("List_Actors"))
				{

					JMSProducer messageProducer = context.createProducer();

					ObjectMessage ObMsgReply = context.createObjectMessage();

					Destination tmp = message.getJMSReplyTo();
					ObMsgReply.setJMSReplyTo(tmp);
					List<String> listAllActors = beanInterface.listAllActors();
					
					

					ObMsgReply.setObject((Serializable) listAllActors);
					messageProducer.send(tmp, ObMsgReply);

				} else if (typeMessage.equals("List_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					
					List<String> listSeries = beanInterface.listSerienames();
					msg.setObject((Serializable) listSeries);
					messageProducer.send(tmp, msg);
					
				}  else if (typeMessage.equals("ListRating_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					System.out.println(beanInterface.allDataOfSerie(textMessage).getRating());

					Set<Rating> setRating = beanInterface.allDataOfSerie(textMessage).getRating();
					List<Rating> listRating =new ArrayList <Rating>();
					
					listRating.addAll(setRating);
					
//					System.out.println(listRating);
					
					msg.setObject((Serializable) listRating);

					messageProducer.send(tmp, msg);
				} else if (typeMessage.equals("ListWriters_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();

					msg.setJMSReplyTo(tmp);

					
					Set<Writers> setWriters = beanInterface.allDataOfSerie(textMessage).getWriters();
					List<Writers> listWriters =new ArrayList <Writers>();
					listWriters.addAll(setWriters);
				//	System.out.println(listWriters);
					
					msg.setObject((Serializable) listWriters);
					

					messageProducer.send(tmp, msg);
					
				}  else if (typeMessage.equals("ListLang_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();

					msg.setJMSReplyTo(tmp);
					
					
					Set<Languages> setLang = beanInterface.allDataOfSerie(textMessage).getLanguages();
					List<Languages> listLang =new ArrayList <Languages>();
					listLang.addAll(setLang);
				//	System.out.println(listLang);
					

					msg.setObject((Serializable) listLang);
					messageProducer.send(tmp, msg);

				} else if (typeMessage.equals("ListProf_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();

					msg.setJMSReplyTo(tmp);

					
					Set<Profiles> setProf = beanInterface.allDataOfSerie(textMessage).getProfiles();
					List<Profiles> listProf =new ArrayList <Profiles>();
					
					listProf.addAll(setProf);
					//System.out.println(listProf);
					
					msg.setObject((Serializable) listProf);
					messageProducer.send(tmp, msg);
				}

			} else {

				Serie ObjMessage = (Serie) ((ObjectMessage) message).getObject();
				String typeMessage = ((TextMessage) message).getJMSType();

				if (typeMessage.equals("New_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage ObMsgReply = context.createObjectMessage();

					Destination tmp = message.getJMSReplyTo();
					ObMsgReply.setJMSReplyTo(tmp);
					// criar método para adicionar uma série
					beanInterface.newSerie(ObjMessage);

					ObMsgReply.setObject((Serializable) ObjMessage);
					messageProducer.send(tmp, ObjMessage);
					
				}

			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
