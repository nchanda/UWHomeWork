package com.example.homework311nchanda;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDetail extends Activity {

	private String title;
	private String content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdetail_item);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    title = extras.getString("title");
		    content = extras.getString("content");
		}
		
		 ImageView image = (ImageView) findViewById(R.id.icon);
   	     if(image != null)
   		   image.setImageResource(R.drawable.ic_launcher);
		TextView makeTextView = (TextView) findViewById(R.id.title);
		TextView contentTxtView = (TextView) findViewById(R.id.desc);
		makeTextView.setText(title);
		contentTxtView.setText(content);
	}

	
}
