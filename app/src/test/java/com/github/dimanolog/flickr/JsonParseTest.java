package com.github.dimanolog.flickr;

import android.util.Log;

import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.http.interfaces.IHttpClient;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.parsers.photo.PhotoParserFactory;
import com.github.dimanolog.flickr.util.IOUtils;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.robolectric.internal.bytecode.RobolectricInternals.getClassLoader;

/**
 * Created by Dimanolog on 21.10.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Constants.SDK_VERSION)
public class JsonParseTest {

    public static final String TAG = JsonParseTest.class.getSimpleName();

    public static final String URL = " https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=643167806a930298e6ade5ca0f7320cd&extras=date_upload%2C+url_s&" +
            "format=json&nojsoncallback=1&api_sig=00b1cc43ecb5d461efb71ed1a103ff52";

    public static final String PHOTO_JSON_OBECT = "{ \"id\": \"23986623688\", " +
            "\"owner\": \"141036339@N04\"," +
            " \"secret\": \"f404800c24\", " +
            "\"server\": \"4455\", \"farm\": 5," +
            " \"title\": \"Predator\", " +
            "\"ispublic\": 1, " +
            "\"isfriend\": 0," +
            " \"isfamily\": 0, " +
            "\"dateupload\": \"1508618791\", " +
            "\"url_s\": \"https:\\/\\/farm5.staticflickr.com\\/4455\\/23986623688_f404800c24_m.jpg\"," +
            " \"height_s\": \"240\"," +
            " \"width_s\": \"135\" }";

    public static final Long EXPECTED_ID = 23986623688L;
    public static final String EXPECTED_TITLE = "Predator";
    public static final String EXPECTED_PHOTO_URL = "https://farm5.staticflickr.com/4455/23986623688_f404800c24_m.jpg";
    public static final String EXPECTED_OWNER = "141036339@N04";
    public static final Date EXPECTED_UPLOAD_DATE = new Date(1508618791L);


    @Mock
    private IHttpClient mHttpClient;
    @Captor
    private ArgumentCaptor<HttpClient.ResponseListener> mResponseListenerArgumentCaptor;
    private HttpClient.ResponseListener mResponseListener;
    private IPhoto mPhoto;
    private List<IPhoto> mPhotoList;
    private InputStream mMockObjectInputStream;
    private InputStream mArrayInputStream;

    @Before
    public void setUp() {
        mHttpClient = mock(IHttpClient.class);
        mResponseListener = mock(HttpClient.ResponseListener.class);
        mMockObjectInputStream = new ByteArrayInputStream(PHOTO_JSON_OBECT.getBytes());
        mArrayInputStream = getClassLoader().getResourceAsStream("JSONArray.json");
    }

    @Test
    public void parseJsonUserPhoto() {
        HttpClient.ResponseListener listener = new HttpClient.ResponseListener() {
            @Override
            public void onResponse(InputStream inputStream) throws IOException, JSONException {
                String jsonString = IOUtils.toString(inputStream);
                mPhoto = new PhotoParserFactory()
                        .getJsonParser()
                        .parseObject(jsonString);

            }
        };

        try {
            listener.onResponse(mMockObjectInputStream);
        } catch (IOException e) {
            Log.e(TAG, "Inputstream error", e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "JSON object error", e);
            e.printStackTrace();
        }
        checkValues();
    }

    @Test
    public void parseJsonPhotoArray() {
        HttpClient.ResponseListener listener = new HttpClient.ResponseListener() {
            @Override
            public void onResponse(InputStream inputStream) throws IOException, JSONException {
                String jsonString = IOUtils.toString(inputStream);
                mPhotoList = new PhotoParserFactory()
                        .getJsonParser()
                        .parseArray(jsonString);
            }
        };

        try {
            listener.onResponse(mArrayInputStream);
        } catch (IOException e) {
            Log.e(TAG, "Inputstream error", e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "JSONArray parse error", e);
            e.printStackTrace();
        }
        checkListValues();

    }

    @Test
    public void parseGsonUserPhoto() {
        HttpClient.ResponseListener listener = new HttpClient.ResponseListener() {
            @Override
            public void onResponse(InputStream inputStream) throws IOException, JSONException {
                String jsonString = IOUtils.toString(inputStream);
                mPhoto = new PhotoParserFactory()
                        .getGsonParser()
                        .parseObject(jsonString);
            }
        };

        try {
            listener.onResponse(mMockObjectInputStream);
        } catch (IOException e) {
            Log.e(TAG, "Inputstream error", e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "GSON parse error", e);
            e.printStackTrace();
        }
        checkValues();
    }

    @Test
    public void parseGsonPhotoArray() {
        HttpClient.ResponseListener listener = new HttpClient.ResponseListener() {
            @Override
            public void onResponse(InputStream inputStream) throws IOException, JSONException {
                String jsonString = IOUtils.toString(inputStream);
                mPhotoList = new PhotoParserFactory()
                        .getGsonParser()
                        .parseArray(jsonString);
            }
        };

        try {
            listener.onResponse(mArrayInputStream);
        } catch (IOException e) {
            Log.e(TAG, "Inputstream error", e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "JSONArray parse error", e);
            e.printStackTrace();
        }
        checkListValues();

    }


    private void checkValues() {
        assertNotNull(mPhoto);
        assertEquals(EXPECTED_ID, mPhoto.getId());
        assertEquals(EXPECTED_OWNER, mPhoto.getOwner());
        assertEquals(EXPECTED_TITLE, mPhoto.getCaption());
        assertEquals(EXPECTED_PHOTO_URL, mPhoto.getUrl());
        assertEquals(EXPECTED_UPLOAD_DATE, mPhoto.getUploadDate());
    }

    private void checkListValues() {
        assertNotNull(mPhotoList);
        assertFalse(mPhotoList.isEmpty());
        for (IPhoto photo : mPhotoList) {
            assertNotNull(photo);
        }
    }

    @After
    public void clear() {
        mPhoto = null;
        mPhotoList = null;
    }
}
