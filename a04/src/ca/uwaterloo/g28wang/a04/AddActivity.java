package ca.uwaterloo.g28wang.a04;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity{
	EditText subject;
	EditText catalog;
	TableLayout layout;
	public CharSequence[] items;
	public ArrayList<String>selected_tut = new ArrayList<String>();
	public ArrayList<String>selected_lab = new ArrayList<String>();
	static final int SHOW_TUT = 0;
	static final int SHOW_LAB = 1;
	
	public List<CourseSection> selected_c = new ArrayList<CourseSection>();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		Model.aactivity = this;
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Button add = (Button) findViewById(R.id.add);
		Button back = (Button) findViewById(R.id.back);
		Button search = (Button) findViewById(R.id.search);
		subject = (EditText)findViewById(R.id.subject);
		catalog = (EditText)findViewById(R.id.catalog);
		layout = (TableLayout)findViewById(R.id.tableLayout1);
		//declare Variable
		TableRow tr;
		TextView tv;
		CheckBox cb;
		//adding courses
		for (CourseSection c: Model.Leclst) {
			tr = new TableRow(this);
			tv = new TextView(this);
			tv.setText(" "+c.section.getRoom()+"\n"+c.section.getFormattedTime());
			cb = new CheckBox(this);
			cb.setText(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec());
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
					if (isChecked == false) {
						for (CourseSection c:selected_c) {
							if (buttonView.getText().equals(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec())) {
								selected_c.remove(c);
								break;
							}
						}
					}
					else {
						for (CourseSection c:Model.Leclst){
							if (buttonView.getText().toString().equals(c.course.getSubject()+c.course.getCatalog()+c.section.getType()+c.section.getSec())) {
								selected_c.add(c);
								for (CourseSection cs: Model.Tutlst) {
									if (c.course == cs.course) {
										String tutSection = cs.course.getSubject()+cs.course.getCatalog()+" "+cs.section.getType()+" " +cs.section.getFormattedTime();
										selected_tut.add(tutSection);
				
									}
								}
								if (selected_tut.size()>0) {
									items = selected_tut.toArray(new CharSequence[selected_tut.size()]);
									selected_tut.clear();
									showDialog(SHOW_TUT);
								}
								
								for (CourseSection cs: Model.Lablst) {
									if (c.course == cs.course) {
										String tutSection = cs.course.getSubject()+cs.course.getCatalog()+" "+cs.section.getType()+" " +cs.section.getFormattedTime();
										selected_lab.add(tutSection);
				
									}
								}
								if (selected_lab.size()>0) {
									items = selected_lab.toArray(new CharSequence[selected_lab.size()]);
									selected_lab.clear();
									showDialog(SHOW_LAB);
								}
								//System.err.println("Added!!");
								//System.err.println(selected_c.size());
								return;
							}
						}
					}
				}
			});
			tr.addView(cb);
			tr.addView(tv);
			layout.addView(tr);
		}
		
		//set onClickListener for add button
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				
				if (selected_c.size() != 0) {
					Model.my_course.addAll(selected_c);
					//remove all courses having right now
					TableLayout tlayout = Model.sactivity.layout; 
					tlayout.removeAllViewsInLayout();
					//update all my_courses to display 
					CheckBox checkBox;
					TextView title;
					TableRow tr;
					List<String> to_be_add = new ArrayList<String>();
					for (CourseSection c: Model.my_course) {
						checkBox = new CheckBox(Model.sactivity);
						checkBox.setText(c.course.getSubject()+c.course.getCatalog());
						tr = new TableRow(Model.sactivity);
						title = new TextView(Model.sactivity);
						title.setText(c.section.getType()+c.section.getSec()+"\n"+c.section.getFormattedTime());
						tr.addView(checkBox);
						tr.addView(title);
						tlayout.addView(tr);
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
			}
		});
		
		////set onClickListener for back button
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				backHandler();
			}
		});
		
		//set onClickListener for search button
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchHandler();
			}
		});
	}
	
	//searchHandler function, search the course in the textfield
	private void searchHandler(){
		ArrayList<Course> to_be_display = new ArrayList<Course>();
		String sub = subject.getText().toString();
		String cat = catalog.getText().toString();
		if (sub.length() != 0) {
			for (Course c: Model.courses) {
				if (c.getSubject().equals(sub)) {
					if (cat.length() != 0) {
						if (c.getCatalog().equals(cat)) {
							to_be_display.add(c);
						}
					}
					else {
						to_be_display.add(c);
					}
				}
			}
		}
		else {
			//create a dialog and say error here!!!!
		}
		if (to_be_display.size() == 0) {
			//create a dialog and say "no such course found!!!"
		}
		else {
			TableRow tr;
			TextView tv;
			CheckBox cb;
			layout.removeAllViewsInLayout();
			for (Course c: to_be_display) {
				for (Section s: c.getSections()) {
					tr = new TableRow(this);
					tv = new TextView(this);
					tv.setText(s.getType()+" "+s.getRoom()+"\n"+s.getFormattedTime()+"\n");
					cb = new CheckBox(this);
					cb.setText(c.getSubject()+c.getCatalog()+"Sec"+s.getSec());
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked == false) {
								for (CourseSection c:selected_c) {
									if (buttonView.getText().equals(c.course.getSubject()+c.course.getCatalog()+"Sec"+c.section.getSec())) {
										selected_c.remove(c);
										break;
									}
								}
							}
							else {
								for (Course cou:Model.courses) {
									for (Section sec:cou.getSections()) {
										if (buttonView.getText().equals(cou.getSubject()+cou.getCatalog()+"Sec"+sec.getSec())) {
											selected_c.add(new CourseSection(cou,sec));
											return;
										}
									}
								}
							}
						}
					});
					tr.addView(cb);
					tr.addView(tv);
					layout.addView(tr);
				}
			}
		}
		
	}
	
	
	//backHandler function, go back to schedule page
	private void backHandler(){
		this.finish();
	}
	
	//a function to replace courses
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
	
	
	
	//create a dialog 
	
	public Dialog onCreateDialog(int id){
		Dialog dialog;
		switch (id) {
		case SHOW_TUT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a TUT");
			builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	for (CourseSection cs: Model.Tutlst) {
						if (items[item].toString().equals(cs.course.getSubject()+cs.course.getCatalog()+" "+cs.section.getType()+" " +cs.section.getFormattedTime())) {
							selected_c.add(cs);
							break;
						}
					}
			    	Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
			    	dialog.dismiss();
			    }
			});
			dialog = builder.create();
			return dialog;
		case SHOW_LAB:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("Pick a LAB");
			builder2.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	for (CourseSection cs: Model.Lablst) {
						if (items[item].toString().equals(cs.course.getSubject()+cs.course.getCatalog()+" "+cs.section.getType()+" " +cs.section.getFormattedTime())) {
							selected_c.add(cs);
							break;
						}
					}
			    	Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
			    	dialog.dismiss();
			    }
			});
			dialog = builder2.create();
			return dialog;
		default:
			dialog = null;
			return dialog;
		}
	}
	
	
}



