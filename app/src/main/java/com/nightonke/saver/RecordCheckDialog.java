package com.nightonke.saver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

/**
 * Created by 伟平 on 2015/10/31.
 */
public class RecordCheckDialog extends DialogFragment {

    private ViewGroup mSnackbarContainer;
    private RecyclerView recyclerView;
    private List<Record> list;
    private Context mContext;
    private String title;

    public RecordCheckDialog(Context context, List<Record> list, String title) {
        this.list = list;
        this.title = title;
        mContext = context;
    }

    public RecordCheckDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_list, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mSnackbarContainer = (ViewGroup) view.findViewById(R.id.snackbar_container);

        RecordCheckAdapter adapter = new RecordCheckAdapter(context, list);

        recyclerView.setAdapter(adapter);

        builder.setTitle("Title");

        builder.setView(view);
        builder.setPositiveButton("GET!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alert = builder.create();

        TextView title = new TextView(mContext);
        title.setHeight(120);
        title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        title.setTypeface(Utils.typefaceLatoLight);
        title.setText(this.title);
        alert.setCustomTitle(title);

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
                btnPositive.setTypeface(Utils.typefaceLatoLight);
            }
        });

        return alert;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        recyclerView = null;
        mSnackbarContainer = null;
        mContext = null;
        list = null;
        title = null;
    }
}
