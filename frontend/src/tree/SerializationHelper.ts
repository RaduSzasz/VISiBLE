export class SerializationHelper {
  static toInstance<T>(obj: T, json: string) : T {
    var jsonObj = JSON.parse(json);
    for (var propName in jsonObj) {
      obj[propName] = jsonObj[propName]
    }
    return obj;
  }
}
