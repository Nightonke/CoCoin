package com.nightonke.saver.ui;

import android.os.AsyncTask;

import com.github.johnpersano.supertoasts.SuperActivityToast;

/**
 * Created by 伟平 on 2015/10/30.
 */
public class DummyOperation extends AsyncTask<Void, Integer, Void> {

    SuperActivityToast mSuperActivityToast;

    public DummyOperation(SuperActivityToast superActivityToast) {
        this.mSuperActivityToast = superActivityToast;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        for(int i = 0; i < 11 ; i++) {

            try {

                Thread.sleep(70);

                onProgressUpdate(i * 10);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        mSuperActivityToast.dismiss();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mSuperActivityToast.setProgress(progress[0]);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        SuperActivityToast.cancelAllSuperActivityToasts();
    }
}