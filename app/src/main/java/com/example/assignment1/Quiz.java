package com.example.assignment1;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class Quiz {
    Map<String, Boolean> questions = new HashMap<>();

    public Quiz(Context c) {
        //Initializing my questions variable with key "questions" and values"true/false" answers
        questions.put(c.getResources().getString(R.string.question1), false);
        questions.put(c.getResources().getString(R.string.question2), false);
        questions.put(c.getResources().getString(R.string.question3), true);
        questions.put(c.getResources().getString(R.string.question4), true);
        questions.put(c.getResources().getString(R.string.question5), false);
        questions.put(c.getResources().getString(R.string.question6), true);
        questions.put(c.getResources().getString(R.string.question7), false);
    }

    //method below will be called to reset/empty the collection
    public void finalize(){
        questions.clear(); //clear out everything
    }

    //method below returns the size of the hashmap
    public int getlength(){
        return questions.size();
    }
}
