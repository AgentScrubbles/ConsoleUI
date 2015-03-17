package web_connections;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HttpWebService {

	private String _url;

	public HttpWebService(String url) {
		_url = url;
	}

	public InputStream callWebService(Map<String, String> params)
			throws MalformedURLException, IOException {
		String charset = "UTF-8";
		StringBuilder query = new StringBuilder();

		Iterator<String> keyIter = params.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			query.append(String.format("%s=%s", key,
					URLEncoder.encode(params.get(key), charset)));
			if (keyIter.hasNext()) {
				query.append("&");
			}
		}

		URLConnection connection = new URL(_url + "?" + query.toString())
				.openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		InputStream response = connection.getInputStream();

		return response;
	}

	public Document readXml(InputStream is) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		// dbf.setCoalescing(true);
		// dbf.setExpandEntityReferences(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		// db.setErrorHandler( new MyErrorHandler());

		return db.parse(is);
	}

	class NullResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}
}
