package com.example.todoappmicroservicetodoapi.response;

import lombok.Builder;

@Builder
public record WebResponse<T extends MResponse>(T data) {

}
