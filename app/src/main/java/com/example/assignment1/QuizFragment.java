package com.example.assignment1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuizFragment extends Fragment {
    TextView quizTextView;
    View quizFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quizfragment,container,false);
    }

    //initializes the textview
    public void setQuizTextView(TextView t){
        quizTextView = t;
    }

    public void setQuizFragment(View fr){
        quizFragment = fr;
    }

    //for changing the quiz text
    public void setQuizText(String s){
        quizTextView.setText(s);
    }

    //for changing the quiz fragment color
    public void setQuizTextColor(int c){
        quizFragment.setBackgroundColor(c);

    }

}
