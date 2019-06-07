package org.springultron.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * myBatis plus工具
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
        page.setAsc(query.getAscs());
        page.setDesc(query.getDescs());
        return page;
    }
}
