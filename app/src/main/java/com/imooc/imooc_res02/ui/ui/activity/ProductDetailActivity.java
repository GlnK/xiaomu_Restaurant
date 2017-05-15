package com.imooc.imooc_res02.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.imooc_res02.R;
import com.imooc.imooc_res02.bean.Product;
import com.imooc.imooc_res02.config.Config;
import com.imooc.imooc_res02.utils.T;
import com.squareup.picasso.Picasso;

/**
 * Created by Gln on 2017/5/11.
 */
public class ProductDetailActivity extends BaseActivity {
    private Product mProduct;

    private ImageView mIvImage;
    private TextView mTvtitle;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private static final String KEY_PRODUCT = "key_product";

    public static void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setUpToolbar();
        setTitle("详情");

        Intent intent = getIntent();
        if (intent != null) {
            mProduct = (Product) intent.getSerializableExtra(KEY_PRODUCT);
        }
        if (mProduct == null) {
            T.showToast("参数传递错误！");
            return;
        }

        initView();
    }

    private void initView() {
        mIvImage = (ImageView) findViewById(R.id.id_iv_image);
        mTvDesc = (TextView) findViewById(R.id.id_tv_desc);
        mTvPrice = (TextView) findViewById(R.id.id_tv_price);
        mTvtitle = (TextView) findViewById(R.id.id_tv_title);

        Picasso.with(this)
                .load(Config.baseUrl + mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvtitle.setText(mProduct.getName());
        mTvDesc.setText(mProduct.getDescription());
        mTvPrice.setText(mProduct.getPrice() + "元/份");

    }
}
