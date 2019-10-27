package org.springultron.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * MyBatis Plus工具
 *
 * @author brucewuu
 * @date 2019-06-07 22:28
 */
public class PageUtils {

    private PageUtils() {}

    /**
     * 转化成myBatis plus中的分页对象Page
     * 默认返回第一页十条数据
     *
     * @param query 查询条件
     * @return 分页对象
     */
    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(null == query.getCurrent() ? 1 : query.getCurrent(), null == query.getSize() ? 10 : query.getSize());
        if (null != query.getCurrent() && query.getCurrent() != 1) {
            page.setSearchCount(query.isSearchCount());
        }
        if (null != query.getAscs()) {
            final int length = query.getAscs().length;
            if (length == 1) {
                page.addOrder(OrderItem.asc(query.getAscs()[0]));
            } else if (length > 1) {
                page.addOrder(OrderItem.ascs(query.getAscs()));
            }
        }
        if (null != query.getDescs()) {
            final int length = query.getDescs().length;
            if (length == 1) {
                page.addOrder(OrderItem.desc(query.getDescs()[0]));
            } else if (length > 1) {
                page.addOrder(OrderItem.descs(query.getDescs()));
            }
        }
        return page;
    }
}
