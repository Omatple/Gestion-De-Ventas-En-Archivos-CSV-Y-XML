import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ventas {

    static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String FICHERO_CSV = String.format("%s%s%s", "datos", File.separator, "ventas.csv");
    private static final String FICHERO_XML = String.format("%s%s%s", "datos", File.separator, "ventas.xml");
    private static final String SEPARADOR = ",";
    private static final String RAIZ = "ventas";
    private static final String VENTA = "venta";
    private static final String FECHA = "fecha";
    private static final String PRODUCTO = "producto";
    private static final String CANTIDAD = "cantidad";
    private static final String PRECIO_UNITARIO = "precioUnitario";
    private List<Venta> coleccionVentas;

    public Ventas() {
        coleccionVentas = new ArrayList<>();
    }

    public void comenzar() {
        procesarFicheroCSV(FICHERO_CSV);
    }

    public void soloTranscribir() {
        lecturaEscrituraFicheroCSVaXML(FICHERO_CSV, FICHERO_XML);
    }

    private void lecturaEscrituraFicheroCSVaXML(String ficheroCSV, String ficheroXML) {
        String linea;
        List<List<String>> listaDelistaDatosFichero = new ArrayList<>();
        try (BufferedReader entrada = new BufferedReader(new FileReader(ficheroCSV))) {
            while ((linea = entrada.readLine()) != null) {
                listaDelistaDatosFichero.add(Arrays.asList(linea.split(SEPARADOR)));
            }
            System.out.printf("Fichero %s leído correctamente.%n", ficheroCSV);
        } catch (FileNotFoundException e) {
            System.out.println("No se puede leer el fichero.");
        } catch (IOException e) {
            System.out.println("Error inesperado de Entrada/Salida.");
        }
        DocumentBuilder constructor = UtilidadesXml.crearConstructorDocumentoXml();
        Document documentoXml = null;
        if (constructor != null) {
            documentoXml = constructor.newDocument();
            documentoXml.appendChild(documentoXml.createElement(RAIZ));
            for (int i = 1; i < listaDelistaDatosFichero.size(); i++) {
                Element elementoVenta = documentoXml.createElement(VENTA);
                for (int j = 0; j < listaDelistaDatosFichero.get(i).size(); j++) {
                    elementoVenta.setAttribute(listaDelistaDatosFichero.get(0).get(j).toLowerCase(), listaDelistaDatosFichero.get(i).get(j));
                }
                documentoXml.getDocumentElement().appendChild(elementoVenta);
            }
        }
        UtilidadesXml.escribirDocumentoXml(documentoXml, ficheroXML);
    }

    private void procesarFicheroCSV(String fichero) {
        try (BufferedReader entrada = new BufferedReader(new FileReader(fichero))) {
            String linea;
            entrada.readLine();
            while ((linea = entrada.readLine()) != null) {
                String[] datosVenta = linea.split(SEPARADOR);
                LocalDate fecha = LocalDate.parse(datosVenta[0]);
                String producto = datosVenta[1];
                int cantidad = Integer.parseInt(datosVenta[2]);
                float precioUnitario = Float.parseFloat(datosVenta[3]);
                coleccionVentas.add(new Venta(fecha, producto, cantidad, precioUnitario));
            }
            System.out.printf("Fichero %s leído correctamente.%n", fichero);
        } catch (FileNotFoundException e) {
            System.out.println("No es posible leer el fichero.");
        } catch (IOException e) {
            System.out.println("Error inesperado de Entrada/Salida.");
        }
    }

    public void terminar() {
        Document documentoXml = crearDocumento();
        UtilidadesXml.escribirDocumentoXml(documentoXml, FICHERO_XML);
    }

    private Document crearDocumento() {
        DocumentBuilder contructor = UtilidadesXml.crearConstructorDocumentoXml();
        Document documentoXml = null;
        if (contructor != null) {
            documentoXml = contructor.newDocument();
            documentoXml.appendChild(documentoXml.createElement(RAIZ));
            for (Venta venta : coleccionVentas) {
                Element elementoVenta = getElemento(documentoXml, venta);
                documentoXml.getDocumentElement().appendChild(elementoVenta);
            }
        }
        return documentoXml;
    }

    public Map<String, String[]> estadisticasVentas(LocalDate mes) {
        Map<String, String[]> mapaEstadisticas = inicializarEstadisticas();
        String[] arrayEstadisticas;
        int indice = 0;
        int cantidadTotal;
        float ingresoTotal;
        for (Venta venta : coleccionVentas) {
            if ((venta.fecha().getMonth().equals(mes.getMonth())) && (venta.fecha().getYear() == mes.getYear())) {
                arrayEstadisticas = mapaEstadisticas.get(venta.producto());
                cantidadTotal = (Integer.parseInt(arrayEstadisticas[indice++]) + venta.cantidad());
                ingresoTotal = (Float.parseFloat(arrayEstadisticas[indice]) + (venta.precioUnitario() * venta.cantidad()));
                indice = 0;
                arrayEstadisticas[indice++] = String.format("%s", cantidadTotal);
                arrayEstadisticas[indice] = String.format("%s", ingresoTotal);
                indice = 0;
                mapaEstadisticas.put(venta.producto(), arrayEstadisticas);
            }
        }
        return mapaEstadisticas;
    }

    private Map<String, String[]> inicializarEstadisticas() {
        Map<String, String[]> mapaEstadisticas = new HashMap<>();
        String[] arrayEstadisticas = {"0", "0"};
        for (Venta venta : coleccionVentas) {
            if (!mapaEstadisticas.containsKey(venta.producto())) {
                mapaEstadisticas.put(venta.producto(), arrayEstadisticas);
            }
        }
        return mapaEstadisticas;
    }

    private Element getElemento(Document documentoXml, Venta venta) {
        Element elementoVenta = documentoXml.createElement(VENTA);
        elementoVenta.setAttribute(FECHA, venta.fecha().format(FORMATO_FECHA));
        elementoVenta.setAttribute(PRODUCTO, venta.producto());
        elementoVenta.setAttribute(CANTIDAD, String.format("%s", venta.cantidad()));
        elementoVenta.setAttribute(PRECIO_UNITARIO, String.format(Locale.US, "%.2f", venta.precioUnitario()));
        return elementoVenta;
    }
}
