package com.github.skewart.wordymenudialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class WordyMenuDialog {

    public interface OnOptionSelectedListener {
        void onItemClick(int position);
    }

    public interface OnCancelListener {
        void onCancel();
    }

    private Context             context;
    private final View          layout;
    private final AlertDialog dialog;

    public WordyMenuDialog(Builder builder) {
        context = builder.context;
        layout = LayoutInflater.from(context).inflate(R.layout.wordy_menu_dialog, null);
        dialog = new AlertDialog.Builder(context).setView(layout).create();

        setTitle(builder.title);
        setMessage(builder.message);
        setMenuOptions(builder.options, builder.onOptionSelectedListener);
        configureCancel(builder.onCancelListener);
    }

    public void show() {
        dialog.show();
    }

    // ===== Private helpers =======================================================================

    private void setTitle(String title) {
        if (isNullOrEmpty(title)) {
            return;
        }

        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);
        dialogTitle.setVisibility(VISIBLE);
    }

    private void setMessage(String msg) {
        if (isNullOrEmpty(msg)) {
            return;
        }

        TextView dialogMessage = (TextView) layout.findViewById(R.id.dialog_message);
        dialogMessage.setText(msg);
        dialogMessage.setVisibility(VISIBLE);
    }

    private void setMenuOptions(ArrayList<String> options, final OnOptionSelectedListener listener) {
        if (options.isEmpty()) {
            return;
        }

        ListView optionsList = (ListView) layout.findViewById(R.id.dialog_options);

        String[] optionsArray = options.toArray(new String[options.size()]);
        optionsList.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, optionsArray));

        optionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View item, int position, long id) {
                listener.onItemClick(position);
                dialog.dismiss();
            }
        });

        optionsList.setVisibility(VISIBLE);
    }

    private void configureCancel(final OnCancelListener listener) {
        dialog.setCanceledOnTouchOutside(true);
        if (listener == null) {
            return;
        }

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onCancel();
            }
        });
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public AlertDialog getAlertDialog() {
        return dialog;
    }

    // ====== Builder ==============================================================================

    public static Builder builder(Context ctx) {
        return new Builder(ctx);
    }

    public static class Builder {

        private Context                     context;
        private String                      title;
        private String                      message;
        private ArrayList<String>           options = new ArrayList<>();
        private OnOptionSelectedListener    onOptionSelectedListener;
        private OnCancelListener            onCancelListener;

        public Builder(Context ctx) {
            context = ctx;
        }

        public Builder title(String t) {
            title = t;
            return this;
        }

        public Builder title(@StringRes int id) {
            title = context.getString(id);
            return this;
        }

        public Builder message(String t) {
            message = t;
            return this;
        }

        public Builder message(@StringRes int id) {
            message = context.getString(id);
            return this;
        }

        public Builder addMenuOption(String option) {
            options.add(option);
            return this;
        }

        public Builder addMenuOption(@StringRes int id) {
            options.add(context.getString(id));
            return this;
        }

        public Builder onOptionSelectedListener(OnOptionSelectedListener listener) {
            onOptionSelectedListener = listener;
            return this;
        }

        public Builder onCancelListener(OnCancelListener listener) {
            onCancelListener = listener;
            return this;
        }

        public WordyMenuDialog build() {
            return new WordyMenuDialog(this);
        }

    }

}
