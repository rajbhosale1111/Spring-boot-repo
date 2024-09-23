<template>
  <div class="home" style="text-align: center">
    <img alt="Aman logo" src="../assets/logo.png" />
    <HelloPage msg="Welcome to Reproduct" />
    <button class="btn btn-danger mx-2" @click="healthCheck()">
      API Health Check
    </button>
    <div>API response is, {{ newMsg }}</div>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloPage from "@/components/HelloPage.vue";
// import axios from "axios";
import mdtiApi from "@/api";

export default {
  name: "HomeView",
  components: {
    HelloPage,
  },
  data() {
    return {
      newMsg: {},
    };
  },
  methods: {
    async healthCheck() {
      const response = await mdtiApi.checkApiHealth();
      this.newMsg = response;
      /*
      axios.get("http://localhost:8088/health-check").then((response) => {
        this.newMsg = response.data;
        console.log(response);
      });
      */
    },
  },
};
</script>
