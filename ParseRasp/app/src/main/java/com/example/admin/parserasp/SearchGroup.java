package com.example.admin.parserasp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Date;
import java.util.Iterator;

public class SearchGroup extends MainActivity implements TextWatcher {
        private AutoCompleteTextView group;
        protected String[] spisokGrp = {"ИВ-14-1", "ИВ-14-2", "ИВ-14-21", "ИВ-201", "ИВ-202",
                "ИВ-301", "ИВ-302", "ИВ-401", "ИВ-402", "ИП-14-22", "ИП-14-3", "ИП-14-4", "ИП-203",
                "ИП-204", "ИП-303", "ИП-304", "ИП-312", "ИП-403", "ИП-404", "ИП-411", "РА-14-5", "РА-14-6",
                "РА-205", "РА-305", "РА-314", "РА-405", "РА-414", "РЭ-14-7С", "СР-14-12", "СР-14-13",
                "СР-208", "СР-308", "СР-408", "ТМ-14-9", "ТМ-206", "ТМ-306", "ТМ-406", "ТО-14-8", "ТО-210", "ТО-310",
                "ТО-410", "УД-14-11", "УД-211", "УД-213К", "УК-14-10", "УК-207", "УК-307", "УК-311", "УК-407",
                "ЭЭ-14-23", "ЭЭ-313", "ЮС-14-14", "ЮС-14-15К", "ЮС-14-16К", "ЮС-209", "ЮС-212К", "ЮС-309"};
        private String groupName;
        public TextView textview;
        searchGroup sG = new searchGroup();
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.srchgroup);
            group = (AutoCompleteTextView) findViewById(R.id.editText);
            group.addTextChangedListener(this);
            group.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, spisokGrp));

            textview = (TextView)findViewById(R.id.textview2);
        }
        class searchGroup extends AsyncTask<String, Void, String> {
            String nameGroup;
            @Override
            protected String doInBackground(String... params) {
                groupName = group.getText().toString().toUpperCase();
                Document doc = null;
                String linkText;
                String mondayDate = "ПОНЕДЕЛЬНИК";
                String valueRetrun = null;
                Boolean bool=false;
                Integer i=0;

                try {
                    doc = Jsoup.connect("http://pkgh.edu.ru/obuchenie/shedule-of-classes.html").timeout(10*1000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (doc != null) {
                    String date = new SimpleDateFormat("EEEE").format(new Date());


                    ArrayList<Elements> numPar = new ArrayList<>();
                    ArrayList<Elements> namePar = new ArrayList<>();

                    Elements allRaspisanie = doc.select("div.column.one-fourth");
                    for (Element link : allRaspisanie) {
                        System.out.println(link.select("h4.expanded").text());

                        if(groupName.equals(link.select("h4.expanded").text())) {
                            Elements table = link.select("table.shedule");
                            for (Element link2 : table) {
                                System.out.println(link2.select("td.pnum.highlightable").text());
                                numPar.add(link2.select("td.pnum.highlightable"));
                            }

                            Elements trpair = table.select("tr.pair");

                            for (Element link2 : trpair) {
                                System.out.println(link2.select("p.pname").text());
                                namePar.add(link2.select("p.pname"));
                            }
                        }
                    }
                    System.out.println(numPar.get(0).text() + namePar.get(0).text() + namePar.get(1).text());



                    Elements content = doc.getElementsByClass("expanded");
                    Elements links = content.tagName("h4");

                    for (Element link : links) {
                        linkText = link.text();
                        if (linkText.equals(groupName)) {
                            valueRetrun = linkText;
                            break;
                        }
                    }


                    Elements contentTwo = doc.getElementsByClass("groupname");
                    Elements linksTwo = contentTwo.tagName("p");

                    for (Element link : linksTwo) {
                        linkText = link.text().toUpperCase();
                        if (linkText.equals(mondayDate)) {
                            valueRetrun += '\n' + linkText;
                            System.out.println(valueRetrun);
                            break;
                        }
                    }


                    do {
                        if (groupName.equals(spisokGrp[i])) {
                            nameGroup = spisokGrp[i];
                            bool = true;
                        } else
                            i++;
                    } while (!bool);
                }
                return valueRetrun;
            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Intent intentNumTwo = new Intent(SearchGroup.this, MainActivity.class);
                intentNumTwo.putExtra("nameGroup", nameGroup);
                intentNumTwo.putExtra("group", result);
                startActivity(intentNumTwo);
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
