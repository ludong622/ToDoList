package comp5216.sydney.edu.au.comp5216hw1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;

public class EditToDoItemActivity extends Activity {
	public int position=0;
	EditText etItem;
	TextView iTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		//Get the data from the main screen
		String editItem = getIntent().getStringExtra("item");
		String itemTime = getIntent().getStringExtra("time");
		position = getIntent().getIntExtra("position",-1);

		// show original content in the text field
		etItem = (EditText)findViewById(R.id.etEditItem);
		iTime = (TextView)findViewById(R.id.iTime);
		etItem.setText(editItem);
		iTime.setText(itemTime);
	}

	public void onSubmit(View v) {

	  	etItem = (EditText) findViewById(R.id.etEditItem);

	  	// Prepare data intent for sending it back
	  	Intent intent = new Intent();

	  	Date date2 = new Date();

	  	User newItem = new User(etItem.getText().toString(), date2.toString());

	  	// Pass relevant data back as a result
	  	intent.putExtra("item", newItem);
	  	intent.putExtra("position", position);

	  	// Activity finished ok, return the data
	  	setResult(RESULT_OK, intent); // set result code and bundle data for response

		//startActivity(intent);
	  	finish(); // closes the activity, pass data to parent
	}

	public void onCancel(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
		builder.setTitle(R.string.dialog_cancel_title)
				.setMessage(R.string.dialog_cancel_msg)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						// User cancelled the dialog
						// Nothing happens
					}
				});
		builder.create().show();
	}

}
