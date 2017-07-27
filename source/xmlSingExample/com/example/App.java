package com.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Collections;

/**
 * Example of JSR 105, XML Digital Signature API
 *
 */
public class App {

    private static KeyPair keyPair = keyPairGenerate();

    public static void main( String[] args ) {
        try {
            Document doc = readXml(App.class.getResourceAsStream("Example.xml"));
            System.out.println("Sing XML");
            singDoc(doc);
            String xml = toStringXml(doc);
            System.out.println(xml);
            System.out.println("Check sing");
            Element el = doc.createElement("test");
            doc.getDocumentElement().appendChild(el);
            System.out.println(checkSing(doc));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static KeyPair keyPairGenerate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            keyPairGenerator.initialize(512);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Document readXml(final InputStream is) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(is);
    }

    public static String toStringXml(final Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
        return stringWriter.toString();
    }

    public static void singDoc(final Document docWithOutSing) throws Exception {
        DOMSignContext domSignContext = new DOMSignContext(keyPair.getPrivate(), docWithOutSing.getDocumentElement());
        XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
        Reference reference = xmlSignatureFactory.newReference( "", xmlSignatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(xmlSignatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec)null)), null, null);
        SignedInfo signedInfo = xmlSignatureFactory.newSignedInfo(xmlSignatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
                (C14NMethodParameterSpec)null), xmlSignatureFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null), Collections.singletonList(reference));
        KeyInfoFactory keyInfoFactory = xmlSignatureFactory.getKeyInfoFactory();
        KeyValue keyValue = keyInfoFactory.newKeyValue(keyPair.getPublic());
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));
        XMLSignature xmlSignature = xmlSignatureFactory.newXMLSignature(signedInfo, keyInfo);
        xmlSignature.sign(domSignContext);
    }

    public static boolean checkSing(final Document documentWithSing) throws Exception {
        NodeList nodeList = documentWithSing.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nodeList.getLength() == 0) {
            throw new RuntimeException("Nothing to check!");
        }
        DOMValidateContext domValidateContext = new DOMValidateContext(new KeySelector() {
            @Override
            public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
                return new KeySelectorResult() {

                    @Override
                    public Key getKey() {
                        return keyPair.getPublic();
                    }
                };
            };
        }, nodeList.item(0));
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        XMLSignature xmlSignature = fac.unmarshalXMLSignature(domValidateContext);
        return xmlSignature.validate(domValidateContext);
    }
}



