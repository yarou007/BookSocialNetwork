package com.yarou.book.book;


import com.yarou.book.common.PageResponse;
import com.yarou.book.exception.OperationNotPermittedException;
import com.yarou.book.history.BookTransactionHistory;
import com.yarou.book.history.BookTransactionHistoryRepository;
import com.yarou.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.yarou.book.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {


     private final BookMapper bookMapper;
     private final BookRepository bookRepository;
     private final BookTransactionHistoryRepository transactionHistoryRepository;
    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal(); // return the connected user
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();

    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow( ()-> new EntityNotFoundException("Book was not found by the id:"+bookId));
    }


    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books=bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books =bookRepository.findAll(withOwnerId(user.getId()),pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id: "+bookId));
        User user = (User) connectedUser.getPrincipal();
        // check el book owner id idha ken moush nafsou l user li yheb yaamel l update mteeou behs nthrowi expcetion nkharjou
        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            // throw exception
            throw new OperationNotPermittedException("You can not update books status cause you don't own it");
        }
        // if true besh trodou false if false trodou true -> update status
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id: "+bookId));
        User user = (User) connectedUser.getPrincipal();
        // check el book owner id idha ken moush nafsou l user li yheb yaamel l update mteeou behs nthrowi expcetion nkharjou
        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            // throw exception
            throw new OperationNotPermittedException("You can not archive books status cause you don't own it");
        }
        // if true besh trodou false if false trodou true -> update status
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id: "+bookId));

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("Requested book can not be borrowed right now ( not archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(),user.getId())){
            // throw exception
            throw new OperationNotPermittedException("You can't borrow your own book");
        }
        // flag
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId,user.getId());
      if(isAlreadyBorrowed)
      {
          throw new OperationNotPermittedException("The requested book is already borrowed");
      }
      BookTransactionHistory bookTransactionHistory =  BookTransactionHistory.builder()
              .user(user)
              .book(book)
              .returned(false)
              .returnApproved(false)
              .build();
            return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }
}
