package ihm.projetihm;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ihm.projetihm.Database.DBHelper;
import ihm.projetihm.Database.QueryBuilder;
import ihm.projetihm.Fragment.NewsFragment;
import ihm.projetihm.Model.Category;
import ihm.projetihm.Model.Source;
import ihm.projetihm.Twitter.TwitterHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private PopupWindow pw;
    private LinearLayout mag_cat;
    private LinearLayout event_cat;

    private QueryBuilder qb;

    private Source currentSource;
    private Map<Category, CheckBox> categorySelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DBHelper dbHelper = DBHelper.getInstance(getBaseContext());
        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        intializeData();
        navigationView.setNavigationItemSelectedListener(this);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_CALENDAR},
                1);
        navigationView.setCheckedItem(R.id.nav_all);
        onNavigationItemSelected(navigationView.getMenu().getItem(2));
    }

    public void refreshEntries(SwipeRefreshLayout swipeRefreshLayout) {
        new TwitterHandler(this, swipeRefreshLayout).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_news);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String query) {
                qb.setSearch(query);
                switchContent();
                return false;
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.popupwindow,
                (ViewGroup) findViewById(R.id.popup_window), false);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double width = 0.8 * size.x;
        double height = 0.8 * size.y;
        pw = new PopupWindow(layout, (int) width, (int) height, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        LinearLayout all_cat = (LinearLayout) layout.findViewById(R.id.all_cat);
        mag_cat = (LinearLayout) layout.findViewById(R.id.mag_cat);
        event_cat = (LinearLayout) layout.findViewById(R.id.event_cat);
        for (Category cat : categorySelection.keySet()) {
            CheckBox curr = categorySelection.get(cat);
            if (Source.COMMON_CATEGORIES.contains(Category.valueOf(curr.getText().toString()))) {
                all_cat.addView(curr);
            } else if (Source.MAGASIN_CATEGORIES.contains(Category.valueOf(curr.getText().toString()))) {
                mag_cat.addView(curr);
            } else if (Source.EVENEMENT_CATEGORIES.contains(Category.valueOf(curr.getText().toString()))) {
                event_cat.addView(curr);
            }
        }
        Button close = (Button) layout.findViewById(R.id.close_popup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        pw.setAnimationStyle(R.style.Slide);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my_stuff) {
            qb.buildMyStuff();
            currentSource = Source.ALL;
        } else if (id == R.id.nav_my_events) {
            qb.buildMyEvents();
            currentSource = Source.EVENEMENT;
        } else if (id == R.id.nav_all) {
            qb.setNavNull();
            currentSource = Source.ALL;
        } else if (id == R.id.nav_events) {
            qb.setNav(Source.EVENEMENT);
            currentSource = Source.EVENEMENT;
        } else if (id == R.id.nav_shops) {
            qb.setNav(Source.MAGASIN);
            currentSource = Source.MAGASIN;
        } else if (id == R.id.nav_tweets) {
            qb.setNav(Source.TWITTER);
            currentSource = Source.TWITTER;
        }
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switchContent();
        return true;
    }

    public void showFilters(MenuItem item) {
        View layout = getLayoutInflater().inflate(R.layout.popupwindow,
                (ViewGroup) findViewById(R.id.popup_window), false);
        switch (currentSource) {
            case MAGASIN:
                mag_cat.setVisibility(View.VISIBLE);
                event_cat.setVisibility(View.GONE);
                break;
            case TWITTER:
                mag_cat.setVisibility(View.GONE);
                event_cat.setVisibility(View.GONE);
                break;
            case EVENEMENT:
                mag_cat.setVisibility(View.GONE);
                event_cat.setVisibility(View.VISIBLE);
                break;
            case ALL:
                mag_cat.setVisibility(View.VISIBLE);
                event_cat.setVisibility(View.VISIBLE);
        }
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    public void switchContent() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(Util.FULLNEWSFRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Bundle bundle = new Bundle();
        bundle.putString("query", qb.buildRequest(categorySelection));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(Util.NEWSFRAGMENT);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERM", "Nice");
        }

    }

    private void intializeData() {
        qb = new QueryBuilder();
        categorySelection = new HashMap<>();
        for (Category c : Category.values()) {
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(c.name());
            cb.setChecked(false);
            cb.setPadding(4, 4, 4, 4);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton item, boolean isChecked) {
                    switchContent();
                }
            });
            categorySelection.put(c, cb);
        }
        currentSource = Source.ALL;
    }
}
