package com.example.todoappmicroserviceuserapi.response;

import lombok.Builder;

@Builder
public record WebResponse<T extends MResponse>(T data) {

}
