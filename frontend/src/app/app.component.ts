import { Component }       from '@angular/core';
import { UploadComponent } from '../upload/upload.component.ts';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html',
  directives: [ UploadComponent ]
})

export class AppComponent {
  title = 'VISiBLE';
}
