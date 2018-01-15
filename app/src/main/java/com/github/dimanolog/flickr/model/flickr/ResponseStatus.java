package com.github.dimanolog.flickr.model.flickr;

import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;
import com.google.gson.annotations.SerializedName;

public class ResponseStatus implements IResponseStatus {
    private static final String OK = "ok";

    @SerializedName("stat")
    private String mStat;
    @SerializedName("code")
    private Integer mCode;
    @SerializedName("message")
    private String message;

    @Override
    public String getStat() {
        return mStat;
    }

    @Override
    public void setStat(String pStat) {
        mStat = pStat;
    }

    @Override
    public Integer getCode() {
        return mCode;
    }

    @Override
    public void setCode(Integer pCode) {
        mCode = pCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String pMessage) {
        message = pMessage;
    }

    public boolean isSuccess() {
       return OK.equals(mStat);
    }
}

