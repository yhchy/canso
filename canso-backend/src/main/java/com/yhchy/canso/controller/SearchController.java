package com.yhchy.canso.controller;

import com.yhchy.canso.model.dto.search.SearchRequest;
import com.yhchy.canso.service.PictureService;
import com.yhchy.canso.common.BaseResponse;
import com.yhchy.canso.common.ResultUtils;
import com.yhchy.canso.manager.SearchFacade;
import com.yhchy.canso.model.vo.SearchVO;
import com.yhchy.canso.service.PostService;
import com.yhchy.canso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 22:54
 * @Description: 图片接口
 */
@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    @Resource
    private SearchFacade searchFacade;

    /**
     * 分页获取列表（封装类）
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchALl(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchALl(searchRequest, request));
    }

}
