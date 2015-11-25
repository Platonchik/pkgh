package com.example.admin.parserasp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class SearchGroup extends ActionBarActivity implements TextWatcher {

        protected String[] spisokGrp = {"ИВ-14-1", "ИВ-14-2", "ИВ-14-21", "ИВ-201", "ИВ-202",
                "ИВ-301", "ИВ-302", "ИВ-401", "ИВ-402", "ИП-14-22", "ИП-14-3", "ИП-14-4", "ИП-203",
                "ИП-204", "ИП-303", "ИП-304", "ИП-312", "ИП-403", "ИП-404", "ИП-411", "РА-14-5", "РА-14-6",
                "РА-205", "РА-305", "РА-314", "РА-405", "РА-414", "РЭ-14-7С", "СР-14-12", "СР-14-13",
                "СР-208", "СР-308", "СР-408", "ТМ-14-9", "ТМ-206", "ТМ-306", "ТМ-406", "ТО-14-8", "ТО-210", "ТО-310",
                "ТО-410", "УД-14-11", "УД-211", "УД-213К", "УК-14-10", "УК-207", "УК-307", "УК-311", "УК-407",
                "ЭЭ-14-23", "ЭЭ-313", "ЮС-14-14", "ЮС-14-15К", "ЮС-14-16К", "ЮС-209", "ЮС-212К", "ЮС-309"};
        private String groupName;
        private AutoCompleteTextView group;
        searchGroup sG = new searchGroup();
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.srchgroup);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){  //checks if its lower than Honeycomb
                android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                actionBar.hide();
            }

            //TextView textview = (TextView)findViewById(R.id.textview2);

            group = (AutoCompleteTextView) findViewById(R.id.editText);
            group.addTextChangedListener(this);
            group.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, spisokGrp));

    }

        class searchGroup extends AsyncTask<String, Void, ArrayList<String[]>> {
            //String nameGroup;

            @Override
            protected ArrayList<String[]> doInBackground(String... params) {
                groupName = group.getText().toString().toUpperCase();

                Document doc = null;
                //String linkText;
                //String valueRetrun = null;
                //Boolean bool=false;
                Integer i=0;
                ArrayList<Elements> numPar = new ArrayList<>();
                ArrayList<String[]> namePar = new ArrayList<>();

                try {
                    doc = Jsoup.connect("http://pkgh.edu.ru/obuchenie/shedule-of-classes.html").timeout(10*1000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (doc != null) {
                    String date = new SimpleDateFormat("EEEE").format(new Date(1)).toUpperCase();


                    Elements allRaspisanie = doc.select("div.column.one-fourth");
                    for (Element link : allRaspisanie) {
                        System.out.println(link.select("h4.expanded").text());

                        Elements table = link.select("table.shedule");
                        if(groupName.equals(link.select("h4.expanded").text())) {
                                for (Element link2 : table) {
                                    System.out.println(link2.select("td.pnum.highlightable").text());
                                    numPar.add(link2.select("td.pnum.highlightable"));
                                }

                            }
                        addList(table, link, date, namePar);
                    }


                   /** Elements content = doc.getElementsByClass("expanded");
                    Elements links = content.tagName("h4");
                    for (Element link : links) {
                        linkText = link.text();
                        if (linkText.equals(groupName)) {
                            valueRetrun = linkText;
                            break;
                        }
                    }*/


                    /**Elements contentTwo = doc.getElementsByClass("groupname");
                    Elements linksTwo = contentTwo.tagName("p");
                    for (Element link : linksTwo) {
                        linkText = link.text().toUpperCase();
                        if (linkText.equals(date)) {
                            valueRetrun += '\n' + linkText;
                            System.out.println(valueRetrun);
                            break;
                        }
                    }*


                    do {
                        if (groupName.equals(spisokGrp[i])) {
                            nameGroup = spisokGrp[i];
                            bool = true;
                        } else
                            i++;
                    } while (!bool);*/
                }
                return namePar;
            }

            protected void onPostExecute(ArrayList<String[]> result) {
                super.onPostExecute(result);
                Intent intentNumTwo = new Intent(SearchGroup.this, MainActivity.class);
                System.out.println(result);
                intentNumTwo.putExtra("group", result);
                startActivity(intentNumTwo);
            }

        }

        public void addList(Elements table, Element link, String date, ArrayList<String[]> namePar){
            for(Element link2 : table){
                Elements trpair = link2.select("tr.pair");
                //System.out.println(link2.select("p.groupname").text().toUpperCase());
                int j = 0;
                if(date.equals(link2.select("p.groupname").text().toUpperCase()) &&
                        groupName.equals(link.select("h4.expanded").text())) {
                    String[] namePara = new String[5];
                    for (Element link3 : trpair) {
                        namePara[j] = link3.select("p.pname").text();
                        System.out.println(namePara[j]);
                        j++;
                    }
                    namePar.add(namePara);
                }

                /**System.out.println("////");
                for(int d = 0; d < namePar.size(); d++) {
                    System.out.println(Arrays.toString(namePar.get(d)));
                }
                //String empty = "";
                //Arrays.fill(namePara, empty);*/
            }
        }

        public void click(View view) {
            sG.execute();
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }
    }
