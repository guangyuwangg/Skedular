package ca.uwaterloo.g28wang.a04;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	public String usrname;
	public String passwd;
	public Button loginButton;
	public Button createButton;
	public EditText entername;
	public EditText enterpasswd;
	public ProgressDialog pdialog;
	static final int DIALOG_WRONG_ID = 0;
	static final int DIALOG_NETWORK_ID = 1;
	/** Called when the activity is first created. */ 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Model.lactivity = this;
		setContentView(R.layout.main);
		this.loginButton = (Button) this.findViewById(R.id.login);
		this.createButton = (Button) this.findViewById(R.id.create);
		this.entername = (EditText)this.findViewById(R.id.entername);
		this.enterpasswd = (EditText)this.findViewById(R.id.enterpasswd);
		//button action listener for the button
		this.loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Model.lactivity.pdialog = ProgressDialog.show(Model.lactivity, "", "Loading. Please wait...",true,false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (Model.goSelectpage ==0) {
							Model.get_acc(entername.getText().toString(), enterpasswd.getText().toString());
						}	
						if (Model.goSelectpage == 1) {
							//Intent intent = new Intent((Activity)this, scheduleActivity.class);
							Intent intent = new Intent((Activity)Model.lactivity, myTabActivity.class);
							//Model.lactivity.pdialog.dismiss();
							startActivity(intent); 
						}
					}
				}).start();

			}
		});
		///////////////////////////////////////////////
		//Start create account activity 			 //
		///////////////////////////////////////////////
		this.createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(this,createActivity.class));
				createHandler();
			}
		});
		////////////////////////////////////////////////////////
		// Initialize LEC list, TUT list and LAB list		  //
		////////////////////////////////////////////////////////
		CourseSection holder;
		for (Course c: Model.courses) {
			for (Section s:c.getSections()) {
				if (s.getType().toString().equals("LEC")) {
					holder = new CourseSection(c, s);
					Model.Leclst.add(holder);
				}
				else if (s.getType().toString().equals("TUT")) {
					holder = new CourseSection(c, s);
					Model.Tutlst.add(holder);
				}
				else {
					holder = new CourseSection(c, s);
					Model.Lablst.add(holder);
				}
			}
		}
	}




	// create button handler
	private void createHandler(){
		if (this.createButton.getText().toString().equals("New Account")) {
			startActivity(new Intent((Activity)this, createActivity.class));
		}
		else {
			this.createButton.setText("New Account");
			this.loginButton.setText("Login");
			this.entername.setText("");
			this.enterpasswd.setText("");
			Model.lactivity = null;
			Model.cactivity = null;
			Model.sactivity = null;
			Model.goSelectpage = 0;
		}
	}
	// login button handler
	private void loginHandler(){
		Model.lactivity = this;
		if (Model.goSelectpage ==0) {
			Model.get_acc(entername.getText().toString(), enterpasswd.getText().toString());
		}	
		if (Model.goSelectpage == 1) {
			//Intent intent = new Intent((Activity)this, scheduleActivity.class);
			Intent intent = new Intent((Activity)this, myTabActivity.class);
			Model.lactivity.pdialog.dismiss();
			startActivity(intent); 
		}
	}

	/** Called when we need to create a dialog 
	 * @return **/
	public Dialog onCreateDialog(int id){

		Dialog dialog;
		switch (id) {
		case DIALOG_WRONG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Invalid Username or Password. Please try again!");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog = builder.create();
			return dialog;
		case DIALOG_NETWORK_ID:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setMessage("HTTP ERROR. Please check your network connection!");
			builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
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