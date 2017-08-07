package com.github.skewart.sample.wordymenudialog.wordymenudialog;

import android.app.Activity;
import android.util.Log;

import com.github.skewart.wordymenudialog.WordyMenuDialog;

public class MainActivity extends Activity {

    @Override
    public void onResume() {
        super.onResume();

        WordyMenuDialog.builder(this)
                       .title("Pet Picker")
                       .message("Select your favorite kind of pet from the options below.")
                       .addMenuOption("Cats")
                       .addMenuOption("Dogs")
                       .addMenuOption("Hamsters")
                       .onOptionSelectedListener(new WordyMenuDialog.OnOptionSelectedListener() {
                           @Override
                           public void onItemClick(int position) {
                               Log.d("MainActivity", "You selected " + position);
                           }
                       })
                       .onCancelListener(new WordyMenuDialog.OnCancelListener() {
                           @Override
                           public void onCancel() {
                               Log.d("MainActivity", "You canceled");
                           }
                       })
                       .build()
                       .show();
    }

}
