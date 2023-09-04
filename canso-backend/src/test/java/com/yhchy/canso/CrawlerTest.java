package com.yhchy.canso;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yhchy.canso.model.entity.Picture;
import com.yhchy.canso.model.entity.Post;
import com.yhchy.canso.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 11:13
 * @Description:
 */
@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    public void testFetchPicture() throws IOException {
        String current = "1";
        String url = "https://cn.bing.com/images/search?q=白月魁&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : newsHeadlines) {
            // 取图片地址(murl)
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = Picture.builder().title(title).url(murl).build();
            pictureList.add(picture);
        }
        System.out.println("pictureList = " + JSONUtil.toJsonPrettyStr(pictureList));
    }

    @Test
    public void testFetchPassage() {
        // 1. 获取数据
        String json = "{\"current\":2,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
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
        boolean saveBatch = postService.saveBatch(postList);
        Assertions.assertTrue(saveBatch);
//        System.out.println("postList = " + JSONUtil.toJsonPrettyStr(postList));
    }

    public static void main(String[] args) {
//        testFetchPassage();
    }
}
