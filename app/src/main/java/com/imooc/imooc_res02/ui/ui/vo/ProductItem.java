package com.imooc.imooc_res02.ui.ui.vo;

import com.imooc.imooc_res02.bean.Product;

import java.net.PortUnreachableException;

/**
 * Created by Gln on 2017/5/5.
 */
public class ProductItem extends Product {
    public int count;

    public ProductItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.restaurant = product.getRestaurant();
    }
}
