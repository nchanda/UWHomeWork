package com.example.homework314nchanda;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.homework314nchanda.adapter.DailyForecastPageAdapter;
import com.example.homework314nchanda.model.Weather;
import com.example.homework314nchanda.model.WeatherForecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView unitTemp;

	private TextView ftemp;
	private TextView unitfTemp;

	private ImageView imgView;

	private Button button;
	private EditText getcity;

	private static String forecastDaysNum = "3";
	private ViewPager pager;

	private String txt;
	private String lang = "en";

	JSONWeatherTask task;
	JSONForecastWeatherTask task1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String city = "Seattle, USA";

		// Finding Controls
		cityText = (TextView) findViewById(R.id.cityText);
		temp = (TextView) findViewById(R.id.temp);
		unitTemp = (TextView) findViewById(R.id.unittemp);
		unitTemp.setText("°C");
		condDescr = (TextView) findViewById(R.id.skydesc);

		pager = (ViewPager) findViewById(R.id.pager);
		imgView = (ImageView) findViewById(R.id.condIcon);

		ftemp = (TextView) findViewById(R.id.ftemp);
		unitfTemp = (TextView) findViewById(R.id.unitftemp);
		unitfTemp.setText("°F");

		// Calling Weather API using Background thread.
		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { city, lang });

		JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
		task1.execute(new String[] { city, lang, forecastDaysNum });

		getcity = (EditText) this.findViewById(R.id.cityEdittext);

		// Handling button click event.
		button = (Button) this.findViewById(R.id.btnAction);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Cilicking Button", "Button Click");
				txt = getcity.getText().toString().trim();
				getcity.setText("");

				// Calling Weather API
				JSONWeatherTask task2 = new JSONWeatherTask();
				task2.execute(new String[] { txt, lang });
				JSONForecastWeatherTask task3 = new JSONForecastWeatherTask();

				task3.execute(new String[] { txt, lang, forecastDaysNum });
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Asyn Task to handle weather API calls
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ((new WeatherHttpClient()).getWeatherData(params[0],
					params[1]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				System.out.println("Weather [" + weather + "]");
				weather.iconData = ((new WeatherHttpClient())
						.getImage(weather.currentCondition.getIcon()));

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			//setting up all controls with requried data
			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0,
						weather.iconData.length);
				imgView.setImageBitmap(img);
			}
			cityText.setText(weather.location.getCity() + ","
					+ weather.location.getCountry());
			temp.setText(""
					+ Math.round((weather.temperature.getTemp() - 275.15)));
			// temp.setText("" + Math.round(weather.temperature.getTemp()));
			ftemp.setText(" "
					+ Math.round((weather.temperature.getTemp() - 275.15) * 1.800 + 32.00));
			condDescr.setText(weather.currentCondition.getCondition() + "("
					+ weather.currentCondition.getDescr() + ")");
		}

	}

	private class JSONForecastWeatherTask extends
			AsyncTask<String, Void, WeatherForecast> {

		@Override
		protected WeatherForecast doInBackground(String... params) {

			String data = ((new WeatherHttpClient()).getForecastWeatherData(
					params[0], params[1], params[2]));
			WeatherForecast forecast = new WeatherForecast();
			try {
				forecast = JSONWeatherParser.getForecastWeather(data);
				System.out.println("Weather [" + forecast + "]");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return forecast;

		}

		@Override
		protected void onPostExecute(WeatherForecast forecast) {
			super.onPostExecute(forecast);

			//Setting up adapter to display forecast weather for next 3 days.
			DailyForecastPageAdapter adapter = new DailyForecastPageAdapter(
					Integer.parseInt(forecastDaysNum),
					getSupportFragmentManager(), forecast);
			pager.setAdapter(adapter);
		}

	}
}
