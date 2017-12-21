package com.ecjia.hamster.adapter;



import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ecjia.hamster.fragment.HomeFragment;

import java.util.List;

public class ECJiaPageAdapter extends PagerAdapter
{
    public List<View> mListViews;

    public ECJiaPageAdapter(List<View> mListViews)
    {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) 	{

        position=position%mListViews.size();
        container.removeView(mListViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡
        position=position%mListViews.size();
        container.addView(mListViews.get(position));
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mListViews.size()>1?Integer.MAX_VALUE:mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }
	@Override
	public CharSequence getPageTitle(
			int position)
	{
		return super.getPageTitle(position);
	}
	@Override
	public void finishUpdate(
			ViewGroup container)
	{
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object)
	{
		return super.getItemPosition(object);
	}
	

}
