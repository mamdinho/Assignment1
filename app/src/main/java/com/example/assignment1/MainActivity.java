package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment1.R.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
     Quiz quizQuestions; //calling empty constructor which will initialize questions
     int size; //size of how many questions my Quiz class has no code change needed (if question added or removed in quiz class my code won't break)
     int progressupdater;
     TextView question;
     Button trueButton;
     Button falseButton;
     ProgressBar progress;
     String[] questionKeys; //array to hold question key
     boolean[] questionValue; //array to hold question value
     Integer [] colors;
     List<Integer> colorsList;
     int counter2;
     int correctAnswers;
     int wrongAnswers;
     int progressUpdate;
     int counter1;
     List<Map.Entry<String, Boolean>> shuffledStudentEntries;
     AlertDialog.Builder builder;
     private Menu menu;
     static int languageCode = 1; //to check which language is currently selected
     QuizFragment quizFragment;
     static String yourScore = "", outOf = "";
     StorageManager storageManager;
     Activity activity;
     int numOfTries = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizFragment = new QuizFragment(); //Initializing the Fragment
        quizFragment.setQuizTextView((TextView)findViewById(R.id.quizFragmentText));
        quizFragment.setQuizFragment(findViewById(R.id.linearFragmentid));

        quizQuestions = new Quiz(this); //passing context so we can use getResources() to fetch string
        size = quizQuestions.getlength();
        progressupdater = 100/size;
        questionKeys = new String[size];
        questionValue = new boolean[size];
        Integer [] colorcopy = {ContextCompat.getColor(this, R.color.Red), ContextCompat.getColor(this, R.color.Beige), ContextCompat.getColor(this, R.color.BlanchedAlmond), ContextCompat.getColor(this, R.color.LightCyan), ContextCompat.getColor(this, R.color.Goldenrod), ContextCompat.getColor(this, R.color.BlueViolet), ContextCompat.getColor(this, R.color.CadetBlue)};
        colors = colorcopy;
        colorsList = Arrays.asList(colors);
        Collections.shuffle(colorsList); //shuffling the colors
        colorsList.toArray(colors);

        counter1 = 0;
        question = (TextView)findViewById(R.id.quizFragmentText);
        trueButton = (Button)findViewById(R.id.trueButton);
        falseButton = (Button)findViewById(R.id.falseButton);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        builder = new AlertDialog.Builder(this);

        shuffledStudentEntries = new ArrayList<>(quizQuestions.questions.entrySet());
        Collections.shuffle(shuffledStudentEntries); //shuffling the entries to show questions in diff order

        for(Map.Entry pairEntry : shuffledStudentEntries){
            questionKeys[counter1] = pairEntry.getKey().toString();
            questionValue[counter1] = (Boolean) pairEntry.getValue();
            counter1++;
        }

        counter2 = 0; //second counter to be updated each time a question has been answered
        correctAnswers = 0;
        wrongAnswers = 0;
        progressUpdate = 100/size; //default increment for progress bar 100 means full
        yourScore = getApplicationContext().getResources().getString(R.string.your_score);
        outOf = getApplicationContext().getResources().getString(R.string.out_of);

        if(savedInstanceState != null){
            counter2 = savedInstanceState.getInt("counter");
            correctAnswers = savedInstanceState.getInt("correct");
            wrongAnswers = savedInstanceState.getInt("wrong");
            progressUpdate = savedInstanceState.getInt("progress");
            questionKeys = savedInstanceState.getStringArray("questions");
            questionValue = savedInstanceState.getBooleanArray("answers");
            numOfTries = savedInstanceState.getInt("tries");
            languageCode = savedInstanceState.getInt("language");
            if(languageCode == 2){
                Locale locale = new Locale("ar");
                Locale.setDefault(locale);

                Configuration config = new Configuration();
                config.setLocale(locale);

                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(getApplicationContext(), "Arabic", Toast.LENGTH_SHORT).show();
                languageCode = 2;//app language is Arabic

            }else{
                Locale locale = new Locale("en");
                Locale.setDefault(locale);

                Configuration config = new Configuration();
                config.setLocale(locale);

                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_SHORT).show();
                languageCode = 1;//app language is english
            }
        }
          updateMenuTitles(); //check to update menu
          quizFragment.setQuizText(questionKeys[counter2]);
          quizFragment.setQuizTextColor(colors[counter2]);
          storageManager = new StorageManager();
          activity = this;

        //When true button is clicked check to see if key and value match and update progress bar
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ans = questionValue[counter2];
                if(ans){ //answer is correct
                    Toast.makeText(getApplicationContext() , "Correct answer", Toast.LENGTH_SHORT).show();
                    correctAnswers++; //
                }else{ //answer is wrong
                    Toast.makeText(getApplicationContext() , "Wrong answer", Toast.LENGTH_SHORT).show();
                    wrongAnswers++;
                }
                //update progress if not full
                progress.setProgress(progressUpdate);
                progressUpdate += progressupdater;
                if(counter2 == size-1 && progress.getProgress() < 100){
                    progress.setProgress(100);
                }
                if(progress.getProgress() >= 100){
                    //if progress is full show alert dialog
                    numOfTries++; //increments number of tries
                    storageManager.saveInternal(activity, String.valueOf(correctAnswers)); //add correct results to internal
                    builder.setMessage(yourScore+" " +correctAnswers +" "+outOf+" "+size).setCancelable(true)
                    .setPositiveButton(getApplicationContext().getResources().getString(R.string.repeat), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //ADD CODE TO RESTART APPLICATION
                            resetUi();
                        }
                    })
                    .setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING WHEN USER CLICKS CANCEL
                        }
                    })
                    ;
                    AlertDialog alert = builder.create();
                    alert.setTitle("Result");
                    alert.show();
                }
                if(counter2 < size){
                    if(counter2 != size-1){
                        counter2++;
                    }
                    //change text and color of fragment below
                    quizFragment.setQuizText(questionKeys[counter2]);
                    quizFragment.setQuizTextColor(colors[counter2]);
                }
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ans = questionValue[counter2];
                if(!ans){ //answer is correct
                    Toast.makeText(getApplicationContext() , "Correct answer", Toast.LENGTH_SHORT).show();
                    correctAnswers++; //
                }else{ //answer is wrong
                    Toast.makeText(getApplicationContext() , "Wrong answer", Toast.LENGTH_SHORT).show();
                    wrongAnswers++;
                }
                //update progress if not full
                progress.setProgress(progressUpdate);
                progressUpdate += progressupdater;
                if(counter2 == size-1 && progress.getProgress() < 100){
                    progress.setProgress(100);
                }
                if(progress.getProgress() >= 100){
                    numOfTries++; //increments number of tries
                    storageManager.saveInternal(activity, String.valueOf(correctAnswers)); //add correct results to internal
                    //if progress is full show alert dialog
                    builder.setMessage(yourScore+" " +correctAnswers +" "+outOf+" "+size).setCancelable(true)
                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.repeat), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ADD CODE TO RESTART APPLICATION
                                    resetUi();
                                }
                            })
                    .setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING WHEN USER CLICKS CANCEL
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Result");
                    alert.show();
                }
                if(counter2 < size){
                    if(counter2 != size-1){
                        counter2++;
                    }
                    //change text and color of fragment below
                    quizFragment.setQuizText(questionKeys[counter2]);
                    quizFragment.setQuizTextColor(colors[counter2]);
                }
            }
        });
    }

    //method below resets the Activity fields again
    public void resetUi(){
        quizQuestions.finalize(); //empties the quizQuestions
        counter1 = 0;
        quizQuestions = new Quiz(this); //calling empty constructor which will initialize questions
        shuffledStudentEntries = new ArrayList<>(quizQuestions.questions.entrySet());
        Collections.shuffle(shuffledStudentEntries); //shuffling the entries to show questions in diff order
        colorsList = Arrays.asList(colors);
        Collections.shuffle(colorsList); //shuffling the colors
        colorsList.toArray(colors);

        for(Map.Entry pairEntry : shuffledStudentEntries){
            questionKeys[counter1] = pairEntry.getKey().toString();
            questionValue[counter1] = (Boolean) pairEntry.getValue();
            counter1++;
        }
        progress.setProgress(0);
        counter2 = 0; //second counter to be updated each time a question has been answered
        correctAnswers = 0;
        wrongAnswers = 0;
        progressUpdate = 100/size;


        quizFragment.setQuizText(questionKeys[counter2]);
        quizFragment.setQuizTextColor(colors[counter2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        this.menu = menu;
        updateMenuTitles();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case  R.id.menuId :
                if(item.getTitle().equals("Arabic")){ //If app menu title is arabic
                    Locale locale = new Locale("ar");
                    locale.setDefault(locale);
                    languageCode = 2; //means arabic is chosen so display english
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    finish();
                    startActivity(getIntent());

                }else {
                    Locale locale = new Locale("en");
                    locale.setDefault(locale);
                    languageCode = 1; //means arabic is chosen so display english
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    finish();
                    startActivity(getIntent());
                }
                break;
                case R.id.averageId:
                    storageManager = new StorageManager();
                    String avg = storageManager.loadFromInternal(activity);
                    double result = 0;
                    //If 
                    try{
                        for(int k=0; k<avg.length(); k++){
                            result += Double.parseDouble(String.valueOf(avg.charAt(k)));
                        }

                        result = result/numOfTries;
                        builder.setMessage("Your average result attempt is " +String.valueOf(result)).setCancelable(false)
                                .setPositiveButton(("OK"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        ;
                        AlertDialog alert = builder.create();
                        alert.setTitle("Average Results");
                        alert.show();
                    }catch (Exception e){
                        System.out.println(e.getStackTrace());
                        Toast.makeText(activity, "No average result available", Toast.LENGTH_LONG).show();
                    }

                    break;
            case R.id.resetId:
                numOfTries = 0; //resets number of tries
                storageManager = new StorageManager();
                storageManager.saveInternalNew(activity, ""); //re-write the file with empty
                builder.setMessage("Your results have been reset").setCancelable(false)
                        .setPositiveButton(("OK"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                ;
                AlertDialog alert = builder.create();
                alert.setTitle("Resetting results");
                alert.show();
                break;

        }
        return true;
    }

    private void updateMenuTitles() {
        if (menu != null) {
            MenuItem langMenuItem = menu.findItem(R.id.menuId);
            Button trueButton = (Button)findViewById(R.id.trueButton);
            Button falseButton = (Button)findViewById(R.id.falseButton);

            if (languageCode == 1) {// if app language is 1 then menu to be Arabic
                langMenuItem.setTitle(R.string.arabic);
                trueButton.setText(R.string.istrue);
                falseButton.setText(R.string.isfalse);
            } else {// if app languege is 2 then menu to be english
                langMenuItem.setTitle(R.string.english);
                trueButton.setText(R.string.istrue);
                falseButton.setText(R.string.isfalse);
            }
        }
    }

    @Override
    //Below will save all instances of the app so i don't loose any data when configuration is changed
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("language", languageCode); //saving the language code
        outState.putInt("counter", counter2);
        outState.putInt("correct", correctAnswers);
        outState.putInt("wrong", wrongAnswers);
        outState.putInt("progress", progressUpdate);
        outState.putStringArray("questions", questionKeys);
        outState.putBooleanArray("answers", questionValue);
        outState.putInt("tries", numOfTries);
    }

}