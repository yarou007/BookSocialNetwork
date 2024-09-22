package com.yarou.book.feedback;


import com.yarou.book.book.Book;
import com.yarou.book.book.BookRepository;
import com.yarou.book.common.PageResponse;
import com.yarou.book.exception.OperationNotPermittedException;
import com.yarou.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor

public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRespository feedbackRespository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(()->new EntityNotFoundException("No book found with id: "+request.bookId()));
        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give a feedback for archived or not shareable book");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(),user.getId())){
            // throw exception
            throw new OperationNotPermittedException("You can't give feedback to your own book ");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRespository.save(feedback).getId();
    }

    public PageResponse<FeedBackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {

        Pageable pageable = PageRequest.of(page,size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRespository.findAllBybookId(bookId,pageable);
        List<FeedBackResponse> feedBackResponses = feedbacks.stream()
                .map(f->feedbackMapper.toFeedbackResponse(f,user.getId()))
                .toList();
            return new PageResponse<>(
                    feedBackResponses,
                    feedbacks.getNumber(),
                    feedbacks.getSize(),
                    feedbacks.getTotalElements(),
                    feedbacks.getTotalPages(),
                    feedbacks.isFirst(),
                    feedbacks.isLast()
            );
    }
}
