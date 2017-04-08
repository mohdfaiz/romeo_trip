package romeotrip.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mohdfaiz.romeotrip.R;
import com.vstechlab.easyfonts.EasyFonts;

import me.relex.circleindicator.CircleIndicator;
import romeotrip.activity.DashBoardActivity;

public class PagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    Intent in = new Intent(getActivity(), DashBoardActivity.class);
                    startActivity(in);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

//    Typeface typeface;
    private class ImagePagerAdapter extends PagerAdapter {

        private final LayoutInflater mLayoutInflater;
        private int[] mImages = new int[]{
                R.drawable.login_bg,
                R.drawable.login_bg,
                R.drawable.login_bg,
                R.drawable.login_bg
        };

        String value1 = "<p> Welcome to Romeo Trip. </p> <p> Just select your destination where you want to go. </b> </b> Continue... </p>";
        String value2 = "<p> Explore your destination and select your favourite places to visit. </b> </b> Continue...</p>";
        String value3 = "<p> Finally checkout and earn point with trip.  </b> </b> Continue...</p>";
        String value4 ="";



        private String[] mText = new String[]{
                value1,
                value2,
                value3,
                value4
        };

        public ImagePagerAdapter(Context mContext) {
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mImages[position]);

            TextView textview = (TextView) itemView.findViewById(R.id.textview);
            textview.setText(Html.fromHtml(mText[position]));

            textview.setTypeface(EasyFonts.caviarDreams(getActivity()));

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
