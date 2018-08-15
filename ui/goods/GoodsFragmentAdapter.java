package com.fancoff.coffeemaker.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;

import java.util.HashMap;
import java.util.Map;

public class GoodsFragmentAdapter extends FragmentPagerAdapter {
    private static final String ARG_TIMELINE_TYPE = "ARG_TIMELINE_TYPE";
    private static final String HEIGHT = "HEIGHT";
    private static final int PAGE_COUNT = 3;

    private Context mContext;
    CoffeeConfigBean coffeeConfigBean;
    OnItemClick onItemClick;
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;  //-2
    }

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * 遍历frgament列表：HashMap<Integer, GoodsFragment> map
     *    遍历到的GoodsFragment不为空：
     *        设置GoodsFragment类中的ArrayList<CoffeeBean> goods列表为 coffeeConfigBean.getCategories().get(entry.getKey()).getCoffees()
     *    刷新GoodsFragment列表；
     */
    public void refGoods() {
        for (Map.Entry<Integer, GoodsFragment> entry : map.entrySet()) {
            GoodsFragment g=entry.getValue();
            if(g!=null){
                g.setGoods(coffeeConfigBean.getCategories().get(entry.getKey()).getCoffees());
                g.refGoods();
            }
        }
    }

    /**adapter中的数据改变了，GoodsFragment中的商品列表需要刷新了*/
    public void ref() {
        notifyDataSetChanged();  //adapter中的数改变了
        refGoods();
    }

    public interface OnItemClick {
        public void onGoodsItemClick(int position, CoffeeBean coffeeBean);
    }

    /**咖啡工艺表传入本类coffeeConfigBean*/
    public void setCoffeeConfigBean(CoffeeConfigBean coffeeConfigBean) {
        this.coffeeConfigBean = coffeeConfigBean;
    }

    /**
     *
     */
    public GoodsFragmentAdapter(Context context, FragmentManager fm, CoffeeConfigBean coffeeConfigBean) {
        super(fm);
        this.coffeeConfigBean = coffeeConfigBean;
        this.mContext = context;
    }

    HashMap<Integer, GoodsFragment> map = new HashMap<Integer, GoodsFragment>();

    /**
     * 继承FragmentPagerAdapter需要重写的方法；
     *  1、新建一个GoodsFragment对象gd：
     *      position对应的GoodsFragment为空：新建一个带args参数的GoodsFragment实例；
     *      position对应的GoodsFragment不为空：map中position位置的GoodsFragment赋予gd
     *  2、GoodsFragment类中的属性String Categorie="type"+position+"-"
     *    设置gd监听onItemClick
     *  3、咖啡工艺表不为空:将position对应的咖啡饮品存入ArrayList<CoffeeBean> goods
     */
    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        args.putInt(ARG_TIMELINE_TYPE, position);//
        GoodsFragment gd;
        if (map.get(position) == null) {//position位置的GoodsFragment为空
            gd = GoodsFragment.newInstance(args);//新建一个带args参数的GoodsFragment实例；
            map.put(position, gd);//此position和gd放入map
        } else {
            gd = map.get(position);  //map中position位置的GoodsFragment赋予gd
        }
        gd.setCategorie("type"+position+"-");
        gd.setOnItemClick(onItemClick);//gd设置onItemClick监听
        if(coffeeConfigBean!=null){//咖啡工艺表不为空:将position对应的咖啡饮品存入ArrayList<CoffeeBean> goods
            if (position == 0) {
                if (coffeeConfigBean.getCategories().size() >= 1) {
                    gd.setGoods(coffeeConfigBean.getCategories().get(0).getCoffees());
                }

            } else if (position == 1) {
                if (coffeeConfigBean.getCategories().size() >= 2) {
                    gd.setGoods(coffeeConfigBean.getCategories().get(1).getCoffees());
                }

            } else if (position == 2) {
                if (coffeeConfigBean.getCategories().size() >= 3) {
                    gd.setGoods(coffeeConfigBean.getCategories().get(2).getCoffees());
                }
            }
        }

        return gd;
    }

    /**
     * 需要重写的方法：返回咖啡目录有多少个
     *   1、咖啡工艺中的咖啡目录为空，则返回0；
     *   2、返回有几个咖啡目录：如正常有3个：咖啡类、奶类、餐包；
     */
    @Override
    public int getCount() {
        if (coffeeConfigBean == null
                || coffeeConfigBean.getCategories() == null) {
            return 0;
        }
        return coffeeConfigBean.getCategories().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "";

    }
}
