package com.yhchy.canso.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhchy.canso.common.ErrorCode;
import com.yhchy.canso.datasource.*;
import com.yhchy.canso.exception.BusinessException;
import com.yhchy.canso.exception.ThrowUtils;
import com.yhchy.canso.model.dto.post.PostQueryRequest;
import com.yhchy.canso.model.dto.search.SearchRequest;
import com.yhchy.canso.model.dto.user.UserQueryRequest;
import com.yhchy.canso.model.entity.Picture;
import com.yhchy.canso.model.enums.SearchTypeEnum;
import com.yhchy.canso.model.vo.PostVO;
import com.yhchy.canso.model.vo.SearchVO;
import com.yhchy.canso.model.vo.UserVO;
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
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private DataSourceRegistry dataSourceRegistry;

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
                return userDataSource.doSearch(searchText, current, pageSize);
            });

            CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setTitle(searchText);
                return postDataSource.doSearch(searchText, current, pageSize);
            });

            CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> pictureDataSource.doSearch(searchText, current, pageSize));

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
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(searchType);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
