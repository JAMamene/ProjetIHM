package ihm.projetihm.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ihm.projetihm.Calendar.CalendarHandler;
import ihm.projetihm.Database.DBHelper;
import ihm.projetihm.Database.QueryBuilder;
import ihm.projetihm.MainActivity;
import ihm.projetihm.Model.Category;
import ihm.projetihm.Model.News;
import ihm.projetihm.Model.Source;
import ihm.projetihm.R;
import ihm.projetihm.Util;


public class NewsFullFragment extends Fragment {
    private View root;

    public NewsFullFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context context = getActivity();
        RelativeLayout layout = (RelativeLayout) root.findViewById(R.id.fullnews_root);
        final TextView content = (TextView) layout.findViewById(R.id.content);
        TextView title = (TextView) layout.findViewById(R.id.title);
        TextView categories = (TextView) layout.findViewById(R.id.categories);
        ImageView image = (ImageView) layout.findViewById(R.id.full_picture);
        TextView datePosted = (TextView) layout.findViewById(R.id.date_posted);
        ImageView placeholder = (ImageView) layout.findViewById(R.id.return_placeholder);
        Button cancelButton = (Button) layout.findViewById(R.id.close_fullnews);
        final Button favButton = (Button) layout.findViewById(R.id.fav_button);
        final Button registerButton = (Button) layout.findViewById(R.id.register_button);
        final Bundle bundle = this.getArguments();
        final News n = bundle.getParcelable("News");
        placeholder.setOnClickListener(new CloseListener());
        cancelButton.setOnClickListener(new CloseListener());
        content.setText(n.getContent());
        title.setText(n.getTitle());
        datePosted.setText(getString(R.string.date_posted) + " " + DateUtils.formatDateTime(context, n.getDate(), DateUtils.FORMAT_SHOW_YEAR));
        categories.setText(getCategoriesAsString(n.getCategories()));
        Picasso.with(context).load(n.getImage()).into(image);
        final boolean follow = n.isFollowing();
        final boolean registered = n.isRegistered();
        if (n.getSource() == Source.EVENEMENT) {
            layout.findViewById(R.id.event_specific).setVisibility(View.VISIBLE);
            String time = DateUtils.formatDateRange(context, n.getDateEvent(), n.getDateEvent() + n.getEventDuration() * 60000L, DateUtils.FORMAT_SHOW_TIME);
            ((TextView) layout.findViewById(R.id.begin_date)).setText(DateUtils.formatDateTime(context, n.getDateEvent(), DateUtils.FORMAT_SHOW_YEAR));
            ((TextView) layout.findViewById(R.id.event_duration)).setText(time);
        }
        if (follow) {
            favButton.setText(getString(R.string.remove) + " " + n.getAuthor() + " " + getString(R.string.from_fav));
            favButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            favButton.setTextColor(Color.WHITE);
        } else {
            favButton.setText(getString(R.string.add) + " " + n.getAuthor() + " " + getString(R.string.to_fav));
        }
        if (registered) {
            registerButton.setText(R.string.remove_event);
            registerButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            registerButton.setTextColor(Color.WHITE);
        } else {
            registerButton.setText(R.string.add_event);
        }
        Log.d("FAVORITE", String.valueOf(follow));
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(context).execWriteQuery(new QueryBuilder().updateFollow(follow, n.getAuthor()));
                ((MainActivity) context).switchContent();
            }
        });
        if (n.getSource() == Source.EVENEMENT) {
            registerButton.setVisibility(View.VISIBLE);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!registered) {
                        if (new CalendarHandler(context).addEvent(n.getTitle(), n.getContent(), n.getDateEvent(), n.getEventDuration(), n.getId())) {
                            DBHelper.getInstance(context).execWriteQuery(new QueryBuilder().updateRegister(false, n.getId()));
                            showCalendarDialog(getString(R.string.cal_update));
                        } else {
                            showCalendarDialog(getString(R.string.no_cal_update));
                        }
                    } else {
                        DBHelper.getInstance(context).execWriteQuery(new QueryBuilder().updateRegister(true, n.getId()));
                        if (new CalendarHandler(context).removeEvent(n.getId())) {
                            showCalendarDialog(getString(R.string.cal_remove));
                        } else {
                            showCalendarDialog(getString(R.string.no_cal_remove));
                        }
                    }
                    ((MainActivity) getActivity()).switchContent();
                }
            });
        }
    }

    @NonNull
    private String getCategoriesAsString(List<Category> categories) {
        if (categories.isEmpty()) return getString(R.string.no_category);
        else {
            return "Catégories : " + categories.toString().substring(1, categories.toString().length() - 1);
        }
    }

    private void showCalendarDialog(String content) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.cal_action))
                .setMessage(content)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_full_news, container, false);
        return root;
    }

    private class CloseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getFragmentManager();
            fm.popBackStack(Util.FULLNEWSFRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
