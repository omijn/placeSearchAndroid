package com.example.omijn.placeandentertainmentsearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

public class WebDataAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        // build URL

    }

    @Override
    protected String doInBackground(String... strings) {
        // fetch data
        return "abc, def, ghi, jkl, mno, pqr, stu, vwx, yz";
    }

}
