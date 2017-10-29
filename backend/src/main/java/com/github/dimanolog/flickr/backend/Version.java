package com.github.dimanolog.flickr.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dimanolog on 29.10.2017.
 */

public class Version {
    @SerializedName("version")
    private Integer mVersion;
    @SerializedName("hard")
    private Boolean mhardUpdate;

    public Integer getVersion() {
        return mVersion;
    }

    public void setVersion(Integer version) {
        mVersion = version;
    }

    public Boolean gethardUpdate() {
        return mhardUpdate;
    }

    public void sethardUpdate(Boolean mhardUpdate) {
        this.mhardUpdate = mhardUpdate;
    }
}
