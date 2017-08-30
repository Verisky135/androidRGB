package com.example.verisky.rgbmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.verisky.rgbmap.dummy.DummyContent;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            Context context= ((ImageView) rootView.findViewById(R.id.item_detail_image)).getContext();
            int id = context.getResources().getIdentifier("pic" + mItem.id, "drawable", context.getPackageName());
            ((ImageView) rootView.findViewById(R.id.item_detail_image)).setImageResource(id);
            Bitmap bitmap = ((BitmapDrawable)((ImageView) rootView.findViewById(R.id.item_detail_image)).getDrawable()).getBitmap();
            int pixel = 0;
            int sumR=0, sumG=0, sumB=0;
            for(int x=0; x<bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    pixel = bitmap.getPixel(x, y);
                    sumR += pixel >> 16 & 0xFF;
                    sumG += pixel >> 8 & 0xFF;
                    sumB += pixel >> 0 & 0xFF;
                }
            }

            int max = 0;
            if (sumR>sumG & sumR>sumB) {
                max = sumR;
            }
            else {
                if (sumG>sumB) {
                    max = sumG;
                }
                else {
                    max = sumB;
                }
            }
            sumR = (int)(((double)sumR)/(double)max*100);
            sumG = (int)(((double)sumG)/(double)max*100);
            sumB = (int)(((double)sumB)/(double)max*100);
            int sumGray = (sumR+sumG+sumB)/3;

            //((TextView) rootView.findViewById(R.id.item_detail)).setText(sumR +" "+ sumG +" "+ sumB);
            BarChart barChart = (BarChart) rootView.findViewById(R.id.chart);
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(sumR, 0));
            entries.add(new BarEntry(sumG, 1));
            entries.add(new BarEntry(sumB, 2));
            entries.add(new BarEntry(sumGray, 3));
            BarDataSet dataset = new BarDataSet(entries, "RGB Gray");
            int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xff404040};
            dataset.setColors(colors);

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Red");
            labels.add("Green");
            labels.add("Blue");
            labels.add("Grayscale");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
        }

        return rootView;
    }
}
