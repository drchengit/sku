package com.gwb.superrecycleview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwb.superrecycleview.BuildConfig;
import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.utils.Sku;
import com.gwb.superrecycleview.ui.wedgit.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${GongWenbo} on 2018/3/30 0030.
 */

public class GoodsPropertyAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "GoodsPropertyAdapter";
    private final String COLOR_SELECT = "#ffffff";
    private final String COLOR_EMPTY = "#BBBBBB";
    private final String COLOR_NORMAL = "#6D6D6D";
    //sku 后的库存子集合
    public Map<String, GoodsPropertyBean.StockGoodsBean> result;
    private List<GoodsPropertyBean.AttributesBean> mAttributes;
    private Context mContext;
    private int layoutId;
    private TextView[][] mTextViews;
    // 每一行选中的属性
    private HashMap<Integer, String> sam = new HashMap<>();
    // 每一行可选的属性
    private SimpleArrayMap<Integer, List<String>> sams = new SimpleArrayMap<>();

    private GoodsSelectListener mGoodsSelectListener;
    private StringBuilder sb;

    public GoodsPropertyAdapter(List<GoodsPropertyBean.AttributesBean> attributes, List<GoodsPropertyBean.StockGoodsBean> stockGoods, Context context, @LayoutRes int layoutId) {
        this.mAttributes = attributes;
        result = Sku.skuCollection(stockGoods);
        this.mContext = context;
        this.layoutId = layoutId;
        int size = attributes.size();
        mTextViews = new TextView[size][0];
    }

    public void setGoodsSelectListener(GoodsSelectListener goodsSelectListener) {
        mGoodsSelectListener = goodsSelectListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.createViewHolder(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        GoodsPropertyBean.AttributesBean attributesBean = mAttributes.get(position);
        // 标题
        holder.setTitle(R.id.tv_title, attributesBean.getTabName());
        // 一行具体的view
        FlowLayout flowLayout = holder.getView(R.id.flowLayout);
        List<String> attributesItem = attributesBean.getAttributesItem();
        int size = attributesItem.size();
        TextView[] textViews = new TextView[size];
        for (int i = 0; i < attributesItem.size(); i++) {
            final String property = attributesItem.get(i);
            TextView textView = getTextView(property, holder);
            flowLayout.addView(textView);
            textViews[i] = textView;
        }
        mTextViews[position] = textViews;
        sb = new StringBuilder();
    }

    public TextView getTextView(final String title, final BaseViewHolder holder) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView tv = new TextView(mContext);
        lp.setMargins(10, 10, 10, 10);
        tv.setPadding(40, 20, 40, 20);
        tv.setBackgroundResource(R.drawable.normal);
        tv.setTextColor(Color.parseColor(COLOR_NORMAL));
        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                Log.d(TAG, "position== " + position);
                // TODO: 2018/5/7 0007 每一行选中的属性
                put(position, title);
                // TODO: 2018/5/6 每一行有的属性
                addProperty(position);
                // TODO: 2018/5/6 根据商品的状态绘制
                initStatus();

                Log.d(TAG, "onClick: sam" + sam.toString());
                Log.d(TAG, "onClick: sams" + sams.toString());

                // 回调
                if (mGoodsSelectListener != null) {
                    mGoodsSelectListener.select(sam);
                }
            }
        });
        return tv;
    }

    public void put(int position, String title) {

        //  如果点击的时候同一个,不做处理
        String str = sam.get(position);
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(title)) {
                sam.remove(position);
            } else {
                //  如果点击的是同一行已有的，先清空掉
                sam.put(position, title);
            }
        } else {
            // TODO: 2018/5/7 0007 如果点击的是没有的话，清空重置
            List<String> list = sams.get(position);
            if (list != null && !list.contains(title)) {
                sam.clear();
            }
            sam.put(position, title);
        }

    }

    private void addProperty(int position) {
        for (int i = 0; i < mAttributes.size(); i++) {//遍历每行的规格
            List<String> lineSpecs = mAttributes.get(i).getAttributesItem();
            //  初始化每一行的属性容器 并填满规格
            List<String> list = new ArrayList<>();
            list.addAll(mAttributes.get(i).getAttributesItem());//所有默认不置灰
            for (String spec : lineSpecs) {//每一个规格
                if (sam.values().contains(spec)) continue;//已选择的是不管的
                if (sam.get(i) != null && sam.get(i).trim().length() > 0) {//当前行已选

//                    i //当前行数
//                    spec //当前的规格
//                    sam //所有已选规格
                    for (int key : sam.keySet()) {
                        if (sb.length() != 0) sb.append(";");
                        if (i == key) {
                            sb.append(spec);
                        } else {
                            sb.append(sam.get(key));
                        }
                    }
                    if (!result.keySet().contains(sb.toString())) {//不存在则置灰
                        list.remove(spec);
                    }
                    if (BuildConfig.DEBUG) Log.e("已选行与sam形成路径", i + ":" + sb.toString());
                } else {//当前行未选与sam的形成路径 不存在则置灰
//                    i //当前行数
//                    spec //当前的规格
//                    sam //所有已选规格


                    Integer[] tmpes = new Integer[sam.keySet().size() + 1];
                    sam.keySet().toArray(tmpes);
                    tmpes[sam.size()] = i;
                    int temp;//临时变量
                    boolean flag;//是否交换的标志
                    for (int m = 0; m < tmpes.length - 1; m++) {//冒泡排序
                        flag = false;
                        for (int n = tmpes.length - 1; n > m; n--) {
                            if (tmpes[n] < tmpes[n - 1]) {
                                temp = tmpes[n];
                                tmpes[n] = tmpes[n - 1];
                                tmpes[n - 1] = temp;
                                flag = true;
                            }
                        }
                        if (!false) break;
                    }
                    for (int key : tmpes) {
                        if (sb.length() != 0) sb.append(";");
                        if (sam.get(key) != null && sam.get(key).trim().length() > 0) {
                            sb.append(sam.get(key));
                        } else {
                            sb.append(spec);
                        }
                    }
                    if (BuildConfig.DEBUG) Log.e("未选行与sam形成路径", i + ":" + sb.toString());
                    if (!result.keySet().contains(sb.toString())) {//不存在则置灰
                        list.remove(spec);
                    }
                }


                sb.setLength(0);
            }
            sams.put(i, list);
        }


    }

    private void initStatus() {
        for (int i = 0; i < mTextViews.length; i++) {
            List<String> list = sams.get(i);
            // 之前选中的
            String select = sam.get(i);
            TextView[] textViews = mTextViews[i];
            for (TextView textView : textViews) {
                String title = textView.getText().toString();
                // 如果是之前选中的，不做处理
                if (!TextUtils.isEmpty(select) && select.equals(title)) {
                    textView.setBackgroundResource(R.drawable.select);
                    textView.setTextColor(Color.parseColor(COLOR_SELECT));
                } else if (list.contains(title)) {
                    // 如果是可选的
                    textView.setEnabled(true);
                    textView.setBackgroundResource(R.drawable.normal);
                    textView.setTextColor(Color.parseColor(COLOR_NORMAL));
                } else {
                    // 如果是不可选的
                    textView.setEnabled(false);
                    textView.setBackgroundResource(R.drawable.empty);
                    textView.setTextColor(Color.parseColor(COLOR_EMPTY));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAttributes == null ? 0 : mAttributes.size();
    }

    public interface GoodsSelectListener {

        void select(HashMap<Integer, String> sam);

    }

}
