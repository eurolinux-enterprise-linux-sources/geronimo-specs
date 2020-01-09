/**
 *
 * Copyright 2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.schema.SchemaConversionUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import junit.framework.TestCase;

/**
 * ejb 1.1 dtd appears to be a subset of ejb 2.0 dtd so the same xsl should
 * work for both.
 *
 * @version $Revision: 1.1 $ $Date: 2004/07/25 08:18:00 $
 *
 * */
public class SchemaConversionUtilsTest extends TestCase {

//comment on validity of j2ee 1.4 schemas: validation doesn't work...
//        From: "Radu Preotiuc-Pietro" <radup@bea.com>
//        Date: Tue Jun 15, 2004  3:37:50 PM US/Pacific
//        To: <xmlbeans-user@xml.apache.org>
//        Subject: RE: Problem with validate -- xsb schema file missing/wrong name
//        Reply-To: xmlbeans-user@xml.apache.org
//
//        Unfortunately, there is an issue in XmlBeans v1 having to do with duplicate id constraints definitions.
//        XmlBeans v2 does not have this issue.
//        Also, these ejb Schemas are techically incorrect because they violate the id constraint uniqueness rule (at least when processed together, you could try and compile each one separately)
//        So, there are a couple of options:
//        1. you hand-edit the schemas to rename those problematic id constraints
//        2. you upgrade to v2
//        Well, there is a third alternative, which is a fix integrated in XmlBeans v1, may or may not be feasible
//
//        Radu

    //I've taken option (1) and fixed the schemas

    public void testXMLBeansTransform() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_3dtd/ejb-jar.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_3dtd/ejb-jar-21.xml");
        XmlObject xmlObject = XmlObject.Factory.parse(srcXml);
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        SchemaConversionUtils.validateDD(expected);
        xmlObject = SchemaConversionUtils.convertToEJBSchema(xmlObject);
//        System.out.println(xmlObject.toString());
//        System.out.println(expected.toString());
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
        //make sure trying to convert twice has no bad effects
        XmlCursor cursor2 = xmlObject.newCursor();
        try {
            String schemaLocationURL = "http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd";
            String version = "2.1";
            assertFalse(SchemaConversionUtils.convertToSchema(cursor2, schemaLocationURL, version));
        } finally {
            cursor2.dispose();
        }
        boolean ok2 = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences after reconverting to schema: " + problems, ok2);
        //do the whole transform twice...
        xmlObject = SchemaConversionUtils.convertToEJBSchema(xmlObject);
        boolean ok3 = compareXmlObjects(xmlObject, expected,  problems);
        assertTrue("Differences after reconverting to ejb schema: " + problems, ok3);
    }

    public void testOrderDescriptionGroup() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_3dtd/DescriptionGroupTestSource.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_3dtd/DescriptionGroupTestExpected.xml");
        XmlObject srcObject = XmlObject.Factory.parse(srcXml);
        XmlCursor srcCursor = srcObject.newCursor();
        XmlCursor moveable = srcObject.newCursor();
        try {
            srcCursor.toFirstChild();
            srcCursor.toFirstChild();
            assertTrue(srcCursor.getName().toString(), "filter".equals(srcCursor.getName().getLocalPart()));
            do {
                srcCursor.push();
                srcCursor.toFirstChild();
                SchemaConversionUtils.convertToDescriptionGroup(srcCursor, moveable);
                srcCursor.pop();
            } while (srcCursor.toNextSibling());
        } finally {
            srcCursor.dispose();
        }
//        System.out.println(srcObject.toString());
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(srcObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
    }

    public void testOrderJNDIEnvironmentRefsGroup() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_3dtd/JNDIEnvironmentRefsGroupTestSource.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_3dtd/JNDIEnvironmentRefsGroupTestExpected.xml");
        XmlObject srcObject = XmlObject.Factory.parse(srcXml);
        XmlCursor srcCursor = srcObject.newCursor();
        XmlCursor moveable = srcObject.newCursor();
        try {
            srcCursor.toFirstChild();
            srcCursor.toFirstChild();
            assertTrue(srcCursor.getName().toString(), "web-app".equals(srcCursor.getName().getLocalPart()));
            do {
                srcCursor.push();
                srcCursor.toFirstChild();
                srcCursor.toNextSibling();
                srcCursor.toNextSibling();
                moveable.toCursor(srcCursor);
                SchemaConversionUtils.convertToJNDIEnvironmentRefsGroup(srcCursor, moveable);
                srcCursor.pop();
            } while (srcCursor.toNextSibling());
        } finally {
            srcCursor.dispose();
        }
//        System.out.println(srcObject.toString());
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(srcObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
    }

    public void testWeb23To24Transform() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_3dtd/web-23.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_3dtd/web-24.xml");
        XmlObject xmlObject = XmlObject.Factory.parse(srcXml);
        xmlObject = SchemaConversionUtils.convertToServletSchema(xmlObject);
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
        xmlObject = SchemaConversionUtils.convertToServletSchema(xmlObject);
        boolean ok2 = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok2);
    }

    public void testWeb23To24OtherTransform() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_3dtd/web-1-23.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_3dtd/web-1-24.xml");
        XmlObject xmlObject = XmlObject.Factory.parse(srcXml);
        xmlObject = SchemaConversionUtils.convertToServletSchema(xmlObject);
        System.out.println(xmlObject.toString());
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
        xmlObject = SchemaConversionUtils.convertToServletSchema(xmlObject);
        boolean ok2 = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok2);
    }

    public void testEJB21To21DoesNothing() throws Exception {
        File srcXml = new File("src/test-data/j2ee_1_4schema/ejb-jar.xml");
        File expectedOutputXml = new File("src/test-data/j2ee_1_4schema/ejb-jar.xml");
        XmlObject xmlObject = XmlObject.Factory.parse(srcXml);
        xmlObject = SchemaConversionUtils.convertToEJBSchema(xmlObject);
        XmlObject expected = XmlObject.Factory.parse(expectedOutputXml);
        List problems = new ArrayList();
        boolean ok = compareXmlObjects(xmlObject, expected, problems);
        assertTrue("Differences: " + problems, ok);
    }

    private boolean compareXmlObjects(XmlObject xmlObject, XmlObject expectedObject, List problems) {
        XmlCursor test = xmlObject.newCursor();
        XmlCursor expected = expectedObject.newCursor();
        boolean similar = true;
        int elementCount = 0;
        while (toNextStartToken(test)) {
            elementCount++;
            if (!toNextStartToken(expected)) {
                problems.add("test longer than expected at element: " + elementCount);
                return false;
            }
            String actualChars = test.getName().getLocalPart();
            String expectedChars = expected.getName().getLocalPart();
            if (!actualChars.equals(expectedChars)) {
                problems.add("Different elements at elementCount: " + elementCount + ", test: " + actualChars + ", expected: " + expectedChars);
                similar = false;
            }
            test.toNextToken();
            expected.toNextToken();
        }
        return similar;
    }

    private boolean toNextStartToken(XmlCursor cursor) {
        while (!cursor.isStart()) {
            if (!cursor.hasNextToken()) {
                return false;
            }
            cursor.toNextToken();
        }
        return true;
    }

}
