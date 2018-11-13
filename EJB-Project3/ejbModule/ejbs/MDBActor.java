package ejbs;

import java.io.Serializable;
import java.util.List;

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
import classesJPA.Serie;

/**
 * Message-Driven Bean implementation class for: MDB2
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "MenuAtor"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, mappedName = "MenuAtor")
public class MDBActor implements MessageListener {

	@Inject
	private JMSContext context;

	@EJB
	private BeanInterface beanInterface;

	/**
	 * Default constructor.
	 */
	public MDBActor() {
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

				System.out.println("Metodo onMessage do MDBActor recebeu a seguinte mensagem: " + textMessage);

				if (typeMessage.equals("List_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					List<String> listSeries = beanInterface.listSerienames();
					msg.setObject((Serializable) listSeries);
					messageProducer.send(tmp, msg);
				} else if (typeMessage.equals("List_Actors")) {

					JMSProducer messageProducer = context.createProducer();

					ObjectMessage ObMsgReply = context.createObjectMessage();

					Destination tmp = message.getJMSReplyTo();
					ObMsgReply.setJMSReplyTo(tmp);
					List<String> listAllActors = beanInterface.listAllActors();

					ObMsgReply.setObject((Serializable) listAllActors);
					messageProducer.send(tmp, ObMsgReply);

				} else if (typeMessage.equals("Obtain_Ator")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					Cast cast = beanInterface.actor(textMessage);
					msg.setObject((Serializable) cast);
					messageProducer.send(tmp, msg);
				} else if (typeMessage.equals("Info_Serie")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					Serie serie = beanInterface.allDataOfSerie(textMessage);
					msg.setObject((Serializable) serie);
					messageProducer.send(tmp, msg);
				} else if (typeMessage.equals("Check_Email")) {
					JMSProducer messageProducer = context.createProducer();

					ObjectMessage msg = context.createObjectMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);
					Boolean emailValidation = beanInterface.emailAtorvalidation(textMessage);
					msg.setObject(emailValidation);
					messageProducer.send(tmp, msg);
				}


			} else {
				String typeMessage = ((ObjectMessage) message).getJMSType();

				if (typeMessage.equals("Registo_Ator")) {
					Cast cast = (Cast) ((ObjectMessage) message).getObject();
					JMSProducer messageProducer = context.createProducer();
					TextMessage msg = context.createTextMessage();
					Destination tmp = message.getJMSReplyTo();
					msg.setJMSReplyTo(tmp);

					Boolean validation = beanInterface.acceptRegist(cast.getEmail());
					if (validation == true) {
						beanInterface.doRegist(cast);
						beanInterface.sendEmail(cast.getEmail(), "Access Token",  cast.getAccess_token());
						msg.setText("Registo efetuado com sucesso! Email enviado com o access token!");
					} else {
						msg.setText("Impossivel fazer o registo. Email ja existe");
					}
					messageProducer.send(tmp, msg);
				}
				 else if (typeMessage.equals("Number_Login")) {
					Cast cast = (Cast) ((ObjectMessage) message).getObject();
						beanInterface.changeNumberLogin(cast);
					}
			}
		} catch (

		JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
