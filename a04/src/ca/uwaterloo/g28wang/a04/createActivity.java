package ca.uwaterloo.g28wang.a04;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;



public class createActivity extends Activity {
    ///////////////////////////////////////////////
	//define some varible, component			 //
	///////////////////////////////////////////////
	public String username;
	public String passwd;
	public String surname;
	public String given_name;
	public EditText uname;
	public EditText pword;
	public EditText pword2;
	public EditText sname;
	public EditText gname;
	public Button createButton;
	public Button cancelButton;
	static final int DIALOG_DUPLICATE_ID = 0;
	static final int DIALOG_WRONG_ID = 1;
	static final int DIALOG_NETWORK_ID = 2;
	static final int DIALOG_NOTMATCH_ID = 3;
	//////////   Constructor     /////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		my_model = new Model();
//		my_model.cactivity = this;
		setContentView(R.layout.new_account);
		//link wigits with layout file
		this.createButton = (Button) this.findViewById(R.id.button1);
		this.cancelButton = (Button) this.findViewById(R.id.button2);
		this.uname = (EditText) this.findViewById(R.id.editText1);
		this.pword = (EditText) this.findViewById(R.id.editText2);
		this.pword2 = (EditText)this.findViewById(R.id.editText5);
		this.sname = (EditText) this.findViewById(R.id.editText3);
		this.gname = (EditText) this.findViewById(R.id.editText4);
		///////////////////////////////////////////////
		//set listeners for two buttons				 //
		///////////////////////////////////////////////
		this.createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createHandler();
			}
		});
		this.cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelHandler();
			}
		});
	}
	
	private void createHandler(){
		if ((gname.getText().toString().length()>0)&&(sname.getText().toString().length()>0)&&pword.getText().toString().equals(pword2.getText().toString())&&(pword.getText().toString().length()>=4)&&(uname.getText().toString().length()>=4)) {
			Model.cactivity = this;
			Model.create_acc(uname.getText().toString(), pword.getText().toString(), sname.getText().toString(), gname.getText().toString());
			if (Model.goSelectpage == 1) {
				Intent intent = new Intent((Activity)this, scheduleActivity.class);
				startActivity(intent);
			}
		}
		else {
			this.showDialog(DIALOG_NOTMATCH_ID);
		}
	}
	private void cancelHandler(){
		finish();
	}
	
	///////////////////////////////////////////////
	//create dialogs for different exceptions	 //
	///////////////////////////////////////////////
	public Dialog onCreateDialog(int id){

		Dialog dialog;
		switch (id) {
		case DIALOG_DUPLICATE_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Duplicate account information. Please try again!");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog = builder.create();
			return dialog;
		case DIALOG_WRONG_ID:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage("Please enter valid information!");
			builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog = builder1.create();
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
		case DIALOG_NOTMATCH_ID:
			AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
			builder3.setMessage("Please check the information you've entered.\n\n"+"*Username can not be empty."+"\n"+"*Both username and  password should be at least 4 characters long\n"+"*Both passwords should be identical");
			builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog = builder3.create();
			return dialog;
			
		default:
			dialog = null;
			return dialog;
		}
	}
	
	
}
