package org.app.sportmanager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SVGLoader {

    public SVGLoader() {
    }

    public String getStyleStr(String path){
        File file = null;
        try {
            file = new File(getClass().getResource("/assets/" + path).toURI());
        } catch (URISyntaxException e) {
            System.out.println("Error wrong URI SVGLoader.extractPathSVg(): " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Error wrong filepath SVGLoader.extractPathSVg(): " + e.getMessage());
        }
        String filepath;
        if (file != null){
            filepath = file.getAbsolutePath();
        }
        else{
            return null;
        }
        SVGPath svg = new SVGPath();
        try {
            String pathSVG = extractPathSVG(filepath);
            if (pathSVG != null){

                return pathSVG;
            }
            else{
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error reading stream SVGLoader.extractPathSVg(): " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error parse doc SVGLoader.extractPathSVg(): " + e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println("Error creating DocumentBuilder SVGLoader.extractPathSVg(): " + e.getMessage());
        }

        return null;
    }


    private String extractPathSVG(String filepath) throws IOException, SAXException, ParserConfigurationException {
        String svgContent = new String(Files.readAllBytes(Paths.get(filepath)));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(svgContent)));

        // Поиск элементов <path>
        NodeList pathNodes = doc.getElementsByTagName("path");

        // Извлечение атрибута "d" (путь)
        if (pathNodes.getLength() > 0) {
            Node pathNode = pathNodes.item(0);  // Получаем первый путь
            String pathData = pathNode.getAttributes().getNamedItem("d").getNodeValue();
            return pathData;
        }
        return null; // Если путь не найден
    }

}
