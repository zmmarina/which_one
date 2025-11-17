import { Component } from '@angular/core';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-suggester',
  templateUrl: './book-suggester.component.html',
  styleUrls: ['./book-suggester.component.css']
})
export class BookSuggesterComponent {

  preferences: string = "";
  book: Book | null = null;

  constructor(private bookService: BookService) {}

  onSuggest() {
    if (!this.preferences.trim()) {
      return; 
    }

    this.bookService.suggestBook(this.preferences).subscribe({
      next: (result) => {
        this.book = result;
      },
      error: (err) => {
        console.error("Error trying to find the book:", err);
      }
    });
  }
}