package ca.uwaterloo.g28wang.a04;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class scheduleActivity extends Activity{
    ///////////////////////////////////////////////
	//define some variables, component			 //
	///////////////////////////////////////////////
	public String username;
	public String passwd;
	public String surname;
	public String given_name;
	public TableLayout layout;
	public ArrayList<CourseSection>to_be_remove = new ArrayList<CourseSection>();
	//////////   Constructor     /////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.schedule);
		CheckBox checkBox;
		super.onCreate(savedInstanceState);
		Model.sactivity = this;
		Model.my_course = CourseSection.get_my_courses();
		////////////////////////////////////////////////////
		// layout the courses of this account			  //
		//////////////////////////////////////////////////// 
		TextView title = new TextView(this);
		TableRow tr;
		Button removeButton = (Button) findViewById(R.id.button2);
		Button backButton = (Button) findViewById(R.id.button3);
		layout = (TableLayout) findViewById(R.id.tableLayout2);
		if (Model.my_course.size() != 0) {
			for (CourseSection c: Model.my_course) {
				checkBox = new CheckBox(this);
				checkBox.setText(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec());//c.course.getSubject()+c.course.getCatalog());
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1 == true) {
							for (CourseSection c: Model.my_course) {
								if (arg0.getText().equals(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec())) {
									to_be_remove.add(c);
								}
							}
						}
						else {
							for (CourseSection c: Model.my_course) {
								if (arg0.getText().equals(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec())) {
									to_be_remove.remove(c);
								}
							}
						}
					}
				});
				tr = new TableRow(this);
				title = new TextView(this);
				title.setText(" "+c.section.getRoom()+"\n"+c.section.getFormattedTime());//c.section.getType()+c.section.getSec()+"\n"+c.section.getFormattedTime());
				tr.addView(checkBox);
				tr.addView(title);
				layout.addView(tr);
			}
		}
		removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeHandler();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backHandler();
			}
		});
	}
	//button handlers 
	private void backHandler(){
		Model.lactivity.loginButton.setText("My Schdule");
		Model.lactivity.createButton.setText("Logout");
		finish();
	}
	private void removeHandler(){
		if (to_be_remove.size() != 0) {
			for (CourseSection c:to_be_remove) {
				Model.my_course.remove(c);
			}
			to_be_remove = new ArrayList<CourseSection>();
			//remove all courses having right now 
			layout.removeAllViewsInLayout();
			//update all my_courses to display
			CheckBox checkBox;
			TextView title;
			TableRow tr;
			List<String> to_be_add = new ArrayList<String>();
			for (CourseSection c: Model.my_course) {
				checkBox = new CheckBox(this);
				checkBox.setText(c.course.getSubject()+c.course.getCatalog());
				tr = new TableRow(this);
				title = new TextView(this);
				title.setText(c.section.getType()+c.section.getSec()+"\n"+c.section.getFormattedTime());
				tr.addView(checkBox);
				tr.addView(title);
				layout.addView(tr);
				to_be_add.add(c.course.getSubject());
				to_be_add.add(c.course.getCatalog());
				to_be_add.add(c.section.getSec());
			}
			try {
				replaceCourses(to_be_add);
			} catch (HttpPostException e) {
				// TODO Auto-generated catch block
				//httpError dialog here!!!!!!!!!!!!!
			}
		}
		else {
			//create a dialog saying "no course is selected"
		}
	}
	
	public void replaceCourses(List<String> courses) throws HttpPostException {
		/*
		 * notice that the size of the list must be a multiple of 3
		 * create two string array as the input of httoPost
		 */
		String[] coursearray = new String[courses.size()+2];
		String[] coursearray2 = new String[courses.size()+2];
		coursearray[0] = "user_id";
		coursearray[1] = "passwd";
		coursearray2[0] = Model.get_account().get_id();
		coursearray2[1] = Model.get_account().get_passwd();
		
		for (int i =0; i < courses.size(); i++) {
			coursearray[i+2] = "subject";
			coursearray2[i+2] = courses.get(i);
			i++;
			coursearray[i+2] = "catalog";
			coursearray2[i+2] = courses.get(i);
			//System.out.println(courses.get(i));
			i++;
			coursearray[i+2] = "section";
			coursearray2[i+2] = courses.get(i);
		}
		String result = HttpUtil.httpPost(
				"http://anlujo.cs.uwaterloo.ca/cs349/replaceCourses.py",
				coursearray,
				coursearray2);

		if (Pattern.matches("<\\?.*\\?><status>OK</status>", result))
			return;	
	}
}