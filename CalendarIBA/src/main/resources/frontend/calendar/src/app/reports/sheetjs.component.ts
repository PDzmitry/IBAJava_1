import * as XLSX from 'xlsx';
import {Component} from '@angular/core';

type AOA = any[][];

@Component({
    selector: 'app-sheetjs',
    template: `
	<input type="file" (change)="onFileChange($event)" multiple="false" />
	<table class="table table-active table-bordered" style="width: 100%">
		<tr *ngFor="let row of data">
			<td *ngFor="let val of row">
				{{val}}
			</td>
		</tr>
	</table>
	<button (click)="export()">Export!</button>
	`,
    styleUrls: ['./reports.component.scss']
})

export class SheetJSComponent {
    data: AOA = [ [1, 2], [3, 4] ];
    wopts: XLSX.WritingOptions = { bookType: 'xlsx', type: 'array' };
    fileName = 'SheetJS.xlsx';

    onFileChange(evt: any) {
        /* wire up file reader */
        const target: DataTransfer = (evt.target) as DataTransfer;
        if (target.files.length !== 1) { throw new Error('Cannot use multiple files'); }
        const reader: FileReader = new FileReader();
        reader.onload = (e: any) => {
            /* read workbook */
            const bstr: string = e.target.result;
            const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

            /* grab first sheet */
            const wsname: string = wb.SheetNames[0];
            const ws: XLSX.WorkSheet = wb.Sheets[wsname];

            /* save data */
            this.data = (XLSX.utils.sheet_to_json(ws, {header: 1})) as AOA;
        };
        reader.readAsBinaryString(target.files[0]);
    }

    export(): void {
        /* generate worksheet */
        const ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(this.data);

        /* generate workbook and add the worksheet */
        const wb: XLSX.WorkBook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

        /* save to file */
        XLSX.writeFile(wb, this.fileName);
    }
}
