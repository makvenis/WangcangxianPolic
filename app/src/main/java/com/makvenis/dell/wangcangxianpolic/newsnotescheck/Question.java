package com.makvenis.dell.wangcangxianpolic.newsnotescheck;

import java.util.List;

/**
 * Created by dell on 2018/4/9.
 */

public class Question {

    String question;
    String title;
    List<Answer> mAnswer;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswer() {
        return mAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(List<Answer> answer) {
        mAnswer = answer;
    }

    public static  class Answer{
        String answerCode;
        String answerMessage;

        public Answer() {
        }

        public Answer(String answerCode, String answerMessage) {
            this.answerCode = answerCode;
            this.answerMessage = answerMessage;
        }

        public String getAnswerCode() {
            return answerCode;
        }

        public void setAnswerCode(String answerCode) {
            this.answerCode = answerCode;
        }

        public String getAnswerMessage() {
            return answerMessage;
        }

        public void setAnswerMessage(String answerMessage) {
            this.answerMessage = answerMessage;
        }
    }

}
