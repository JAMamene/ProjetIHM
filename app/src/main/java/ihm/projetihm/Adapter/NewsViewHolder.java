package ihm.projetihm.Adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ihm.projetihm.Fragment.NewsFullFragment;
import ihm.projetihm.Model.News;
import ihm.projetihm.R;
import ihm.projetihm.Util;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView content;
    public TextView author;
    public TextView date;
    public ImageView image;
    private News news;

    public News getNews() {
        return news;
    }

    public NewsViewHolder(final View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        content = (TextView) itemView.findViewById(R.id.content);
        author = (TextView) itemView.findViewById(R.id.author);
        date = (TextView) itemView.findViewById(R.id.date);
        image = (ImageView) itemView.findViewById(R.id.picture);
    }

    public void setNews(final Context context, final News n) {
        this.news = n;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("News",n);
                FragmentTransaction fragmentTransaction = ((Activity) context).getFragmentManager().beginTransaction();
                NewsFullFragment fullNewsFragment = new NewsFullFragment();
                fullNewsFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
                fragmentTransaction.add(R.id.content_frame, fullNewsFragment);
                fragmentTransaction.addToBackStack(Util.FULLNEWSFRAGMENT);
                fragmentTransaction.commit();
            }
        });
    }
}