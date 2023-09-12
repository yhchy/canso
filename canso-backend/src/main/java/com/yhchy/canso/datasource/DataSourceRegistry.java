package com.yhchy.canso.datasource;

import com.yhchy.canso.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Yang Hengcan
 * @Date: 2023/9/4 21:39
 * @Description:
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;

    private Map<String, DataSource<T>> dataSourceMap;

    @PostConstruct
    public void doInit() {
        dataSourceMap = new HashMap(3) {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        }};
    }

    public DataSource<?> getDataSourceByType(String type) {
        if (dataSourceMap == null) {
            return null;
        }
        return dataSourceMap.get(type);
    }
}
