package com.imooc.imooc_res02.ui.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.imooc.imooc_res02.R;
import com.imooc.imooc_res02.bean.Order;
import com.imooc.imooc_res02.bean.Product;
import com.imooc.imooc_res02.biz.OrderBiz;
import com.imooc.imooc_res02.biz.ProductBiz;
import com.imooc.imooc_res02.net.CommonCallback;
import com.imooc.imooc_res02.ui.ui.adapter.ProductListAdapter;
import com.imooc.imooc_res02.ui.ui.refresh.view.refresh.SwipeRefresh;
import com.imooc.imooc_res02.ui.ui.refresh.view.refresh.SwipeRefreshLayout;
import com.imooc.imooc_res02.ui.ui.vo.ProductItem;
import com.imooc.imooc_res02.utils.T;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvCount;
    private Button mBtnPay;

    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();

    private ProductBiz mProductBiz = new ProductBiz();
    private OrderBiz mOrderBiz = new OrderBiz();
    private Order mOrder = new Order();

    private int mCurrentPage;
    private float mTotalPrice;
    private int mTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar();
        setTitle("订单");

        initView();
        initEvent();
        loadDatas();
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });
        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量" + mTotalCount);
                mTotalPrice += productItem.getPrice();
                mBtnPay.setText((float)mTotalPrice + "元 立即支付");
                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量" + mTotalCount);
                mTotalPrice -= productItem.getPrice();
                mBtnPay.setText((float)mTotalPrice + "元 立即支付");
                mOrder.removeProduct(productItem);
            }
        });
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTotalCount <= 0) {
                    T.showToast("你还没有选择");
                    return;
                }
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());

                startLoadingProgress();

                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });
    }

    private void loadDatas() {
        startLoadingProgress();

        mProductBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrentPage = 0;
                mDatas.clear();
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                mTotalPrice = 0;
                mTotalCount = 0;
                mTvCount.setText("数量" + mTotalCount);

                mBtnPay.setText((float)mTotalPrice + "元 立即支付");
            }
        });
    }


    private void loadMore() {
        startLoadingProgress();

        mProductBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if (response.size() == 0) {
                    T.showToast("没有了");
                    return;
                }
                T.showToast("又找到" + response.size() + "道菜");
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swiperefresh);
        mTvCount = (TextView) findViewById(R.id.id_tv_count);
        mBtnPay = (Button) findViewById(R.id.id_btn_pay);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        mAdapter = new ProductListAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestroy();
    }
}
