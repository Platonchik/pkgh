package com.example.admin.parserasp;


import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.SupportActionModeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends SearchGroup {

    //private TextView textview1;
    protected TextView textview2;
    mytask mt = new mytask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        //ListView listView = (ListView)findViewById(R.id.listView);

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //listView.setAdapter(adapter);
        //textview1 = (TextView)findViewById(R.id.textView1);
        textview2 = (TextView)findViewById(R.id.textView2);


        //textview1.setText(getIntent().getStringExtra("nameGroup"));
        textview2.setText(getIntent().getStringExtra("group"));
    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onBackPressed(){
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Click(View view) {

            mt.execute();
        }

    class mytask extends AsyncTask<Void, Void, Void> {
        private String raspElement;
        @Override
        protected Void doInBackground(Void... params) {

            Document doc=null;
            try{
                doc = Jsoup.connect("http://pkgh.edu.ru/obuchenie/shedule-of-classes.html").timeout(10*1000).get();
            }catch (IOException e) {
                e.printStackTrace();
            }
            if (doc!=null) {

                raspElement = doc.title();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //textview1.setText(raspElement);
        }

    }
}
