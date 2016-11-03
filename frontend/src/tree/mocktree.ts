import { Tree } from './tree';

export var TREE: Tree = 
{
  "index": 0,
  "data": "Condition A",
  "incoming": [], 
  "children": [
    {
      "index": 1,
      "data": "Condition B",
      "incoming": [0],
      "children": [
        {
          "index": 2,
          "data": "Something",
          "incoming": [1],
          "children": []
        },
        {
          "index": 3,
          "data": "Something else",
          "incoming": [1],
          "children": []
        }
      ]
    },
    {
      "index": 4,
      "data": "Also something",
      "incoming": [0],
      "children": []
    }
  ]
};


