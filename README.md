# SuperRecycleView

该项目主要是: https://github.com/GongWnbo/SuperRecycleView 项目
我只是对sku 部分进行了算法优化，选择商品时速度加快了一点，
选择时提供个顾客的选择更加贴近现实，所有算法理论来源于：
https://keelii.com/2016/12/22/sku-multi-dimensional-attributes-state-algorithm/

## 修改部分：

  GoodsPropertyAdapter 类 {

  void addProperty(){

  该方法sku算法全部替换

  }
  }

  void put(int position, String title) {

  修改部分

  }


  SkuActivity 类{

  select(HashMap<Integer, String> sam) {

   部分修改
   }

  }


  utils 包增加了 https://github.com/hfkai/SkuSelects 项目下的Sku工具类

## 关于优化
  还有优化空间，在https://keelii.com/2016/12/22/sku-multi-dimensional-attributes-state-algorithm/
最后有记载，但是目前满足了需求,现在还没有优化的计划，

## 最后

我只是一个改代码的，如想star 请移步
+ https://github.com/GongWnbo/SuperRecycleView
+ https://github.com/hfkai/SkuSelects


