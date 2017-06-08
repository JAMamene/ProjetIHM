package ihm.projetihm.Fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ihm.projetihm.Adapter.NewsRecyclerAdapter;
import ihm.projetihm.Adapter.FlexGridLayoutManager;
import ihm.projetihm.Database.DBHelper;
import ihm.projetihm.MainActivity;
import ihm.projetihm.R;


public class NewsFragment extends Fragment {

    private View root;

    public NewsFragment() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Cursor cursor = DBHelper.getInstance(getActivity()).execQuery(getArguments().getString("query"));
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.news_list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).refreshEntries(swipeRefreshLayout);
            }
        });
        recyclerView.setLayoutManager(new FlexGridLayoutManager(getActivity(),300));
        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(getActivity(), cursor);
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() <= 0) {
            recyclerView.setVisibility(View.GONE);
            TextView emptyText = (TextView) root.findViewById(R.id.empty_text);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_news_list, container, false);
        return root;
    }
}
