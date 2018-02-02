package com.bignerdranch.android.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private int mScores = 0;
    private int mCount = 0;


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int[] mAnswered = {0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d(TAG, "onCreate(Bundle) called");

        if(savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        /* Question */
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex=(mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        /* True Button */
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
                checkAnswer(true);
                mAnswered[mCurrentIndex] = 1;
            }
        });

        /* False Button */
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
                checkAnswer(false);
                mAnswered[mCurrentIndex] = 1;
            }
        });


        /* Next Button */
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                if (mAnswered[mCurrentIndex] == 0) {
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                    updateQuestion();
                }else {
                    for(int j = 0; j < mQuestionBank.length; j++){
                        mCount += mAnswered[j];
                    }
                    if(mCount == mQuestionBank.length){
                        Toast.makeText(getApplicationContext(),"Your score percentage is "+String.valueOf(Math.round(mScores/(float)mQuestionBank.length*100)),Toast.LENGTH_SHORT).show();
                    }

                    mCount = 0;
                    updateQuestion();
                }
            }
        });


        /* Previous Button */
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex == 0)
                {
                    mCurrentIndex= mQuestionBank.length - 1;
                }
                else
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;

                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    /* Question Update */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    /* User Input */
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            mQuestionBank[mCurrentIndex].mAnsweredCorrectly = true;
            messageResId = R.string.correct_toast;
            mScores++;
        } else {
            mQuestionBank[mCurrentIndex].mAnsweredCorrectly = false;
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
