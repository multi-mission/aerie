/**
 * Copyright 2018, by the California Institute of Technology. ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
 * Any commercial use must be negotiated with the Office of Technology Transfer at the California Institute of Technology.
 * This software may be subject to U.S. export control laws and regulations.
 * By accepting this document, the user agrees to comply with all applicable U.S. export laws and regulations.
 * User has the responsibility to obtain export licenses, or other export authority as may be required
 * before exporting such information to foreign countries or providing access to foreign persons
 */

import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
} from '@angular/core';

import { Store } from '@ngrx/store';

import {
  combineLatest,
  Observable,
  Subject,
} from 'rxjs';

import {
  map,
  takeUntil,
  tap,
} from 'rxjs/operators';

import * as fromLayout from './../../reducers/layout';
import * as fromSourceExplorer from './../../reducers/source-explorer';
import * as fromTimeline from './../../reducers/timeline';

import * as dialogActions from './../../actions/dialog';
import * as layoutActions from './../../actions/layout';
import * as timelineActions from './../../actions/timeline';

import {
  RavenTimeRange,
} from './../../shared/models';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-root',
  styleUrls: ['./app.component.css'],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnDestroy {
  loading$: Observable<boolean>;
  mode: string;

  private ngUnsubscribe: Subject<{}> = new Subject();

  constructor(
    private changeDetector: ChangeDetectorRef,
    private store: Store<fromSourceExplorer.SourceExplorerState>,
  ) {
    // Combine all fetch pending observables for use in progress bar.
    this.loading$ = combineLatest(
      this.store.select(fromSourceExplorer.getPending),
      this.store.select(fromTimeline.getPending),
    ).pipe(
      map(loading => loading[0] || loading[1]),
      tap(() => this.markForCheck()),
    );

    // Layout mode.
    this.store.select(fromLayout.getMode).pipe(
      takeUntil(this.ngUnsubscribe),
    ).subscribe(mode => {
      this.mode = mode;
      this.markForCheck();
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  /**
   * Helper. Marks this component for change detection check,
   * and then detects changes on the next tick.
   *
   * TODO: Find out how we can remove this.
   */
  markForCheck() {
    this.changeDetector.markForCheck();
    setTimeout(() => this.changeDetector.detectChanges());
  }

  onPanLeft() {
    this.store.dispatch(new timelineActions.PanLeftViewTimeRange());
  }

  onPanRight() {
    this.store.dispatch(new timelineActions.PanRightViewTimeRange());
  }

  onPanTo(viewTimeRange: RavenTimeRange) {
    this.store.dispatch(new timelineActions.UpdateViewTimeRange(viewTimeRange));
  }

  onReset() {
    this.store.dispatch(new timelineActions.ResetViewTimeRange());
  }

  onZoomIn() {
    this.store.dispatch(new timelineActions.ZoomInViewTimeRange());
  }

  onZoomOut() {
    this.store.dispatch(new timelineActions.ZoomOutViewTimeRange());
  }

  toggleDetailsPanel() {
    this.store.dispatch(new layoutActions.ToggleDetailsPanel());
  }

  toggleEpochsDrawer() {
    this.store.dispatch(new layoutActions.ToggleEpochsDrawer());
  }

  toggleGlobalSettingsDrawer() {
    this.store.dispatch(new layoutActions.ToggleGlobalSettingsDrawer());
  }

  toggleLeftPanel() {
    this.store.dispatch(new layoutActions.ToggleLeftPanel());
  }

  toggleOutputDrawer() {
    this.store.dispatch(new layoutActions.ToggleOutputDrawer());
  }

  toggleRightPanel() {
    this.store.dispatch(new layoutActions.ToggleRightPanel());
  }

  toggleShareableLinkDialog() {
    this.store.dispatch(new dialogActions.OpenShareableLinkDialog('600px'));
  }

  toggleSouthBandsPanel() {
    this.store.dispatch(new layoutActions.ToggleSouthBandsPanel());
  }

  toggleTimeCursorDrawer() {
    this.store.dispatch(new layoutActions.ToggleTimeCursorDrawer());
  }
}
