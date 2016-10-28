import { Tree } from './tree';

export var TREE: Tree = 
{
  "name": "Top Level",
  "mother": "null",
  "children": [
    {
      "name": "Level 2: A",
      "mother": "Top Level",
      "children": [
        {
          "name": "Son of A",
          "mother": "Level 2: A",
          "children": []
        },
        {
          "name": "Daughter of A",
          "mother": "Level 2: A",
          "children": []
        }
      ]
    },
    {
      "name": "Level 2: B",
      "mother": "Top Level",
      "children": []
    }
  ]
};

