package ihm.projetihm.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class FlexGridLayoutManager extends GridLayoutManager {

    private int minItemWidth;
    private Context context;

    public FlexGridLayoutManager(Context context, int minItemWidth) {
        super(context, 1);
        this.context = context;
        this.minItemWidth = minItemWidth;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler,
                                 RecyclerView.State state) {
        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }

    private void updateSpanCount() {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int spanCount = (int) (getWidth() / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)) / minItemWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        this.setSpanCount(spanCount);
    }
}