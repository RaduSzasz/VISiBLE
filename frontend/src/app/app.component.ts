import { Component } from '@angular/core';

@Component({
  selector: 'my-app',
  template: `<h1>Setting up {{name}}</h1>`,
})
export class AppComponent  { name = 'Testing'; }
