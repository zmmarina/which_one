import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookSuggester } from './book-suggester.component';

describe('BookSuggester', () => {
  let component: BookSuggester;
  let fixture: ComponentFixture<BookSuggester>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookSuggester]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookSuggester);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
