package com.example.tyler.webgraph;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WebGraph web;
    private int indexOfPage;
    private static TableLayout table;
    private Button addPageButton;
    private Button quitButton;
    final Context context = this;
    private static int sourceInt;
    private static int destInt;
    private static int[][] edges = new int[40][40];


    public static TableLayout getTable(){
        return  table;
    }

    public void addRow(WebPage newPage){
        TableRow row = new TableRow(this);
        TextView index = new TextView(this);
        index.setText(String.valueOf(newPage.getIndex()));
        index.setTextSize(30);
        index.setPadding(0, 0, 0, 20);
        TextView url = new TextView(this);
        url.setText(newPage.getUrl());
        url.setTextSize(30);
        url.setPadding(0, 0, 0, 20);
        TextView rank = new TextView(this);
        rank.setText(String.valueOf(newPage.getRank()));
        rank.setTextSize(30);
        rank.setPadding(0, 0, 0, 20);
        TextView links = new TextView(this);
        links.setText(newPage.getLinks());
        links.setTextSize(30);
        links.setPadding(0, 0, 0, 20);
        TextView keywords = new TextView(this);
        keywords.setText(newPage.getKeywords().toString());
        keywords.setTextSize(30);
        keywords.setPadding(0, 0, 0, 20);
        table.addView(row);
        row.addView(index);
        row.addView(url);
        row.addView(rank);
        row.addView(links);
        row.addView(keywords);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        table = (TableLayout) findViewById(R.id.table);
        addPageButton = (Button) findViewById(R.id.addPageButton);
        quitButton = (Button) findViewById(R.id.quitButton);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material));
                builder.setTitle("Add a page");


                LinearLayout addBox = new LinearLayout(context);
                addBox.setOrientation(LinearLayout.VERTICAL);

                final EditText url = new EditText(context);
                url.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(url);

                final EditText keywords = new EditText(context);
                keywords.setInputType(InputType.TYPE_CLASS_TEXT);

                addBox.addView(url);
                addBox.addView(keywords);

                builder.setView(addBox);

                builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String urlToAdd = url.getText().toString();
                        String keywordsToAdd = keywords.getText().toString();
                        String[] words = keywordsToAdd.split(" ");
                        ArrayList<String> keywordsList = new ArrayList<>();
                        for (int i = 0; i < words.length; i++) {
                            keywordsList.add(words[i]);
                        }
                        WebPage newPage = new WebPage(web.getCount(), urlToAdd, keywordsList);
                        web.addPage(urlToAdd, keywordsList);
                        addRow(newPage);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("Do you want to quit?");
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        AssetManager am = getAssets();
        web = WebGraph.buildFromFiles(am, "pages.txt", "links.txt");
        ArrayList<WebPage> pagesToAdd = web.getPages();
        web.updatePageRanks();
        for(int i = 0; i < pagesToAdd.size(); i++){
            addRow(pagesToAdd.get(i));
        }
       /*AssetManager am = getAssets();

        try {
            InputStream is = am.open("pages.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            InputStream linksIn = am.open("links.txt");
            BufferedReader linkReader = new BufferedReader(new InputStreamReader(linksIn));
            String linksString;

            while ((line = reader.readLine()) != null){
                String[] pageSplit = line.split(" ");
                String urlParam = pageSplit[4];
                ArrayList<String> keywordsParam = new ArrayList<>();
                for (int index = 5; index < pageSplit.length; index++)
                    keywordsParam.add(pageSplit[index]);
                WebPage newPage = new WebPage(indexOfPage, urlParam, keywordsParam);
                web.addPage(newPage);
                indexOfPage++;
                addRow(newPage);
            }
            while((linksString = linkReader.readLine()) != null){
                String[] linkSplit = linksString.split(" ");
                String source = linkSplit[4];
                String dest = linkSplit[5];
                sourceInt = web.getIndex(source);
                destInt = web.getIndex(dest);
                edges[sourceInt][destInt] = 1;
            }
            web.updatePageRanks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
