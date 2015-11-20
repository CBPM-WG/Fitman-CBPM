/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.discovery;

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SPARQLResultHandler implements ContentHandler {

    private boolean bStartResults;
    private boolean bStartResult;
    private boolean bStartBinding;

    Set<String> result = new HashSet<String>();

    @Override
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        // TODO Auto-generated method stub
        if (bStartResults && bStartResult && bStartBinding) {
            String string = new String(arg0, arg1, arg2);
            if (string != null && string.trim().length() > 0) {
                result.add(string);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        if (arg1.equals("results")) {
            bStartResults = false;
            bStartResult = false;
            bStartBinding = false;
        }
        if (arg1.equals("result")) {
            bStartResult = false;
            bStartBinding = false;
        }
        if (arg1.equals("binding")) {
            bStartBinding = false;
        }
    }

    @Override
    public void endPrefixMapping(String arg0) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void processingInstruction(String arg0, String arg1) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDocumentLocator(Locator arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skippedEntity(String arg0) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        if (arg1.equals("results")) {
            result = new HashSet<String>();
            bStartResults = true;
        }
        if (arg1.equals("result")) {
            bStartResult = true;
        }
        if (arg1.equals("binding")) {
            if (((String) arg3.getValue("name")).equalsIgnoreCase("x")) {
                bStartBinding = true;
            }
        }
    }

    @Override
    public void startPrefixMapping(String arg0, String arg1) throws SAXException {
        // TODO Auto-generated method stub

    }

    public Set<String> getResult() {
        return result;
    }

}
