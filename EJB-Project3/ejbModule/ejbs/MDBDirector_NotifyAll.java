package ejbs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import classesJPA.Serie;

/**
 * Message-Driven Bean implementation class for: MDBDirector_NotifyAll
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "NotifyAll"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") }, mappedName = "NotifyAll")
public class MDBDirector_NotifyAll implements MessageListener {

	@Inject
	private JMSContext context;

	@EJB
	private BeanInterface beanInterface;

	/**
	 * Default constructor.
	 */
	public MDBDirector_NotifyAll() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {

			if(message instanceof TextMessage)
			{
				String textMessage = ((TextMessage) message).getText();
				String typeMessage = ((TextMessage) message).getJMSType();
				
				System.out.println("Mensagem recebida: "+textMessage);	
				
				if(typeMessage.equals("Remove_Serie")){
					//Aqui o textMessage é o nome da série
					JMSProducer messageProducer = context.createProducer();
					
					TextMessage TextMsgReply = context.createTextMessage();
					Destination tmp = message.getJMSReplyTo();

					Serie serieToRmv = beanInterface.allDataOfSerie(textMessage);
					beanInterface.removeSerie(serieToRmv);
					
					TextMsgReply.setJMSReplyTo(tmp);
					TextMsgReply.setJMSType("SerieRemoved");
					
					TextMsgReply.setText("Série "+ textMessage + " foi removida da base de dados.");
					messageProducer.send(tmp, TextMsgReply);
				}	
				else if (typeMessage.equals("New_Serie")) {
					JMSProducer messageProducer = context.createProducer();
					System.out.println(textMessage);
					TextMessage TextMsgReply = context.createTextMessage();

					Destination tmp = message.getJMSReplyTo();
					TextMsgReply.setJMSReplyTo(tmp);
					TextMsgReply.setText("Convites enviados aos Atores");
					messageProducer.send(tmp, TextMsgReply);
				}

				else if(typeMessage.equals("Invite_An_Actor") || typeMessage.equals("Invite_All_Actors")){
					JMSProducer messageProducer = context.createProducer();
					
					TextMessage TextMsgReply = context.createTextMessage();
					Destination tmp = message.getJMSReplyTo();
					TextMsgReply.setJMSReplyTo(tmp);
					TextMsgReply.setText("Convites enviados aos Atores");
					messageProducer.send(tmp, TextMsgReply);
				}
				else if (typeMessage.equals("new_Position")) {
					JMSProducer messageProducer = context.createProducer();

					TextMessage msg = context.createTextMessage();
					Destination tmp = message.getJMSReplyTo();

					msg.setJMSReplyTo(tmp);
					String[] strings = textMessage.split(";;");
					beanInterface.newCharacterinSerie(strings[0],strings[1],strings[2]);

					msg.setText(strings[0] + " adicionado à serie "+ strings[2] + " na posicao de "+ strings[1] + " com sucesso!");
					messageProducer.send(tmp, msg);
				}
			}
			else
			{

				String typeMessage = ((ObjectMessage) message).getJMSType();
				System.out.println("Mensagem recebida do tipo: "+typeMessage);
				
				if (typeMessage.equals("New_Serie")) {
					Serie ObjMessage = (Serie) ((ObjectMessage) message).getObject();

					JMSProducer messageProducer = context.createProducer();

					TextMessage TextMsgReply = context.createTextMessage();

					beanInterface.newSerie(ObjMessage);

					Destination tmp = message.getJMSReplyTo();
					
					
					TextMsgReply.setJMSReplyTo(tmp);
					TextMsgReply.setJMSType("SerieAdded");
					TextMsgReply.setText("Foi adicionada uma nova série com o seguinte nome: "+ ObjMessage.getSerieName());
					messageProducer.send(tmp, TextMsgReply);
				}
				else if (typeMessage.equals("Update_Serie"))
				{
					HashMap ObjMessage = (HashMap) ((ObjectMessage) message).getObject();
					
					JMSProducer messageProducer = context.createProducer();

					TextMessage TextMsgReply = context.createTextMessage();

					beanInterface.UpdateSerie(ObjMessage);

					Destination tmp = message.getJMSReplyTo();
					
					TextMsgReply.setJMSReplyTo(tmp);
					TextMsgReply.setJMSType("SerieUpdated");
					

					TextMsgReply.setText("A série "+ ObjMessage.get("SerieName")+" foi atualizada com sucesso!");
					
					messageProducer.send(tmp, TextMsgReply);
				}
			}
				

			

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
