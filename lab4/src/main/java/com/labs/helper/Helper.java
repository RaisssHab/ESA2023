package com.labs.helper;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import javax.xml.transform.Transformer;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Helper {
    public static String generateXmlResponse(Object object, String xslString, @Nullable Class<?>... classesToBeBound) {
        try {
            // Convert data to XML using JAXB
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(classesToBeBound);

            StringWriter sw = new StringWriter();
            marshaller.marshal(object, new StreamResult(sw));

            String xmlString = sw.toString();

            // Transform XML to HTML using XSLT
            StringWriter result = new StringWriter();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            ResourceLoader resourceLoader = new DefaultResourceLoader();
            // Obtain the Resource for the XSLT file
            Resource resource = resourceLoader.getResource("classpath:" + xslString);

            StreamSource xslt = new StreamSource(resource.getInputStream());
            xslt.setSystemId(resource.getURI().toString());
            Transformer transformer = transformerFactory.newTransformer(xslt);

            StreamSource source = new StreamSource(new StringReader(xmlString));
            transformer.transform(source, new StreamResult(result));

            // Return the transformed HTML
            return result.toString();
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return "Error generating XML response";
        }
    }

    public static ResponseEntity<Object> response(Object output, String acceptHeader, String xslString, @Nullable Class<?>... classesToBeBound) {
        if (acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            // Return HTML
            return ResponseEntity.ok(generateXmlResponse(output, xslString, classesToBeBound));
        } else {
            // Default to JSON
            return ResponseEntity.ok(output);
        }
    }
}
