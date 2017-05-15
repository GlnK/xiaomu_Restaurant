package com.imooc.imooc_res02.biz;

import com.imooc.imooc_res02.bean.Order;
import com.imooc.imooc_res02.bean.Product;
import com.imooc.imooc_res02.config.Config;
import com.imooc.imooc_res02.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Gln on 2017/5/2.
 */
public class OrderBiz {
    public void ListByPage(int currentPage, CommonCallback<List<Order>> commonCallback) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "order_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(commonCallback);
    }

    public void add(Order order, CommonCallback<String> commonCallback) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> productMap = order.productMap;
        for (Product p : productMap.keySet()) {
            sb.append(p.getId() + "_" + productMap.get(p));
            sb.append("|");
        }
        sb = sb.deleteCharAt(sb.length() - 1);

        OkHttpUtils.post()
                .url(Config.baseUrl + "order_add")
                .addParams("res_id", order.getRestaurant().getId()+"")
                .addParams("product_str",sb.toString())
                .addParams("count",order.getCount()+"")
                .addParams("price",order.getPrice()+"")
                .tag(this)
                .build()
                .execute(commonCallback);

    }

    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
