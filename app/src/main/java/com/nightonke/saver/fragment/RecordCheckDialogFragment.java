package com.nightonke.saver.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.RecordCheckDialogRecyclerViewAdapter;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.util.CoCoinUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 伟平 on 2015/10/31.
 */
public class RecordCheckDialogFragment extends DialogFragment implements RecordCheckDialogRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<CoCoinRecord> list;
    private Context mContext;
    private String title;

    public RecordCheckDialogFragment(Context context, List<CoCoinRecord> list, String title) {
        this.list = list;
        this.title = title;
        mContext = context;
    }

    public RecordCheckDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_list, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        RecordCheckDialogRecyclerViewAdapter adapter = new RecordCheckDialogRecyclerViewAdapter(context, list, this);

        recyclerView.setAdapter(adapter);

        builder.setTitle("Title");

        builder.setView(view);
        builder.setPositiveButton(mContext.getResources().getString(R.string.get),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alert = builder.create();

        TextView title = new TextView(mContext);
        title.setHeight(120);
        title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        title.setText(this.title);
        alert.setCustomTitle(title);

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
                btnPositive.setTypeface(CoCoinUtil.typefaceLatoLight);
            }
        });

        return alert;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        recyclerView = null;
        mContext = null;
        list = null;
        title = null;
    }

    private MaterialDialog dialog;
    private View dialogView;
    @Override
    public void onItemClick(View view, int position) {
        String subTitle;
        double spend = list.get(position).getMoney();
        int tagId = list.get(position).getTag();
        if ("zh".equals(CoCoinUtil.GetLanguage())) {
            subTitle = CoCoinUtil.GetSpendString((int)spend) +
                    "于" + CoCoinUtil.GetTagName(tagId);
        } else {
            subTitle = "Spend " + (int)spend +
                    "in " + CoCoinUtil.GetTagName(tagId);
        }
        dialog = new MaterialDialog.Builder(mContext)
                .icon(CoCoinUtil.GetTagIconDrawable(list.get(position).getTag()))
                .limitIconToDefaultSize()
                .title(subTitle)
                .customView(R.layout.dialog_a_record, true)
                .positiveText(R.string.get)
                .show();
        dialogView = dialog.getCustomView();
        TextView remark = (TextView)dialogView.findViewById(R.id.remark);
        TextView date = (TextView)dialogView.findViewById(R.id.date);
        remark.setText(list.get(position).getRemark());
        date.setText(CoCoinUtil.GetCalendarStringRecordCheckDialog(mContext, list.get(position).getCalendar()));
    }
}
