package com.example.todoappmicroservicetodoapi.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateDTO {
    @NotBlank(message = "Todo id is mandatory")
    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    private Long updatedBy;
    private boolean completed;
}
