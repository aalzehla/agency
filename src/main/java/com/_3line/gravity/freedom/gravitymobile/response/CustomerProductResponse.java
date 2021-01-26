/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.gravitymobile.response;


import com._3line.gravity.core.models.Response;

import java.util.List;
import java.util.Map;

/**
 *
 * @author OlalekanW
 */
public class CustomerProductResponse extends Response {
  private List<Map<String, Object>> products;

    /**
     * @return the products
     */
    public List<Map<String, Object>> getProducts() {
        return products;
                                                   }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
                                                                }
                                                    }
