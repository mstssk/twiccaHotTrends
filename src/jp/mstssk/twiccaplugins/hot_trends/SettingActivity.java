package jp.mstssk.twiccaplugins.hot_trends;

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingActivity extends Activity implements OnClickListener {

    private Spinner spinner;
    private SharedPreferences pref;

    private String[] list_lang;
    private String key_lang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        spinner = (Spinner) findViewById(R.id.spinner_lang);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        list_lang = getResources().getStringArray(R.array.list_lang);
        key_lang = getString(R.string.key_lang);
        String pref_lang = pref.getString(getString(R.string.key_lang), 
                                                getString(R.string.default_lang));
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int selected = 0;
        for (int i = 0; i < list_lang.length; i++) {
            adapter.add((new Locale(list_lang[i])).getDisplayName());
            if (list_lang[i].equals(pref_lang)) {
                selected = i;
            }
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(selected);

        findViewById(R.id.button_ok).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_ok) {
            int selected = spinner.getSelectedItemPosition();
            Editor editor = pref.edit();
            editor.putString(key_lang, list_lang[selected]);
            editor.commit();
        }
        finish();
    }

}
