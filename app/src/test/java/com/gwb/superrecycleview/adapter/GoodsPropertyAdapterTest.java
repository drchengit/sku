package com.gwb.superrecycleview.adapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author DrChen
 * @Date 2019/9/28 0028.
 * qq:1414355045
 */
public class GoodsPropertyAdapterTest {
@Test
    public  void test(){

    HashSet<Integer> sam = new HashSet<>();
    sam.add(4);
    sam.add(2);
    Integer[]tmpes = new Integer[sam.size()+1];
    sam.toArray(tmpes);
    tmpes[sam.size()]=3;
    System.out.println(Arrays.toString(tmpes));
    int temp;//临时变量
    boolean flag;//是否交换的标志
    for(int m = 0;m<tmpes.length-1;m++){//冒泡排序
        flag = false;
      for(int n = tmpes.length-1;n>m;n--){
            if(tmpes[n]<tmpes[n-1]){
                temp = tmpes[n];
                tmpes[n] = tmpes[n-1];
                tmpes[n-1] = temp;
         flag = true;
            }
      }
      if(!false)break;
    }
    System.out.println(Arrays.toString(tmpes));

}

}