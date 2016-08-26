package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/25/16.
 */
public class FaqItemResponseModel {
    private Integer id_faq;
    private String question;
    private String answer;

    public Integer getId_faq() {
        return id_faq;
    }

    public void setId_faq(Integer id_faq) {
        this.id_faq = id_faq;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
