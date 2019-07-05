package org.springultron.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis Plus工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-07 22:28
 * @Description:
 */
public class Condition {

    /**
     * 转化成myBatis plus中的分页对象Page
     *
     * @param query 查询条件
     * @return 分页对象
     */
    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(null == query.getCurrent() ? 1 : query.getCurrent(), null == query.getSize() ? 10 : query.getSize());
        List<OrderItem> orderItems = null;
        OrderItem orderItem;
        if (null != query.getAscs()) {
            orderItems = new ArrayList<>();
            for (String asc : query.getAscs()) {
                orderItem = OrderItem.asc(asc);
                orderItems.add(orderItem);
            }
        }
        if (null != query.getDescs()) {
            if (null == orderItems) {
                orderItems = new ArrayList<>();
            }
            for (String desc : query.getDescs()) {
                orderItem = OrderItem.desc(desc);
                orderItems.add(orderItem);
            }
        }
        if (null != orderItems) {
            page.setOrders(orderItems);
        }
        return page;
    }
}
