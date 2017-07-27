package com.example;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Example of XSLT
 */
public class XLSTTransform {

    public static void main(String[] args) {
        String xml = process();
        System.out.println(xml);
    }

    public static String process() {
        InputStream xslInputStream = null;
        InputStream xmlInputStream = null;
        try {
            xslInputStream = XLSTTransform.class.getResourceAsStream("Example.xsl");
            xmlInputStream = XLSTTransform.class.getResourceAsStream("Example.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(xmlInputStream);
            DOMSource source = new DOMSource(document);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            StreamSource xslStreamSource = new StreamSource(xslInputStream);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
            transformer.transform(source, result);
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            closeResources(xslInputStream);
            closeResources(xmlInputStream);
        }
    }

    public static void closeResources(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
