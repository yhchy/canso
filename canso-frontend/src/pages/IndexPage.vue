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
        <PictureList :picture-list="pictureList" />
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
const pictureList = ref([]);

const router = useRouter();
const route = useRoute();

const activeKey = ref(route.params.category);

const initSearchParams = {
  text: route.query.text,
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

const loadData = (params: any) => {
  const query = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("/search/all", query).then((res: any) => {
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
  });
};

const loadDataOld = (params: any) => {
  const postQuery = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("/post/list/page/vo", postQuery).then((res: any) => {
    postList.value = res.records;
  });
  const userQuery = {
    ...params,
    userName: params.text,
  };

  myAxios.post("/user/list/page/vo", userQuery).then((res: any) => {
    userList.value = res.records;
  });
  const pictureQuery = {
    ...params,
    searchText: params.text,
  };

  myAxios.post("/picture/list/page/vo", pictureQuery).then((res: any) => {
    pictureList.value = res.records;
  });
};

loadData(initSearchParams);

const onSearch = (value: string) => {
  console.log(value);
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};

const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
