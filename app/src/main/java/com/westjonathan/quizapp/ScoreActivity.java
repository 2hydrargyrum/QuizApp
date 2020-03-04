package com.westjonathan.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
// For data save:
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    ListView listview;
    String[] ListElements = new String[]{"Name - Score - Time"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        listview = findViewById(R.id.listView1);

        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getApplicationContext(), android.R.layout.simple_list_item_1, ListElementsArrayList);
        listview.setAdapter(adapter);

        ArrayList<String> pastNames = readFromFile("nombres.txt");
        ArrayList<String> pastScores = readFromFile("scores.txt");
        ArrayList<String> pastTimes = readFromFile("times.txt");
        Log.i("Names read from file: ", pastNames.toString());
        for(int i = 0; i < pastNames.size(); i++){
            int time = Integer.parseInt(pastTimes.get(i));
            ListElementsArrayList.add(pastNames.get(i)+"    -   "+pastScores.get(i)+"   -   "+(time/60)+":"+(time%60));
            adapter.notifyDataSetChanged();
        }
    }
    public ArrayList<String> readFromFile(String fileName) {// reads text file and saves to arraylist
        ArrayList<String> ret = new ArrayList<>();
        try{
            FileInputStream fis = getApplicationContext().openFileInput(fileName);// potential FileNotFoundException
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);//potential IOException
            String line = reader.readLine();
            while (line != null) {
                ret.add(line);
                line = reader.readLine();
            }
        } catch (java.io.FileNotFoundException e){
            Log.e("Reading files:", "FileNotFoundException");
        } catch (java.io.IOException e) {
            // Error occurred when opening raw file for reading.
            Log.e("Reading files:", "IOException");
        }
        return ret;
    }
    public void returnToPrevious(View view) {
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        returnIntent.putExtra("returning?", true);
        startActivity(returnIntent);
        finish();
    }
}