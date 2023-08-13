package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 17:35
 * @Description: 图片服务
 */
public interface PictureService {
    /**
     * 查询图片，从外部获取
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
