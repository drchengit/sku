package com.gwb.superrecycleview.utils;

import android.text.TextUtils;
import android.util.Log;


import com.gwb.superrecycleview.BuildConfig;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：Sku算法
 * 类描述：
 * 创建人：N.Sun
 * 创建时间：2016/11/3 下午2:32
 * 修改人：N.Sun
 * 修改时间：2016/11/3 下午2:32
 * 修改备注：
 */

public class Sku {

    /**
     * 算法入口
     *
     * @param initData 所有库存的hashMap组合
     * @return 拆分所有组合产生的所有情况（生成客户端自己的字典）
     */
    public static Map<String, GoodsPropertyBean.StockGoodsBean> skuCollection(List<GoodsPropertyBean.StockGoodsBean> initData) {
        //用户返回数据
        HashMap<String, GoodsPropertyBean.StockGoodsBean> result = new HashMap<>();
        // 遍历所有库存
        for (GoodsPropertyBean.StockGoodsBean skuModel : initData ) {

            //根据；拆分key的组合
            List<GoodsPropertyBean.StockGoodsBean.GoodsInfoBean> skuKeyAttrs = skuModel.getGoodsInfo();
            ArrayList<String> tmpe = new ArrayList<>();
            for(GoodsPropertyBean.StockGoodsBean.GoodsInfoBean s:skuKeyAttrs){
                tmpe.add(s.getTabValue());
            }
            //获取所有的组合
            ArrayList<ArrayList<String>> combArr = combInArray(tmpe);

            // 对应所有组合添加到结果集里面
            for (int i = 0; i < combArr.size(); i++) {
                add2SKUResult(result, combArr.get(i), skuModel);
            }
            // 将原始的库存组合也添加进入结果集里面
            String key = TextUtils.join(";", tmpe);

            result.put(key, skuModel);
          if(BuildConfig.DEBUG)  Log.e("得到组合原始", key +skuModel);
        }
        if(BuildConfig.DEBUG)    for (String key : result.keySet()) {
            Log.e("得到组合", key + "       :" + result.get(key).toString());
        }


        return result;
    }

    /**
     * 获取所有的组合放到ArrayList里面
     *
     * @param skuKeyAttrs 单个key被； 拆分的数组
     * @return ArrayList
     */
    private static ArrayList<ArrayList<String>> combInArray(ArrayList<String> skuKeyAttrs) {
        if (skuKeyAttrs == null || skuKeyAttrs.size() <= 0)
            return null;
        int len = skuKeyAttrs.size();
        ArrayList<ArrayList<String>> aResult = new ArrayList<>();
        for (int n = 1; n < len; n++) {
            ArrayList<Integer[]> aaFlags = getCombFlags(len, n);
            for (int i = 0; i < aaFlags.size(); i++) {
                Integer[] aFlag = aaFlags.get(i);
                ArrayList<String> aComb = new ArrayList<>();
                for (int j = 0; j < aFlag.length; j++) {
                    if (aFlag[j] == 1) {
                        aComb.add(skuKeyAttrs.get(j));
                    }
                }
                aResult.add(aComb);
            }
        }
        return aResult;
    }

    /**
     * 添加到数据集合
     *
     * @param result
     * @param newKeyList
     * @param skuModel
     */
    private static void add2SKUResult(HashMap<String, GoodsPropertyBean.StockGoodsBean> result, ArrayList<String> newKeyList, GoodsPropertyBean.StockGoodsBean skuModel) {
        String key = TextUtils.join(";", newKeyList);
        if (result.keySet().contains(key)) {
            result.get(key).setStock(result.get(key).getStock() + skuModel.getStock());
            result.get(key).setGoodsInfo(skuModel.getGoodsInfo());
//            result.get(key).setPrice(skuModel.getPrice());
        } else {
            result.put(key, new GoodsPropertyBean.StockGoodsBean(skuModel.getGoodsInfo(),skuModel.getStock()));
        }
    }

    /**
     * 算法拆分组合 用1和0 的移位去做控制
     * （这块需要你打印才能看的出来）
     *
     * @param len
     * @param n
     * @return
     */
    private static ArrayList<Integer[]> getCombFlags(int len, int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        ArrayList<Integer[]> aResult = new ArrayList<>();
        Integer[] aFlag = new Integer[len];
        boolean bNext = true;
        int iCnt1 = 0;
        //初始化
        for (int i = 0; i < len; i++) {
            aFlag[i] = i < n ? 1 : 0;
        }
        aResult.add(aFlag.clone());
        while (bNext) {
            iCnt1 = 0;
            for (int i = 0; i < len - 1; i++) {
                if (aFlag[i] == 1 && aFlag[i + 1] == 0) {
                    for (int j = 0; j < i; j++) {
                        aFlag[j] = j < iCnt1 ? 1 : 0;
                    }
                    aFlag[i] = 0;
                    aFlag[i + 1] = 1;
                    Integer[] aTmp = aFlag.clone();
                    aResult.add(aTmp);
                    if (!TextUtils.join("", aTmp).substring(len - n).contains("0")) {
                        bNext = false;
                    }
                    break;
                }
                if (aFlag[i] == 1) {
                    iCnt1++;
                }
            }
        }
        return aResult;
    }

}
