package com.github.dimanolog.flickr.db.annotations;

/**
 * Created by Dimanolog on 31.12.2017.
 */

public @interface ForeignKey {
    String name();

    String table();

    String column();
}
