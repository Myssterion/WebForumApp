import { TestBed } from '@angular/core/testing';

import { ModeratorGuardService } from './moderator-guard.service';

describe('ModeratorGuardService', () => {
  let service: ModeratorGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModeratorGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
