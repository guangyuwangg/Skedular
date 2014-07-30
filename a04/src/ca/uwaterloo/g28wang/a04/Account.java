package ca.uwaterloo.g28wang.a04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * A user account for the skedulr web service.
 *
 */
public class Account {

	final String userId;
	final String password;
	final String surname;
	final String givenNames;

	/**
	 * Construct an account based on data already in the database.
	 * 
	 * @param user
	 * @param passwd
	 */
//	public Account(String user, String passwd) {
//		this.userId = user;
//		this.password = passwd;
//		
//		// Work to do here!
//		this.surname = "";
//		this.givenNames = "";
//
//	}
	public Account(String user, String passwd) 
			throws HttpPostException, WrongLoginMsgError {
		
		this.userId = user;
		this.password = passwd;
		String[] accinfo;// = new String[2];
		
		String result = HttpUtil.httpPost(
				"http://anlujo.cs.uwaterloo.ca/cs349/getAccount.py",
				new String[] { "user_id", "passwd"},
				new String[] { user, passwd});
		if (result.matches("<\\?.*\\?><account>.*")){
			
			accinfo = getAccount(user, passwd);
			this.surname = accinfo[0];
			this.givenNames = accinfo[1];
			return;
		}
		else {
			throw new WrongLoginMsgError(result);
		}
	}
	
	public String get_id(){
		return this.userId;
	}
	public String get_passwd(){
		return this.password;
	}
	public String get_surname(){
		return this.surname;
	}
	public String get_given(){
		return this.givenNames;
	}
	

	/**
	 * A function that create a factory to parse the account info into an array
	 */
	public String[] getAccount(String user, String passwd) {
		String[] accinfo = new String[3];
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the input and also register a private class for call backs
			sp.parse("http://anlujo.cs.uwaterloo.ca/cs349/getAccount.py?user_id="+user+"&passwd="+passwd, new accParserCallBacks(accinfo));
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		return accinfo;
	}
	
	private Pattern accountExistsRE = Pattern
			.compile("<\\?.*\\?><error>Account for user_id ([a-zA-Z0-9]+) already exists.</error>");

	/**
	 * Construct an account that is not yet in the database.
	 * 
	 * @param userId
	 * @param passwd
	 * @param surname
	 * @param givenNames
	 */
	public Account(String user, String passwd, String surname, String givenNames)
			throws DuplicateAccountError, HttpPostException,
			AccountCreationError {
		this.userId = user;
		this.password = passwd;
		this.surname = surname;
		this.givenNames = givenNames;
		
		String result = HttpUtil.httpPost(
				"http://anlujo.cs.uwaterloo.ca/cs349/createAccount.py",
				new String[] { "user_id", "passwd", "surname", "given_names" },
				new String[] { user, passwd, surname, givenNames });

		if (Pattern.matches("<\\?.*\\?><status>OK</status>", result))
			return;

		Matcher m = this.accountExistsRE.matcher(result);
		if (m.matches()){
			throw new DuplicateAccountError(m.group(1));}
		else
			throw new AccountCreationError(result);
	}

}



/**
 * A class to parse account info, similar to the one in the course.
 */
class accParserCallBacks extends DefaultHandler {
	private String[] accinfo;
	
	private String tmpVal;
	
	// values to add to the course currently being parsed.
	
	public accParserCallBacks(String[] accinfo) {
		this.accinfo = accinfo;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.tmpVal = new String(ch, start, length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("surname")) {
			this.accinfo[0] = this.tmpVal;
		} else if (qName.equalsIgnoreCase("given_names")) {
			this.accinfo[1] = this.tmpVal;
		}
	}
}

/**
 * An exception for signaling attempts to create a duplicate account.
 */
class DuplicateAccountError extends Exception {
	public DuplicateAccountError(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountCreationError extends Exception {
	public AccountCreationError(String msg) {
		super(msg);
	}
}

class WrongLoginMsgError extends Exception {
	public WrongLoginMsgError(String msg) {
		super(msg);
	}
}
