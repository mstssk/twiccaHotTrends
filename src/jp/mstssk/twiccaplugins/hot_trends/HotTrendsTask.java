package jp.mstssk.twiccaplugins.hot_trends;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HotTrendsTask extends AsyncTask<String, Integer, LinearLayout> {

	private Activity activity;
	private ViewGroup resultArea;
	private OnClickListener clickListener;
	private OnLongClickListener longClickListener;

	public HotTrendsTask(Activity act, ViewGroup view,
			OnClickListener listener, OnLongClickListener listener2) {
		activity = act;
		resultArea = view;
		clickListener = listener;
		longClickListener = listener2;
	}

	@Override
	protected LinearLayout doInBackground(String... params) {

		String url = (String) params[0];
		String source = null;

		try {
			source = new String(getByteArrayFromURL(url), "UTF-8");
		} catch (Exception e) {
			// Log.e("mstssk", e.getLocalizedMessage(), e);
		}

		if (source == null || source.length() <= 0) {
			return null;
		}

		Pattern pattern = Pattern.compile(
				"<a (?:\\s|.)*?>\\s*((?!\\s)[^<]+?)\\s*</a>",
				Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(source);
		source = null;

		ArrayList<String> list = new ArrayList<String>();
		int length = 20;
		while (matcher.find() && length > 0) {
			list.add(matcher.group(1));
			length--;
		}
		matcher = null;

		length = list.size();
		LinearLayout layout = null;

		if (length > 0) {

			layout = new LinearLayout(activity);
			layout.setOrientation(LinearLayout.VERTICAL);
			int padding = activity.getResources().getDimensionPixelSize(
					R.dimen.dialog_padding);
			layout.setPadding(padding, padding, padding, padding);

			LayoutInflater inflater = LayoutInflater.from(activity);
			for (int i = 0; i < length; i++) {
				Button button = (Button) inflater
						.inflate(R.layout.button, null);
				button.setOnClickListener(clickListener);
				button.setOnLongClickListener(longClickListener);
				button.setText(list.remove(0));
				layout.addView(button);
			}

			list.clear();
			list = null;
		}

		return layout;
	}

	@Override
	protected void onPostExecute(LinearLayout result) {
		if (result != null) {
			resultArea.removeAllViews();
			resultArea.addView(result);
		} else {
			Toast.makeText(activity, R.string.text_toast_error,
					Toast.LENGTH_SHORT).show();
			activity.finish();
		}
	}

	private byte[] getByteArrayFromURL(String strUrl) {
		byte[] byteArray = new byte[1024];
		byte[] result = null;
		HttpURLConnection con = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		int size = 0;
		try {
			URL url = new URL(strUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			in = con.getInputStream();

			out = new ByteArrayOutputStream();
			while ((size = in.read(byteArray)) != -1) {
				out.write(byteArray, 0, size);
			}
			result = out.toByteArray();
		} catch (Exception e) {
			// Log.e("mstssk", e.getLocalizedMessage(), e);
		} finally {
			try {
				if (con != null)
					con.disconnect();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
				// Log.e("mstssk", e.getLocalizedMessage(), e);
			}
		}
		return result;
	}

}
