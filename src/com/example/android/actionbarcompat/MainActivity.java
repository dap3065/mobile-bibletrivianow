/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private boolean mAlternateTitle = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText edittext = (EditText) findViewById(R.id.edit_message);
        edittext.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                  Toast.makeText(MainActivity.this, edittext.getText().toString(), Toast.LENGTH_SHORT).show();
                  return true;
                }
                return false;
            }
        });

        findViewById(R.id.toggle_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mEdit   = (EditText)findViewById(R.id.edit_message);
                setTitle(mEdit.getText().toString());
                Log.i("help", mEdit.getText().toString());
                /*
                if (mAlternateTitle) {
                    setTitle(mEdit.getText());
                } else {
                    setTitle(R.string.alternate_title);
                }
                */
                mAlternateTitle = !mAlternateTitle;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                        }, 1000);
                break;

            case R.id.menu_search:
                Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public String getWebPage(String adresse) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();

        InputStream inputStream = null;

        String response = null;

        try {

            URI uri = new URI(adresse);
            httpGet.setURI(uri);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statutCode = httpResponse.getStatusLine().getStatusCode();
            int length = (int) httpResponse.getEntity().getContentLength();

            Log.v("help", "HTTP GET: " + adresse);
            Log.v("help", "HTTP StatutCode: " + statutCode);
            Log.v("help", "HTTP Lenght: " + length + " bytes");

            inputStream = httpResponse.getEntity().getContent();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            int inChar;
            StringBuffer stringBuffer = new StringBuffer();

            while ((inChar = reader.read()) != -1) {
                stringBuffer.append((char) inChar);
            }

            response = stringBuffer.toString();

        } catch (ClientProtocolException e) {
            Log.e("help", "HttpActivity.getPage() ClientProtocolException error", e);
        } catch (IOException e) {
            Log.e("help", "HttpActivity.getPage() IOException error", e);
        } catch (URISyntaxException e) {
            Log.e("help", "HttpActivity.getPage() URISyntaxException error", e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

            } catch (IOException e) {
                Log.e("help", "HttpActivity.getPage() IOException error lors de la fermeture des flux", e);
            }
        }

        return response;
    }
}
