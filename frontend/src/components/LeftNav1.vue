<template>
  <div class="rpdb-left">
    <div class="rpdb-ln-acct">
      <div class="user-tp-icon-wrap">
        <div class="user-tp-letters">{{ userName.charAt(0) }}</div>
      </div>
      <div></div>
      <div class="rpdb-ln-acct-name">{{ userName }}</div>
    </div>

    <div class="rpdb-ln-itm rln-1 active">
      <div class="rpdb-ln-link">
        <img
          src="../assets/img/i/svgs/regular/user.svg"
          class="myic ic-gray2"
        />Company Info
      </div>
    </div>

    <div class="rpdb-ln-itm rln-2">
      <div class="rpdb-ln-link">
        <img
          src="../assets/img/i/svgs/solid/users.svg"
          class="myic ic-gray2"
        />User Management
      </div>
    </div>

    <div class="rpdb-ln-itm rln-3">
      <div class="rpdb-ln-link">
        <img
          src="../assets/img/i/svgs/regular/wallet.svg"
          class="myic ic-gray2"
        />Billing
      </div>
    </div>

    <div class="rpdb-ln-itm rln-4">
      <div class="rpdb-ln-link">
        <img
          src="../assets/img/i/svgs/regular/code.svg"
          class="myic ic-gray2"
        />Pixel
      </div>
    </div>
  </div>
  <!-- left end -->
</template>

<script>
import mdtiApi from "@/api";

export default {
  name: "LeftNav1",
  props: {
    msg: String,
  },
  data() {
    return {
      userName: "", // Initialize userName
    };
  },
  created() {
    // Assuming the JSON structure is an array of objects
    const userInfo = mdtiApi.fetchUserInfo();
    this.userName = userInfo?.userName || ""; // Set the userName from JSON data
  },
};
</script>
