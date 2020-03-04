package com.westjonathan.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
// For data save:
import java.io.FileOutputStream;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {
    int[] gameData;
    String currName;
    String[] endMessages = new String[]{
            "... just skipping through every question?",
            "At least you got a few correct.",
            "This is the worst place to be; you tried somewhat, but still have a negative score.",
            "At least you're out of the negatives. That's pretty good, right?", // technically includes a score of negative one, but only even scores are possible
            "Decent.",
            "You're definitely getting somewhere.",
            "Hey, that's pretty good! Try to take less time though."};

    private String getResponse(int currScore, int time){
        int msgChoice = ((currScore+22) + (currScore+22) % 7) / 7; // add 22 to get min score to 0
        return endMessages[msgChoice] + " Your final score was " + currScore +", achieved in a time of "+(time/60)+":"+(time%60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        TextView msgBox = findViewById(R.id.msgBox);
        gameData = intent.getIntArrayExtra("sendData"); // [score, time]
        currName = intent.getStringExtra("sendName");
        msgBox.setText(getResponse(gameData[0], gameData[1]));
        writeToFile("nombres.txt", currName);
        writeToFile("scores.txt", Integer.toString(gameData[0]));
        writeToFile("times.txt", Integer.toString(gameData[1]));
    }

    public void writeToFile(String filename, String val) {// save new score
        byte[] bytesArray = (val+"\n").getBytes();
        try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_APPEND)) {
            fos.write(bytesArray);
        }
        catch (java.io.IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void returnToPrevious(View view) {
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        returnIntent.putExtra("returning?", true);
        startActivity(returnIntent);
        finish();
    }

    public void scorePage(View view) {
        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        startActivity(intent);
        finish();
    }
}