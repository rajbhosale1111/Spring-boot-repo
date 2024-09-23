// as it is funny gen IDs using b - https://gist.github.com/jed/982883
const uuid = (a) =>
  a // eslint-disable-next-line no-bitwise
    ? (a ^ ((Math.random() * 16) >> (a / 4))).toString(16)
    : ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, uuid);

const formatDateString = (date = "") => {
  let dateString = "";
  if (date !== "") {
    dateString = new Date(date)?.toISOString()?.split("T")[0];
  } else {
    dateString = new Date()?.toISOString()?.split("T")[0];
  }
  return dateString;
};
export function removeKeys(data, deleteKeys) {
  // There is nothing to be done if `data` is not an object,
  // but for example "user01" or "MALE".
  if (typeof data !== "object") return;
  if (!data) return; // null object
  Object.keys(data).forEach((key) => {
    if (deleteKeys.includes(key)) {
      delete data[key];
    } else {
      // If the key is not deleted from the current `data` object,
      // the value should be check for black-listed keys.
      removeKeys(data[key], deleteKeys);
    }
  });
}
const setCssVariable = (config) => {
  const root = document.querySelector(":root");
  if (config.PRIMARY_COLOR) {
    // root.style.setProperty('--primary-color', config.BRAND_COLOR);
    root.style.setProperty("--primary-color", config.PRIMARY_COLOR);
  }
  if (config.BUTTON_TEXT) {
    root.style.setProperty(
      "--overtext",
      config.BUTTON_TEXT === "light" ? "#ffffff" : "#303335"
    );
  }
};
export { uuid, formatDateString, setCssVariable };
