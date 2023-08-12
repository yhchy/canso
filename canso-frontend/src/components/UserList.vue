<template>
  <a-list item-layout="horizontal" :data-source="props.userList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-card hoverable style="width: 240px">
          <template #cover>
            <img alt="example" :src="baiyuekui" style="width: 200px" />
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
