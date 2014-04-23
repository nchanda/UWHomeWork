package com.example.homework311nchanda;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<ArticleData>{

	 private ArrayList<ArticleData> mArticle;
	 Context context;
	public CustomListViewAdapter(Context context, int resource,
			ArrayList<ArticleData> articles) {
		super(context, resource, articles);
		 Log.d("CustomAdapater","Constructor of CstmAdpater");
		this.mArticle = articles;
		this.context = context;
	}

	  @Override
      public View getView(int position, View convertView, ViewGroup parent) {
		  Log.d("CustomAdapater2","Setting up adapater");
		  View v = convertView;
          if (v == null) {
              LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              v = vi.inflate(R.layout.list_item, null);
          }

          Log.d("CustomAdapater3",String.valueOf(position));
          ArticleData a = mArticle.get(position);
          if(a!= null)
          {
        	  Log.d("CustomAdapater4","Inside a");
        	  ImageView image = (ImageView) v.findViewById(R.id.icon);
        	  if(image != null)
        		  image.setImageResource(R.drawable.ic_launcher);    
        	  TextView makeTextView = (TextView) v.findViewById(R.id.title);
              if (makeTextView != null)
                  makeTextView.setText(a.title);
              TextView idTextView = (TextView) v.findViewById(R.id.id);
              if (idTextView != null)
            	  idTextView.setText(String.valueOf(a.id));
          }
		  return v;
      }
}
