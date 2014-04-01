package com.munit.test.commons;

public class DataMapperMockResponse {

	/**This method is used accross other Unit-test cases. 
	 * @return xml String
	 */
	public static String getXMLPayload() {

		String xmlString =
				"<table>" +
					"<record>" +
						"<field name=\"COLUMN2\" >INDIA</field>" +
						"<field name=\"COLUMN1\" >1</field>" +
						"</record>" +
					"<record>" +
						"<field name=\"COLUMN2\">SYDNEY</field>" +
						"<field name=\"COLUMN1\">2</field>" +
					"</record>" +
				"</table>";
		return xmlString;
	}
}
