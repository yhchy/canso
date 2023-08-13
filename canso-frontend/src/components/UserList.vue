<template>
  <a-list
    item-layout="horizontal"
    :data-source="props.userList"
    :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }"
  >
    <template #renderItem="{ item }">
      <a-list-item>
        <a-card hoverable>
          <template #cover>
            <img alt="example" :src="baiyuekui" />
          </template>
          <a-card-meta :title="item.userName">
            <template #description>{{ item.userProfile }}</template>
          </a-card-meta>
        </a-card>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
import baiyuekui from "@/assets/baiyuekui.png";

interface Prop {
  userList: any[];
}

const props = withDefaults(defineProps<Prop>(), {
  userList: () => [],
});
</script>

<style scoped></style>
