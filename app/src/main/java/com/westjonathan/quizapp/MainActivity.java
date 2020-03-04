package com.westjonathan.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // Declare android elements
    Button submitButton;
    Button scorePageBtn;
    EditText responseText;
    TextView dispMessage;
    TextView dispTime;
    TextView displayScore;

    String[] respuestas = new String[]{"Badinage", "Macabre", "Irrefutable", "Echelon", "Allege", "Fatuous", "Lackadaisical", "Juggernaut", "exacerbate", "conciliate",
            "arrant","countermand","saturnine","litany","substantive","recant","paucity","raze","portend","melange","saturate","slough"};
    String[] preguntas = new String[]{"(n.) Light or playful banter or teasing during conversation",
            "(adj.) Gruesome or horribly or dealing with death",
            "(adj.) Cannot be disproved or argued against",
            "(n.) A level of authority or command in an organization like the military",
            "(v.) To assert as true without proof or positiveness",
            "(adj.) Foolish or stupid or unintelligent",
            "(adj.) Without spirit or interest or effort",
            "(n.) A massive, overpowering destructive force that crushes everything in its way",
            "(v.) To increase the bitterness or pain of something",
            "(v.) To win or overcome the distrust of another",
            "(adj.) Glaringly obvious, outright, blatant;",
            "(v.) To cancel or reverse a previous command",
            "(adj.) A gloomy or sullen mood",
            "(n.) A recital of prayers in response to a leader; A long or drawn-out account",
            "(adj.) Solid or real or pertaining to practical importance",
            "(v.) To withdraw one's belief or statement formally",
            "(n.) Smallness of number or scarce",
            "(v.) To tear down to the ground or completely destroy",
            "(v.) To foreshadow or warn in advance",
            "(n.) A mixture or medley of things",
            "(v.) To soak or fill to capacity",
            "(v.) To shed or cast off"
    };
    String currName;
    int score = 0;
    int time = 0;
    Timer timer = new Timer();
    private Toast mainToast = null;

    private void scoreUpdate(int dif){ // update current score after each answer
        score += dif;
        String temp = "Score: " + score;
        displayScore.setText(temp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize elements
        scorePageBtn=findViewById(R.id.scoreButton);
        submitButton=findViewById(R.id.submitButton);
        responseText=findViewById(R.id.responseEditText);
        dispMessage=findViewById(R.id.msgBox);
        dispTime = findViewById(R.id.dispTime);
        displayScore = findViewById(R.id.dispScore);
        //Setup what is shown on initial page
        dispTime.setVisibility(View.INVISIBLE);
        displayScore.setVisibility(View.INVISIBLE);
        //if returned-to from end screen:
        Intent intent = getIntent();
        String returnGreeting = "Welcome back! Please reenter your name, or change it if you'd like.";
        if(intent.hasExtra("returning?")) {
            dispMessage.setText(returnGreeting);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currName = responseText.getText().toString();
                // Welcome message
                if (mainToast != null) mainToast.cancel();//eliminate previous toasts, if any remain
                mainToast = Toast.makeText(getApplicationContext(), "Welcome to the game "+currName+"!", Toast.LENGTH_SHORT);
                mainToast.show();
                dispMessage.setText(preguntas[0]);
                dispTime.setVisibility(View.VISIBLE);
                displayScore.setVisibility(View.VISIBLE);
                scorePageBtn.setVisibility((View.INVISIBLE));
                responseText.setHint("              ");
                responseText.setText("");
                // Game Timer
                timer.scheduleAtFixedRate(new TimerTask(){
                    public void run() { runOnUiThread(new Runnable() {
                            public void run() {
                    TextView timeView = dispTime;
                    String timeText = "Timer: ";
                    int sec = time % 60;
                    int min = time / 60;
                    timeText += min + ":" + sec;
                    timeView.setText(timeText);
                    time += 1; // increment seconds
                } });
                    }
                }, 0, 1000);// call each second
                // Answer submissions:
                submitButton.setOnClickListener(new View.OnClickListener(){
                    int questNum = 0;
                    public void onClick(View view) {
                        // correct answer submitted
                        if (responseText.getText().toString().trim().equalsIgnoreCase(respuestas[questNum])) {
                            scoreUpdate(1);
                        } else {// incorrect answer submitted
                            scoreUpdate(-1);
                            // Give answer
                            if (mainToast != null) mainToast.cancel();//eliminate previous toasts, if any remain
                            mainToast = Toast.makeText(getApplicationContext(), "CORRECT ANSWER: "+respuestas[questNum], Toast.LENGTH_SHORT);
                            mainToast.show();
                        }
                        responseText.setText("");
                        if (this.questNum < preguntas.length-1) {
                            questNum += 1;
                            dispMessage.setText(preguntas[questNum]);//Next question
                        }
                        else{
                            timer.cancel();
                            timer.purge();
                            timer=new Timer();
                            // Go to end screen
                            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                            intent.putExtra("sendData", new int[]{score, time});
                            intent.putExtra("sendName", currName);
                            startActivity(intent);
                            finish();
                        }
                    }

                });
            }
        });
    }

    public void scorePage(View view) {
        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        startActivity(intent);
    }
}