import { Component }         from '@angular/core';
import { UploadComponent }   from '../upload/upload.component';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})

export class AppComponent {
  title = 'VISiBLE';
  uploadFile: any;
  hasBaseDropZoneOver: boolean = false;
  filename: string;
  uploadSuccess: boolean = false;
  options: Object = {
    url: 'http://localhost:5000/upload'
  };

  handleUpload(data): void {
    if (data && data.response) {
      this.filename = data.originalName;
      data = JSON.parse(data.response);
      this.uploadSuccess = true;
      this.uploadFile = data;
    }
  }

  fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }
}
