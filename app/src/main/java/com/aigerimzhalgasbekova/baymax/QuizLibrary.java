package com.aigerimzhalgasbekova.baymax;

import java.util.Arrays;

/**
 * Created by aigerimzhalgasbekova on 14/10/2018.
 */

public class QuizLibrary {

    private String mQuestions [] = {
            "Are you interested in cheap products?",
            "Are you interested in bio products?",
            "Are you interested in discounts?"

    };

    private String mChoices [][] = {
            {"YES", "NO"},
            {"YES", "NO"},
            {"YES", "NO"}
    };

    public int size() {
        return Arrays.asList(mQuestions).size();
    }

    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }

    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }

    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }
}
