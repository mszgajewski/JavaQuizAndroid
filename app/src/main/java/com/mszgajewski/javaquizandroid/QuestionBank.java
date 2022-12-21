package com.mszgajewski.javaquizandroid;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    private static List<QuestionsList> javaQuestions(){

        final List<QuestionsList> questionsLists = new ArrayList<>();
        final QuestionsList question1 = new QuestionsList("questionj1","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question2 = new QuestionsList("questionj2","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question3 = new QuestionsList("questionj3","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question4 = new QuestionsList("questionj4","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question5 = new QuestionsList("questionj5","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question6 = new QuestionsList("questionj6","opt1", "opt2", "opt3","opt4",4);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);
        questionsLists.add(question5);
        questionsLists.add(question6);

        return questionsLists;
    }

    private static List<QuestionsList> htmlQuestions(){

        final List<QuestionsList> questionsLists = new ArrayList<>();
        final QuestionsList question1 = new QuestionsList("questionh1","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question2 = new QuestionsList("questionh2","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question3 = new QuestionsList("questionh3","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question4 = new QuestionsList("questionh4","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question5 = new QuestionsList("questionh5","opt1", "opt2", "opt3","opt4",4);
        final QuestionsList question6 = new QuestionsList("questionh6","opt1", "opt2", "opt3","opt4",4);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);
        questionsLists.add(question5);
        questionsLists.add(question6);

        return questionsLists;
    }

    private static List<QuestionsList> phpQuestions(){

        final List<QuestionsList> questionsLists = new ArrayList<>();
        final QuestionsList question1 = new QuestionsList("questionp1","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question2 = new QuestionsList("questionp2","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question3 = new QuestionsList("questionp3","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question4 = new QuestionsList("questionp4","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question5 = new QuestionsList("questionp5","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question6 = new QuestionsList("questionp6","opt1", "opt2", "opt3","opt4",1);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);
        questionsLists.add(question5);
        questionsLists.add(question6);

        return questionsLists;
    }

    private static List<QuestionsList> androidQuestions(){

        final List<QuestionsList> questionsLists = new ArrayList<>();
        final QuestionsList question1 = new QuestionsList("questiona1","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question2 = new QuestionsList("questiona2","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question3 = new QuestionsList("questiona3","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question4 = new QuestionsList("questiona4","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question5 = new QuestionsList("questiona5","opt1", "opt2", "opt3","opt4",1);
        final QuestionsList question6 = new QuestionsList("questiona6","opt1", "opt2", "opt3","opt4",1);

        questionsLists.add(question1);
        questionsLists.add(question2);
        questionsLists.add(question3);
        questionsLists.add(question4);
        questionsLists.add(question5);
        questionsLists.add(question6);

        return questionsLists;
    }

    public static List<QuestionsList> getQuestions(String selectedTopic){
        switch (selectedTopic){
            case "javascript":
                return javaQuestions();
            case "php":
                return phpQuestions();
            case "android":
                return androidQuestions();
            default:
                return htmlQuestions();
        }
    }
}
