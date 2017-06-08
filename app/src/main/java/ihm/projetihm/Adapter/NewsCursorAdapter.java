package ihm.projetihm.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ihm.projetihm.Model.Category;
import ihm.projetihm.Model.News;
import ihm.projetihm.Model.Source;
import ihm.projetihm.R;


public class NewsCursorAdapter extends CursorAdapter {

    private NewsViewHolder newsViewHolder;

    public void setViewHolder(NewsViewHolder newsViewHolder) {
        this.newsViewHolder = newsViewHolder;
    }

    public NewsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        News n = newsViewHolder.getNews();
        TextView title = newsViewHolder.title;
        title.setText(n.getTitle());
        title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, getCorrespondingDrawable(n.getSource())), null);
        TextView author = newsViewHolder.author;
        author.setText(n.getAuthor());
        if (n.isFollowing()) {
            author.setTypeface(null, Typeface.BOLD);
            author.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            author.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_small_star), null);
        }
        TextView content = newsViewHolder.content;
        content.setText(shortenContent(n.getContent()));
        ImageView imageView = newsViewHolder.image;
        Picasso.with(context).load(n.getImage()).into(imageView);
        TextView dateField = newsViewHolder.date;
        if (n.getSource() == Source.EVENEMENT) {
            dateField.setText(DateUtils.getRelativeTimeSpanString(n.getDateEvent(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
            dateField.setTypeface(null, Typeface.BOLD);
            if (n.isRegistered()) {
                dateField.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
        } else {
            dateField.setText(DateUtils.getRelativeTimeSpanString(n.getDate(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        }
    }

    private String shortenContent(String content) {
        return (content.length() > 100)
                ? (content.substring(0, 100) + "...")
                : content;
    }

    private int getCorrespondingDrawable(Source s) {
        switch (s) {
            case TWITTER:
                return R.drawable.ic_twitter;
            case MAGASIN:
                return R.drawable.ic_shop;
            case EVENEMENT:
                return R.drawable.ic_event;
            default:
                return R.drawable.ic_shop;
        }
    }
}
