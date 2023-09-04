package com.yhchy.canso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhchy.canso.model.dto.picture.PictureQueryRequest;
import com.yhchy.canso.service.PictureService;
import com.yhchy.canso.common.BaseResponse;
import com.yhchy.canso.common.ErrorCode;
import com.yhchy.canso.common.ResultUtils;
import com.yhchy.canso.exception.ThrowUtils;
import com.yhchy.canso.model.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 17:54
 * @Description: 图片接口
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        long current = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
        return ResultUtils.success(picturePage);
    }

}
