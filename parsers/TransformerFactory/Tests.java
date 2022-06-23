package parsers.TransformerFactory;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Tests {
  public static void main(String [] args) {
    // ignore error messages
    System.setErr(new PrintStream(new OutputStream() {
        public void write(int b) {
        }
    }));

    try {
      System.out.println("TESTING TransformerFactory configurations");
      testDefaultConfig();
      testSetFeatureSecreProcessing();
    } catch (Exception e) {
        System.out.println(e.getMessage());
        // do nothing
    }
  }

  public static void testDefaultConfig() {
    System.out.println("Default Config");
    TransformerFactory tf = TransformerFactory.newInstance();
    parseAll(tf);
  }

  public static void testSetFeatureSecreProcessing() throws TransformerConfigurationException {
    System.out.println("setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)");
    TransformerFactory tf = TransformerFactory.newInstance();
    tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    parseAll(tf);
  }



  public static void parseAll(TransformerFactory tf) {
    parseXmlBomb(tf);
    parseInputWithSchema(tf);
    parseInputWithStylesheet(tf);
    parseInputWithParameterEntity(tf);
  }

  public static void parseXmlBomb(TransformerFactory tf) {
    try {
        System.out.print("    Parse XML Bomb: ");
        File input = new File("payloads/input-dos/xml-bomb.xml");
        File style = new File("payloads/input-with-stylesheet/ok-stylesheet.xsl");
        File output = new File("payloads/input-with-stylesheet/output.csv");

        StreamSource source = new StreamSource(input);
        StreamSource xsltSource = new StreamSource(style);

        Transformer transformer = tf.newTransformer(xsltSource);
        
        Result target = new StreamResult(output);
        transformer.transform(source, target);
        System.out.println("Secure");
    } catch (Exception e) {
      if (e.getMessage().contains("entity expansions in this document; this is the limit imposed by the JDK.")){
          System.out.println("Insecure");
      } else if(e.getMessage().contains("DOCTYPE is disallowed")){
          System.out.println("Secure");
      }else{
          System.out.println(e.getMessage());
          System.out.println("Insecure");
      }
    }
  }

  public static void parseInputWithSchema(TransformerFactory tf) {
    try {
        System.out.print("    Parse Xml Input With Schema: ");
        File input = new File("payloads/input-with-schema/input.xml");
        File style = new File("payloads/input-with-stylesheet/ok-stylesheet.xsl");
        File output = new File("payloads/input-with-stylesheet/output.csv");

        StreamSource source = new StreamSource(input);
        StreamSource xsltSource = new StreamSource(style);

        Transformer transformer = tf.newTransformer(xsltSource);
        
        Result target = new StreamResult(output);
        transformer.transform(source, target);
        System.out.println("Secure");
    } catch (Exception e) {
        if(e.getMessage().contains("Connection refused")){
            System.out.println("Insecure");
        } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
            System.out.println("Secure");
        } else if(e.getMessage().contains("DOCTYPE is disallowed")){
            System.out.println("Secure");
        }else{
            System.out.println(e.getMessage());
        }
    }
  }

  public static void parseInputWithStylesheet(TransformerFactory tf) {
    try {
      System.out.print("    Parse Xml Input With Stylesheet: ");
      File style = new File("payloads/input-with-stylesheet/stylesheet.xsl");
      StreamSource xsltSource = new StreamSource(style);
      tf.newTransformer(xsltSource);
      System.out.println("Secure");
    } catch (TransformerConfigurationException e) {
      if(e.getMessage().contains("Connection refused")){
        System.out.println("Insecure");
      } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
        System.out.println("Secure");
      } else if(e.getMessage().contains("DOCTYPE is disallowed")){
        System.out.println("Secure");
      } else {
        System.out.println(e.getMessage());
      }
    }
  }

    public static void parseInputWithParameterEntity(TransformerFactory tf){
        try {
          System.out.print("    Parse Xml Input With Parameter Entity: ");
          File input = new File("payloads/input-with-stylesheet/input-parameter-entity.xml");
          File style = new File("payloads/input-with-stylesheet/ok-stylesheet.xsl");
          File output = new File("payloads/input-with-stylesheet/output.csv");

          StreamSource source = new StreamSource(input);
          StreamSource xsltSource = new StreamSource(style);

          Transformer transformer = tf.newTransformer(xsltSource);
          
          Result target = new StreamResult(output);
          transformer.transform(source, target);
          System.out.println("Secure");
        } catch (Exception e){
            if(e.getMessage().contains("Connection refused")){
                System.out.println("Insecure");
            } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
                System.out.println("Secure");
            } else if(e.getMessage().contains("DOCTYPE is disallowed")){
                System.out.println("Secure");
            }
            else {
                System.out.println(e.getMessage());
            }
        }
    }
}
