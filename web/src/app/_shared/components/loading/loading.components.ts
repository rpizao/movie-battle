
import { Component, OnDestroy, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'loading',
	template: `		<div style="display: {{shown ? 'block' : 'none'}} !important" id="pause" class="d-flex align-items-center justify-content-center">
									<div id="spinner"></div>
								</div>`,
	styleUrls: ['./loading.scss']
})

export class LoadingComponent {
  constructor(private activeModal: NgbActiveModal) { }

  shown: boolean = false;

  close(){
    this.activeModal.close();
  }
}
