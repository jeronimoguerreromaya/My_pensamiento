package com.mypensamiento.mypensamiento.thought;

import com.mypensamiento.mypensamiento.application.dto.request.CreateThoughtRequest;
import com.mypensamiento.mypensamiento.application.usecase.thought.CreateThoughtUseCase;
import com.mypensamiento.mypensamiento.domain.model.Thought;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateThoughtUseCaseTest {

    @Mock
    private ThoughtRepository thoughtRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateThoughtUseCase createThoughtUseCase;

    @Test
    void createThought_whenThoughtIsCreated_shouldCreateSuccessfully(){
        // Arrange
        Long id= 1l;

        CreateThoughtRequest request = new CreateThoughtRequest(
                "This is a thought"
        );
        when(userRepository.existsById(id)).thenReturn(true);

        // Act
        createThoughtUseCase.execute(request,id);

        //Assert
        ArgumentCaptor<Thought> captor = ArgumentCaptor.forClass(Thought.class);
        verify(thoughtRepository).save(captor.capture());
        Thought thought = captor.getValue();

        assertEquals(id, thought.getUsers_id());
        assertEquals(request.content(), thought.getThought_text());
        assertNotNull( thought.getCreated_at());
    }

}
