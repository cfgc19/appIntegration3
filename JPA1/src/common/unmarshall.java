package common;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import common.Project;

public class unmarshall {

	public static Project unmarshalles_project(File xmlFile) {
		Unmarshaller jaxbUnmarshaller;
		try {
			
			File schemaFile = new File("./src/common/schema.xsd");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = null;
			try {
				schema = sf.newSchema(schemaFile);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jaxbUnmarshaller.setSchema(schema);
			Project project = (Project) jaxbUnmarshaller.unmarshal(xmlFile);
			
			return project;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
