package com.yhchy.canso.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Author: Yang Hengcan
 * @Date: 2023/9/4 21:01
 * @Description: 数据源接口
 */
public interface DataSource<T> {
    /**
     * 统一分页接口
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
