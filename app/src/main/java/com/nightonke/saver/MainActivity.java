package com.nightonke.saver;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    private MyGridView myGridView;
    private MyGridViewAdapter myGridViewAdapter;

    private MaterialEditText editView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        Utils.init(mContext);

        myGridView = (MyGridView)findViewById(R.id.gridview);
        myGridViewAdapter = new MyGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        editView = (MaterialEditText)findViewById(R.id.edit_view);
        editView.setTypeface(Utils.typefaceBernhardFashion);
        editView.setText("0");

    }

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (Utils.ClickButtonDelete(position)) {
                editView.setText("0");
            }
            return false;
        }
    };


    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            if (editView.getText().toString().equals("0")) {
                if (Utils.ClickButtonDelete(position)
                        || Utils.ClickButtonCommit(position)
                        || Utils.ClickButtonIsZero(position)) {

                } else {
                    editView.setText(Utils.BUTTONS[position]);
                }
            } else {
                if (Utils.ClickButtonDelete(position)) {
                    editView.setText(editView.getText().toString()
                            .substring(0, editView.getText().toString().length() - 1));
                    if (editView.getText().toString().length() == 0) {
                        editView.setText("0");
                    }
                } else if (Utils.ClickButtonCommit(position)) {
                    Toast.makeText(mContext, "Commit", Toast.LENGTH_SHORT).show();
                    editView.setText("0");
                } else {
                    editView.setText(editView.getText().toString() + Utils.BUTTONS[position]);
                }
            }
            editView.setHelperText(Utils.FLOATINGLABELS[editView.getText().toString().length()]);
        }
    };

    @Override
    public void onBackPressed() {
        editView.setText(editView.getText().toString()
                .substring(0, editView.getText().toString().length() - 1));
        if (editView.getText().toString().equals("")) {
            editView.setText("0");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
