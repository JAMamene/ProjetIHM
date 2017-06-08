package ihm.projetihm.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.text.ParseException;

import ihm.projetihm.Database.DBHelper;


public class NewsAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private NewsCursorAdapter cursorAdapter;
    private NewsViewHolder newsViewHolder;
    private Cursor cursor;
    private int position;

    public NewsAsyncTask(Context context, NewsCursorAdapter cursorAdapter, NewsViewHolder newsViewHolder, int position) {
        this.context = context;
        this.cursorAdapter = cursorAdapter;
        this.newsViewHolder = newsViewHolder;
        this.cursor = cursorAdapter.getCursor();
        this.position = position;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            cursorAdapter.getCursor().moveToPosition(position);
            newsViewHolder.setNews(context, DBHelper.getInstance(context).getNews(cursor));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void res) {
        cursorAdapter.setViewHolder(newsViewHolder);
        cursorAdapter.bindView(newsViewHolder.itemView, context, cursor);
    }
}
