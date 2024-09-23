import { shallowMount } from "@vue/test-utils";
import HelloPage from "@/components/HelloPage.vue";

describe("HelloPage.vue", () => {
  it("renders props.msg when passed", () => {
    const msg = "new message";
    const wrapper = shallowMount(HelloPage, {
      props: { msg },
    });
    expect(wrapper.text()).toMatch(msg);
  });
});
