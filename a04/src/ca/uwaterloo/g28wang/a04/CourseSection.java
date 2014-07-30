package ca.uwaterloo.g28wang.a04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CourseSection {
	public final Course course;
	public final Section section;

	public CourseSection(Course course, Section section) {
		this.course = course;
		this.section = section;
	}
	
	public static ArrayList<CourseSection> get_my_courses(){
		Course tmpCourse;
		Section tmpSection = null;
		ArrayList<String[]> lst = new ArrayList<String[]>();
		ArrayList<CourseSection> my_courses = new ArrayList<CourseSection>();
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the input and also register a private class for call backs
			sp.parse("http://anlujo.cs.uwaterloo.ca/cs349/getCourses.py?user_id="+Model.get_account().get_id()+"&passwd="+Model.get_account().get_passwd(), new getCourseParserCallBacks(lst));

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		for (int i = 0; i < lst.size(); i++) {
			//find the courses the account selected 
			for (Course c: Model.courses) {
				if (c.getSubject().equals(lst.get(i)[0]) && c.getCatalog().equals(lst.get(i)[1])) {
					tmpCourse = c;
					//find the sections of the course
					for (int j=0; j<tmpCourse.getSections().length; j++) {
						if (tmpCourse.getSections()[j].getSec().equals(lst.get(i)[2])) {
							tmpSection = tmpCourse.getSections()[j];
							break;
						}
					}
					my_courses.add(new CourseSection(tmpCourse, tmpSection));
					break;
				}
			}
		}
		return my_courses;
		
	}
}



//a class to parse my courses
class getCourseParserCallBacks extends DefaultHandler {
	private List<String[]> lst;
	
	private String tmpVal;
	private String subject;
	private String catalog;
	private String section;
	// values to add to the course currently being parsed.
	
	private String[] courseinfo;
	
	
	public getCourseParserCallBacks(List<String[]> lst) {
		this.lst = lst;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("course")) {
			this.courseinfo = new String[3];
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.tmpVal = new String(ch, start, length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("course")) {
			this.courseinfo = new String[3];
			this.courseinfo[0] = this.subject;
			this.courseinfo[1] = this.catalog;
			this.courseinfo[2] = this.section;
			this.lst.add(courseinfo);
		} else if (qName.equalsIgnoreCase("section")) {
			this.section = this.tmpVal;
		} else if (qName.equalsIgnoreCase("subject")) {
			this.subject = this.tmpVal;
		} else if (qName.equalsIgnoreCase("catalog")) {
			this.catalog = this.tmpVal;
		} 
	}
}
