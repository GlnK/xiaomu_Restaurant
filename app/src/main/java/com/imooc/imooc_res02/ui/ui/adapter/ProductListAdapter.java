package com.imooc.imooc_res02.ui.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.imooc_res02.R;
import com.imooc.imooc_res02.config.Config;
import com.imooc.imooc_res02.ui.ui.activity.ProductDetailActivity;
import com.imooc.imooc_res02.ui.ui.vo.ProductItem;
import com.imooc.imooc_res02.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gln on 2017/5/5.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder> {

    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, List<ProductItem> productItems) {
        mContext = context;
        mProductItems = productItems;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ProductListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductListItemViewHolder holder, int position) {
        ProductItem productItem = mProductItems.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl + productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.count + "");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice() + "元/份");
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public interface OnProductListener {
        void onProductAdd(ProductItem productItem);

        void onProductSub(ProductItem productItem);
    }

    private OnProductListener mOnProductListener;

    public void setOnProductListener(OnProductListener onProductListener) {
        mOnProductListener = onProductListener;
    }

    class ProductListItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;

        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;

        public ProductListItemViewHolder(View itemView) {
            super(itemView);


            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
            mTvLabel = (TextView) itemView.findViewById(R.id.id_tv_label);
            mTvName = (TextView) itemView.findViewById(R.id.id_tv_name);
            mTvPrice = (TextView) itemView.findViewById(R.id.id_tv_price);
            mIvAdd = (ImageView) itemView.findViewById(R.id.id_iv_add);
            mIvSub = (ImageView) itemView.findViewById(R.id.id_iv_sub);
            mTvCount = (TextView) itemView.findViewById(R.id.id_tv_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//// TODO: 2017/5/5
                    ProductDetailActivity.launch(mContext, mProductItems.get(getLayoutPosition()));

                }
            });
            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    productItem.count += 1;
                    mTvCount.setText(productItem.count + "");
                    //会掉到Activity
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductAdd(productItem);
                    }
                }
            });
            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    if (productItem.count <= 0) {
                        T.showToast("已经是0了");
                        return;
                    }
                    productItem.count -= 1;
                    mTvCount.setText(productItem.count + "");
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductSub(productItem);
                    }
                }
            });
        }
    }
}



