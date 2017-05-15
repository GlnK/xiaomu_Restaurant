package com.imooc.imooc_res02.biz;

import com.imooc.imooc_res02.bean.Product;
import com.imooc.imooc_res02.config.Config;
import com.imooc.imooc_res02.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * Created by Gln on 2017/5/5.
 */
public class ProductBiz {
    public void listByPage(int currentPage, CommonCallback<List<Product>> commonCallback) {
//                product_find
//               currentPage
        OkHttpUtils.post()
                .url(Config.baseUrl + "product_find")
                .addParams("currentPage", currentPage + "")
                .tag(this)
                .build()
                .execute(commonCallback);
    }

    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
    }


}
