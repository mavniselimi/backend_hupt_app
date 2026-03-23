package com.hupt.hupt_backend.dto;

public class QuestionAskRequestDto {
    private String content;
    private Boolean anonymous;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
}
