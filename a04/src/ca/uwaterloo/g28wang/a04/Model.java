package ca.uwaterloo.g28wang.a04;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.widget.TableLayout;

public class Model {
	public static Account my_acc;
	public static final List<Course> courses = Course.coursesFactory();
	public static ArrayList<CourseSection> my_course;
	public static ArrayList<CourseSection> Leclst = new ArrayList<CourseSection>();
	public static ArrayList<CourseSection> Tutlst = new ArrayList<CourseSection>();
	public static ArrayList<CourseSection> Lablst = new ArrayList<CourseSection>();
	public static LoginActivity lactivity;
	public static createActivity cactivity;
	public static scheduleActivity sactivity;
	public static AddActivity aactivity;
	public static int goCreatepage = 0;
	public static int goSelectpage = 0;
	
	
	public static void get_acc(String user, String password){
		try {
			my_acc = new Account(user, password);
			goSelectpage=1;
			//  go to select course page
			
			
		} catch (WrongLoginMsgError e) {
			// TODO: handle exception
			lactivity.showDialog(lactivity.DIALOG_WRONG_ID);
			
		} catch (HttpPostException e) {
			// TODO: handle exception
			lactivity.showDialog(lactivity.DIALOG_NETWORK_ID);
		}
	}
	
	public static Account get_account(){
		return my_acc;
	}
	
	public static void create_acc(String user, String passwd, String surname, String givenNames){
		try {
			my_acc = new Account(user, passwd, surname, givenNames);
			goSelectpage = 1;
			
			
			//go to select course page
		
		
		
		} catch (DuplicateAccountError e) {
			// TODO: handle exception
			cactivity.showDialog(cactivity.DIALOG_DUPLICATE_ID);
		} catch (HttpPostException e) {
			// TODO: handle exception
			cactivity.showDialog(cactivity.DIALOG_NETWORK_ID);
		} catch (AccountCreationError e) {
			// TODO: handle exception
			cactivity.showDialog(cactivity.DIALOG_WRONG_ID);
		}
	}

	
}
