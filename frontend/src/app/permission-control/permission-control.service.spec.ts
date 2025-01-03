import { TestBed } from '@angular/core/testing';

import { PermissionControlService } from './permission-control.service';

describe('PermissionControlService', () => {
  let service: PermissionControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PermissionControlService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
