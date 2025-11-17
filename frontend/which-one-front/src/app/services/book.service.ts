import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8080/api/books/suggest';

  constructor(private http: HttpClient) {}

  suggestBook(preferences: string): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, preferences, {
      headers: { 'Content-Type': 'text/plain' }
    });
  }
}
