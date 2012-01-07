package jp.mstssk.twiccaplugins.hot_trends;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class HotTrendsActivity extends Activity implements OnClickListener,
        OnLongClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

    }

    @Override
    public void onStart() {
        super.onStart();

        String pref_lang = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.key_lang),
                        getString(R.string.default_lang));
        String[] list_lang = getResources().getStringArray(R.array.list_lang);
        int selected = 0;
        for (int i = 0; i < list_lang.length; i++) {
            if (pref_lang.equals(list_lang[i])) {
                selected = i;
                break;
            }
        }

        HotTrendsTask task = new HotTrendsTask(this,
                (ViewGroup) findViewById(R.id.layoutarea), this, this);
        task.execute(getResources().getStringArray(R.array.list_url)[selected]);
    }

    @Override
    public void onClick(View v) {
        String word = (String) ((TextView) v).getText();
        Intent intent = getIntent();
        intent.putExtra(Intent.EXTRA_TEXT, word);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onLongClick(View v) {
        String word = (String) ((TextView) v).getText();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, word);
        startActivity(intent);
        finish();
        return true;
    }

}