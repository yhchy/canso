<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <MyDivider />
    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="post">
        <template #tab>
          <span>
            <book-two-tone />
            文章
          </span>
        </template>
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="user">
        <template #tab>
          <span>
            <smile-two-tone />
            用户
          </span>
        </template>
        <UserList :user-list="userList" />
      </a-tab-pane>
      <a-tab-pane key="picture">
        <template #tab>
          <span>
            <picture-two-tone />
            图片
          </span>
        </template>
        <PictureList />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  SmileTwoTone,
  BookTwoTone,
  PictureTwoTone,
} from "@ant-design/icons-vue";
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import myAxios from "@/plugins/myAxios";

const postList = ref([]);
const userList = ref([]);
// myAxios.get("/post/get/vo?id=" + "1689987839645515778").then((res) => {
//   console.log(res);
//   ref1.value = res;
// });

myAxios.post("/post/list/page/vo", {}).then((res: any) => {
  console.log(res);
  postList.value = res.records;
});

myAxios.post("/user/list/page/vo", {}).then((res: any) => {
  console.log(res);
  userList.value = res.records;
});

const router = useRouter();
const route = useRoute();

const activeKey = ref(route.params.category);

const initSearchParams = {
  text: "",
  pageNum: 1,
  pageSize: 10,
};

const searchParams = ref(initSearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});
const onSearch = (value: string) => {
  router.push({
    query: searchParams.value,
  });
};

const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
