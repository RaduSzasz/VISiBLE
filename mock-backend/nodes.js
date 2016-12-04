module.exports = [
  {
    "id": 0,
    "parent_": -1,
    "children": [1, 2],
    "pc": "true"
  },
  {
    "id": 1,
    "parent_": 0,
    "children": [3, 4],
    "pc": "x >= y"
  },
  {
    "id": 2,
    "parent_": 0,
    "children": [5, 6],
    "pc": "x < y"
  },
  {
    "id": 3,
    "parent_": 1,
    "children": [],
    "pc": "x >= z"
  },
  {
    "id": 4,
    "parent_": 1,
    "children": [],
    "pc": "x < z"
  },
  {
    "id": 5,
    "parent_": 2,
    "children": [],
    "pc": "y >= z"
  },
  {
    "id": 6,
    "parent_": 2,
    "children": [],
    "pc": "y < z"
  }
] 