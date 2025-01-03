import { TestBed } from '@angular/core/testing';

import { HeaderbarService } from './headerbar.service';

describe('HeaderbarService', () => {
  let service: HeaderbarService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeaderbarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
