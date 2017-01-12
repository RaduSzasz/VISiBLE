var dummy = {};

dummy['upload'] = {
  "jar": "MaxOfFour.jar",
  "data": [
    {
      "class": "MaxOfFour",
      "methods": [
        {
          "name": "main",
          "numArgs": 1,
          "signature": "public static void MaxOfFour.main(java.lang.String[])"
        },
        {
          "name": "symVis",
          "numArgs": 4,
          "signature": "private static java.lang.String MaxOfFour.symVis(int,int,int,int)"
        }
      ]
    }
  ]
};

dummy['symbolicmethod'] = {
  "parent": null,
  "ifPC": "x_1_SYMINT>=y_2_SYMINT",
  "elsePC": "x_1_SYMINT<y_2_SYMINT",
  "concreteValues": {
    "x": 0,
    "y": 0,
    "z": 0,
    "t": 0,
  },
  "type": "normal",
  "id": 0
};

dummy['step0'] = {
  "parent": 0,
  "ifPC": "y_2_SYMINT>=z_3_SYMINT",
  "elsePC": "y_2_SYMINT<z_3_SYMINT",
  "concreteValues": {
    "x": 1,
    "y": 1,
    "z": 1,
    "t": 1,
  },
  "type": "normal",
  "id": 1
}

dummy['step1'] = {
  "parent":1,
  "ifPC":"x_1_SYMINT>=t_4_SYMINT",
  "elsePC":"x_1_SYMINT<t_4_SYMINT",
  "concreteValues": {
    "x": 2,
    "y": 2,
    "z": 2,
    "t": 2,
  },
  "type":"normal",
  "id":2
}

dummy['step2'] = {
  "parent":2,
  "ifPC":null,
  "elsePC":null,
  "concreteValues": {
    "x": 3,
    "y": 3,
    "z": 3,
    "t": 3,
  },
  "type":"leaf",
  "id":3
}

module.exports = dummy;
