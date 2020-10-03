package com.developer.dk.thebraintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer,textViewQuestion,textViewScore,textViewQuestionResult;
    private Button btnPlay,btn0,btn1,btn2,btn3,btnGo;

    public static final String TAG = "Main";
    private int leftValue = 0,rightValue = 0;

    private CountDownTimer timer;

    private int totalQuestions = 0,correctAnswers = 0;

    private ConstraintLayout constraintLayout;

    public void Go(View view){
        btnGo.animate().translationYBy(-1500f).setDuration(1000);
        constraintLayout.setVisibility(View.VISIBLE);
        constraintLayout.animate().rotation(1440).setDuration(1000);

        startTimer();

        changeValues();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintMainGameLayout);

        textViewTimer = findViewById(R.id.textViewTimer);

        textViewQuestion = findViewById(R.id.textViewQuestion);

        textViewScore = findViewById(R.id.textViewScore);

        textViewQuestionResult = findViewById(R.id.textViewQuestionResult);

        btnGo = findViewById(R.id.btnGo);

        btn0 = findViewById(R.id.button0);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        btnPlay = findViewById(R.id.btnPlay);

        textViewQuestionResult.setVisibility(View.VISIBLE);
    }

    public void enableBtns(boolean btns){
        btn0.setEnabled(btns);
        btn1.setEnabled(btns);
        btn2.setEnabled(btns);
        btn3.setEnabled(btns);
    }

    public void btnPressed(View view){

        Button btn = (Button) view;

        int correctAnswer = leftValue + rightValue;

        if (btn.getText().toString().equals("" + correctAnswer)) {
            textViewQuestionResult.setText("Correct!");
            correctAnswers = correctAnswers + 1;
        }
        else {
            textViewQuestionResult.setText("Wrong!");
        }

        textViewScore.setText("" + correctAnswers + "/" + totalQuestions);

        changeValues();
    }

    public void startAgain(View view){

        textViewQuestionResult.setVisibility(View.INVISIBLE);
        btnPlay.setVisibility(View.INVISIBLE);
        textViewScore.setText("0/0");
        totalQuestions = 0;
        correctAnswers = 0;
        timer.start();
        enableBtns(true);
        changeValues();
    }

    private void changeValues(){

        totalQuestions = totalQuestions + 1;

        Random randNumber = new Random();

        leftValue = randNumber.nextInt(20 + 1);
        rightValue = randNumber.nextInt(20 + 1);

        textViewQuestion.setText(leftValue + " + "+ rightValue);

        ArrayList<Integer> answers = new ArrayList<>();

        int correctAnswerAtPosition = randNumber.nextInt(4);

        for (int i = 0; i < 4; i++){

            if (i == correctAnswerAtPosition){
                answers.add(leftValue + rightValue);
            }
            else {
                int wrongAnswer = randNumber.nextInt(40 + 1);

                while ((leftValue + rightValue) == wrongAnswer){
                    answers.add(randNumber.nextInt(40 + 1));
                    break;
                }
                answers.add(wrongAnswer);
            }
        }

        btn0.setText("" + answers.get(0));
        btn1.setText("" + answers.get(1));
        btn2.setText("" + answers.get(2));
        btn3.setText("" + answers.get(3));

    }

    private void startTimer(){
        timer = new CountDownTimer(30000 + 300,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timerString = String.format(Locale.getDefault(),"%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                textViewTimer.setText(timerString);
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("00:00");
                textViewQuestionResult.setText("Game Over!");
                textViewQuestionResult.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.VISIBLE);

                enableBtns(false);

                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gameover);
                mediaPlayer.start();
            }
        };

        timer.start();
    }
}