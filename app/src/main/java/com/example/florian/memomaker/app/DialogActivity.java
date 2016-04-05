package com.example.florian.memomaker.app;

/**
 * Created by Sebastian on 19.03.2016.
 */


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DialogActivity extends Activity implements OnClickListener {

    DBManager dbManager;

    Button ok_btn , cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_alert_dialog);

        dbManager = new DBManager(this);
        ok_btn = (Button) findViewById(R.id.ok_btn_id);
        cancel_btn = (Button) findViewById(R.id.cancel_btn_id);

        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        setFinishOnTouchOutside(false);

    }

    @Override
    public void onBackPressed() {
        // prevent "back" from leaving this activity }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ok_btn_id:
                checkDeleteDataFromDB(true);
                this.finish();
                break;

            case R.id.cancel_btn_id:
                checkDeleteDataFromDB(false);
                this.finish();
                break;
        }
    }


    public void checkDeleteDataFromDB(boolean b) {
        if (b) {
            if (PlaceholderFragment.getDelDatasetID() != -1) {
                dbManager.deleteDatasetFromDB(PlaceholderFragment.getDelDatasetID());
            }
        }
    }

}