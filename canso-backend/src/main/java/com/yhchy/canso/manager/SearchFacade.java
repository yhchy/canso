package com.yhchy.canso.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhchy.canso.common.ErrorCode;
import com.yhchy.canso.exception.BusinessException;
import com.yhchy.canso.exception.ThrowUtils;
import com.yhchy.canso.model.dto.post.PostQueryRequest;
import com.yhchy.canso.model.dto.search.SearchRequest;
import com.yhchy.canso.model.entity.Picture;
import com.yhchy.canso.model.vo.SearchVO;
import com.yhchy.canso.service.PictureService;
import com.yhchy.canso.service.PostService;
import com.yhchy.canso.model.dto.user.UserQueryRequest;
import com.yhchy.canso.model.enums.SearchTypeEnum;
import com.yhchy.canso.model.vo.PostVO;
import com.yhchy.canso.model.vo.UserVO;
import com.yhchy.canso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Yang Hengcan
 * @Date: 2023/8/13 11:40
 * @Description: 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    public SearchVO searchALl(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchType = searchRequest.getSearchType();
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchType);
        ThrowUtils.throwIf(StringUtils.isBlank(searchType), ErrorCode.PARAMS_ERROR);
        if (Objects.isNull(searchTypeEnum)) {
            CompletableFuture<Page<UserVO>> userCompletableFuture = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setTitle(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
                return picturePage;
            });

            CompletableFuture.allOf(userCompletableFuture, pictureCompletableFuture, postCompletableFuture).join();

            try {
                Page<UserVO> userVOPage = userCompletableFuture.get();
                Page<Picture> picturePage = pictureCompletableFuture.get();
                Page<PostVO> postVOPage = postCompletableFuture.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            switch (searchTypeEnum) {
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setTitle(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                default:
            }
            return searchVO;
        }
    }
}
