package ihm.projetihm.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private NewsCursorAdapter cursorAdapter;
    private Context context;

    public NewsRecyclerAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursorAdapter = new NewsCursorAdapter(context, cursor);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        new NewsAsyncTask(context, cursorAdapter, holder, position).execute();
    }


    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
