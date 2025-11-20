import { Component, ChangeDetectorRef } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-suggester',
  standalone: true,
  imports: [
    TranslateModule, 
    FormsModule,
    CommonModule
  ],
  templateUrl: './book-suggester.component.html',
  styleUrls: ['./book-suggester.component.css']
})


export class BookSuggesterComponent {
  maxChars = 500;

  preferences: string = "";
  book: Book | null = null;

  constructor(private bookService: BookService,
    private cdr: ChangeDetectorRef) {}

  adjustHeight(el: HTMLTextAreaElement) {    

    if (this.preferences.length > this.maxChars){
      this.preferences = this.preferences.substring(0, this.maxChars);
    }

    el.style.height = "auto";
    el.style.height = el.scrollHeight + "px";
  }

  onSuggest() {
    if (!this.preferences.trim()) {
      return; 
    }

    this.bookService.suggestBook(this.preferences).subscribe({
      next: (result) => {
        this.book = result;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Error trying to find the book:", err);
      }
    });
  }
}