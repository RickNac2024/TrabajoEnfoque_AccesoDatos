/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabajoenfoque;

import java.io.FileOutputStream;
import java.sql.Statement;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TrabajoEnfoque {

    public static void main(String[] args) {
        // capturar los errores y al final utilizamos el multicath. 
        try {
            // instanciamos la clase perfil para trabajar con ella durante el código.
            Perfil canturador = new Perfil();
            
            /**
             * NOTA: la coneccion a la base de datos se hace con mysql y se utiliza la version mysql-connector-j-9.1.0
             */
            
            // conección a la base de datos y creamos el statement para crear la base de datos, la tabla y más adelante insertar los datos obenidos.
            Statement queryConeccion = Perfil.conexion("root", "Nacional2022");
            // creación base de datos y como parametro el nombre de la bbdd.
            canturador.crearDataBase(queryConeccion, "contratos");
            //creación de la tabla llamada "cliente" y con valores predeterminados.
            canturador.crearTabla(queryConeccion);

            // es la encargada de moldear el documento para más adelante poder utilizar el DocumentBuilder.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // analizar el xml y generar el arbol DOM del archivo .
            DocumentBuilder builder = factory.newDocumentBuilder();
            // el Document es el punto de entrada para trabajar en memoria  con la estructura del arbol Dom .
            Document ubiDcoumento = builder.parse("src/trabajoenfoque/archivos/contratos-adjudicados-sep-24.xml");

            // se crea la instancia para mas adelante poder utilizar las expresiones Xpath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            //aqui indicamos que vamos a crear una nueva expresion 
            XPath xPath = xPathFactory.newXPath();
            // introducimos la expresion para obtener los nodos, se utliza el metodo Compile para poder trabar con la misma expresion de una más forma rápida 
            XPathExpression xxpe = xPath.compile("//Row");
            // indicamos el archivo a trabajar y como queremos que se guarde las respuesta. NODE ,, NODESET;
            NodeList listaTitulos = (NodeList) xxpe.evaluate(ubiDcoumento, XPathConstants.NODESET);
            
            
            //variables a utilizar dentro del For
            String nifs;
            String adjudicatarios;
            String objetoGenericos;
            String objetos;
            String fechaAdjudicaciones;
            String importes;
            String proveedores;
            String tipoContrato;
            // recorrer todos los nodos ROW para obetener la información
            for (int i = 0; i < listaTitulos.getLength(); i++) {
                Node elemento = listaTitulos.item(i);

                /**
                 * *
                 * en este bloque de codigo se va obtener la informacion segun
                 * su posicion dentro del nodo ROW ya que esta informacion esta
                 * dentro de etiquetas <Data><Data/>
                 * y no hay forma de obtener los elementos de forma organizadas
                 * (utilizando el metodo getElementsByTagName); entonces dentro
                 * del For se busca la información mediante su posicionamiento
                 * dentro de las etiquetas ROW.En otro caso se busca según su
                 * contenido clave, como es el caso del importe el cual tiene en
                 * comun el signo del euro(€), y se entiende como patron para
                 * buscar la etiqueta.
                 */
                nifs = xPath.evaluate("Cell[1]/Data", elemento);
                nifs = (nifs.length() > 9) ? nifs.substring(1, 9) : nifs; // algunos nifs tienen mas de los 9 caracteres por lo solo obtenemos los 9 primeros
                canturador.setNif(nifs); // capturamos la informacion obtenida del nif y lo mandamos a la clase Perfil

                adjudicatarios = xPath.evaluate("Cell[2]/Data", elemento);
                canturador.setAdjudicatario(adjudicatarios);// capturamos la informacion obtenida del adjudicatario y lo mandamos a la clase Perfil

                objetoGenericos = xPath.evaluate("Cell[3]/Data", elemento);
                canturador.setObjetoGenerico(objetoGenericos); // capturamos la informacion obtenida del objeto generico y lo mandamos a la clase Perfil

                objetos = xPath.evaluate("Cell[4]/Data", elemento);
                canturador.setObjeto(objetos);// capturamos la informacion obtenida del objeto  y lo mandamos a la clase Perfil

                fechaAdjudicaciones = xPath.evaluate("Cell/Data[@Type='DateTime']", elemento);
                canturador.setFechaAdjudicacion(fechaAdjudicaciones);// capturamos la informacion obtenida del fecha adjudicaciones  y lo mandamos a la clase Perfil

                importes = xPath.evaluate("Cell/Data[contains(text(), '€')]", elemento);
                canturador.setImporte(importes);// capturamos la informacion obtenida del  importes  y lo mandamos a la clase Perfil

                proveedores = xPath.evaluate("Cell[last()-2]/Data[@Type='String']", elemento);
                canturador.setProveedoresConsultados(proveedores);// capturamos la informacion obtenida del  proveedores  y lo mandamos a la clase Perfil

                tipoContrato = xPath.evaluate("Cell[last()-1]/Data", elemento);
                canturador.setTipoContrato(tipoContrato);// capturamos la informacion obtenida del  tipo contrato  y lo mandamos a la clase Perfil

                /**
                 * en esta linea de codigo hacemos la insercion de datos en la
                 * base de datos con los datos obtenidos en la iteracion numero
                 * (i), con el Statement que ya se ha obtenido en la conección.
                 */
                canturador.Insertar(queryConeccion);
                System.out.println(i);// cantidad de datos introducidos

            }

            /**
             * Después de haber insertado la información se procede a quitar el
             * "tipo de contrato" por lo que primero debemos hacer el ubicar la
             * etiqueta y generar las lista de nodos para poder recorrerlos y
             * poder borrarlos uno a uno como lo muestra el siguiente código.
             */
            String rutaXPath = "//Cell[last()-1]/Data";
            NodeList borrador = (NodeList) xPath.evaluate(rutaXPath, ubiDcoumento, XPathConstants.NODESET);

            for (int i = 0; i < borrador.getLength(); i++) {
                /**
                 * se alamacena en una variable tipo Nodo para poder ejecutar el
                 * metodo "getParentNode" para obtener el Nodo padre y después
                 * poder borrarlo "removeChild"
                 */

                Node pruebaBorrador = borrador.item(i);
                pruebaBorrador.getParentNode().removeChild(pruebaBorrador);
            }
                
            
            // se utiliza para poder crear objetos de tipo Transforme más adelante
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // instanciamos para crear el objeto transformador que usaremos mas adelante
            Transformer transformador = transformerFactory.newTransformer();
             // aqui indicamos el origen del xml , donde indicamos que proviene del recuro de DOM cargado en memoria.
            DOMSource origen = new DOMSource(ubiDcoumento);
            // creamos la salida del nuevo xml del documento .
            OutputStream salida = new FileOutputStream("src/trabajoenfoque/archivos/contratos-adjudicados-sep-24-sin-tipoContrato.xml"); // nombre para sacar el archivo.
            // esta clase nos ayuda a construir un xml con la ayuda del FileOutputStream y del Transformer para poder sacar el xml sin el tipo de contratos.
            StreamResult result = new StreamResult(salida);
            transformador.transform(origen, result); //decimo que recree el xml y le damos  la varialbe de salida.

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | SQLException | TransformerException ex) {
            Logger.getLogger(TrabajoEnfoque.class.getName()).log(Level.SEVERE, "REVISA LA INFORMACION INTRODUCIDA SEA LA CORRECTA", ex);
            
        }
    }
}
