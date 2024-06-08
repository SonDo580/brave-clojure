// Implement sequence operations on linked list
// const first = (list) => list.value;

// const rest = (list) => list.next;

// const cons = (newValue, list) => ({
//   value: newValue,
//   next: list,
// });

// Implement sequence operations on array
// const first = (array) => array[0];

// const rest = (array) => {
//   const sliced = array.slice(1);
//   return sliced.length === 0 ? null : sliced;
// };

// const cons = (newValue, array) => [newValue].concat(array);

const map = (list, transform) => {
  if (list === null) {
    return null;
  }
  return cons(transform(first(list)), map(rest(list), transform));
};

// const node3 = {
//   value: "last",
//   next: null,
// };
// const node2 = {
//   value: "middle",
//   next: node3,
// };
// const node1 = {
//   value: "first",
//   next: node2,
// };
// const mappedList = map(node1, (val) => `${val} mapped!`)
// console.log(JSON.stringify(mappedList, null, 2));

// const array = ["Transylvania", "Forks, WA"];
// const mappedArray = map(array, (val) => `${val} mapped!`);
// console.log(JSON.stringify(mappedArray, null, 2));
