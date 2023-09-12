package com.yhchy.canso.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yhchy.canso.service.PostService;
import com.yhchy.canso.model.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 11:54
 * @Description: 获取初始帖子列表
 */
// 取消注释后，每次启动 springboot 项目时都会执行一次 run 方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 1. 获取数据
        String json = "{\"current\":5,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        // 2. json 转对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tmpRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tmpRecord.getStr("title"));
            post.setContent(tmpRecord.getStr("content"));
            JSONArray tags = (JSONArray) tmpRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1689986144312033281L);
            postList.add(post);
        }
        // 3. 数据入库
        try {
            postService.saveBatch(postList);
            log.info("获取初始帖子列表成功, 条数 = {}", postList.size());
        } catch (Exception e) {
            log.info("获取初始帖子列表失败");
            throw new RuntimeException(e);
        }

    }
}
