package com.github.dimanolog.flickr.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dimanolog on 10.01.2018.
 */

public class ResponseStatus implements IResponseStatus {
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
}

