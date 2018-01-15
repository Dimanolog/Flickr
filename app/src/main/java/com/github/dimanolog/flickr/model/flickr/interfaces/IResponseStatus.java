package com.github.dimanolog.flickr.model.flickr.interfaces;

/**
 * Created by Dimanolog on 11.01.2018.
 */

public interface IResponseStatus {
    String getStat();

    void setStat(String pStat);

    Integer getCode();

    void setCode(Integer pCode);

    String getMessage();

    void setMessage(String pMessage);

    boolean isSuccess();
}
