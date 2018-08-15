package com.fancoff.coffeemaker.ui.goods;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.coffe.CoffeeBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.ui.commomView.MyTextViewSkill;
import com.fancoff.coffeemaker.ui.main.MainPageFragment;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 商品界面
 */
public class GoodsFragment extends BaseFragment {
    RecyclerView recyclerView;
    GoodsFragmentAdapter.OnItemClick onItemClick;
    String Categorie;
    public GoodsFragmentAdapter.OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public String getCategorie() {
        return Categorie;
    }

    /**GoodsFragmentAdapter中的getItem会调用该方法，传过来："type"+position+"-"*/
    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    /**GoodsFragmentAdapter类的getItem会调用该方法*/
    public void setOnItemClick(GoodsFragmentAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public GoodsFragment() {
    }

    ArrayList<CoffeeBean> goods = new ArrayList<CoffeeBean>();

    public ArrayList<CoffeeBean> getGoods() {
        return goods;
    }

    /**
     * 设置goods；GoodsFragmentAdapter类的refGoods()方法会调用该方法；
     * 1、ArrayList<CoffeeBean> goods 饮品列表清空
     * 2、goods饮品列表添加传入的饮品列表：ArrayList<CoffeeBean> ggoods
     */
    public void setGoods(ArrayList<CoffeeBean> ggoods) {
        goods.clear();
        goods.addAll(ggoods);
//        if (goods.size() < 12) {
//            for (int i = ggoods.size(); i < 12; i++) {
//                goods.add(null);
//
//            }
//
//        }
    }

    public static GoodsFragment newInstance(Bundle args) {
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.goods_fragment;
    }

    CommonAdapter commonAdapter;

    /***
     * 1、定义一个GridLayoutManager对象，获得布局中recyclerView的id，在recyclerView中加入GridLayoutManager对象；
     * 2、commonAdapter适配器定义：item为goositem.xml，list为：ArrayList<CoffeeBean> good
     *      获得各控件id；设置名字；item点击事件监听；
     * 3、recyclerView绑定适配器commonAdapter；
     */
    @Override
    public void initView(View v) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), MainPageFragment.colume);//3列
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);//goods_fragment.xml下的@+id/recyclerView
        recyclerView.setLayoutManager(gridLayoutManager);
        commonAdapter = new CommonAdapter<CoffeeBean>(getActivity(), R.layout.goositem, goods) {
            @Override
            protected void convert(ViewHolder holder, final CoffeeBean goodsBean, final int position) {

                MyRelativeLayout bg = (MyRelativeLayout) holder.getConvertView().findViewById(R.id.bg);
//                MyRelativeLayout.LayoutParams layoutParams = new RelativeLayout
//                        .LayoutParams(SizeUtil.screenWidth() / MainPageFragment.colume, SizeUtil.heightSize(MainPageFragment.tabItemHeight));
                MyRelativeLayout.LayoutParams layoutParams = new RelativeLayout
                        .LayoutParams(SizeUtil.heightSize(MainPageFragment.tabItemWith), SizeUtil.heightSize(MainPageFragment.tabItemHeight));

                bg.setLayoutParams(layoutParams);
                MyImageView img = (MyImageView) holder.getConvertView().findViewById(R.id.imageView);
                MyImageView img_type = (MyImageView) holder.getConvertView().findViewById(R.id.img_type);
                MyTextView t_name = (MyTextView) holder.getConvertView().findViewById(R.id.t_name);
                MyTextView t_name_en = (MyTextView) holder.getConvertView().findViewById(R.id.t_name_en);
                MyImageView img_skill = (MyImageView) holder.getConvertView().findViewById(R.id.img_skill);
                MyTextView t_cxs = (MyTextView) holder.getConvertView().findViewById(R.id.t_cxs);
                MyTextView t_leftcups = (MyTextView) holder.getConvertView().findViewById(R.id.t_leftcups);
                MyTextView t_yuan = (MyTextView) holder.getConvertView().findViewById(R.id.t_yuan);
                MyTextViewSkill t_skill = (MyTextViewSkill) holder.getConvertView().findViewById(R.id.t_skill);
                MyTextView t_xian2 = (MyTextView) holder.getConvertView().findViewById(R.id.t_xian2);
                MyTextView t_xian = (MyTextView) holder.getConvertView().findViewById(R.id.t_xian);
                t_skill.bind(img_skill, img_type, t_xian, t_xian2);
                t_skill.update(Categorie+position,goodsBean, DataCenter.getInstance().getSeckillText());
                if (goodsBean != null) {
                    img.update(goodsBean.getImage());
                    t_name.update(goodsBean.getName());
                    t_name_en.update(goodsBean.getSub_name());
                    t_yuan.update("￥", goodsBean.getOrg_price(), true);
                } else {
                    ImageLoadUtils.getInstance().loadNetImage(img, DataCenter.getInstance().getEmptyGoodsUrl());
                    t_name.setText("");
                    t_name_en.setText("");
                    t_yuan.setText("");
                    t_xian2.setText("");
                    t_xian.setText("");
                    t_cxs.setText("");
                    t_leftcups.setText("");
                }
                RxView.clicks(holder.getConvertView()).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onItemClick.onGoodsItemClick(position, goodsBean);
                    }
                });
            }
        };
        recyclerView.setAdapter(commonAdapter);


    }

    /**
     * commonAdapter != null:
     *    notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
     */
    public void refGoods() {
        if (commonAdapter != null) {
            commonAdapter.notifyDataSetChanged();
        }


    }
}
