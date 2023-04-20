package com.example.todoappmicroserviceuserapi.mapper;

import com.example.todoappmicroserviceuserapi.exception.ItemNotFoundException;
import lombok.NonNull;

import java.util.List;

public interface BaseMapper<T,D ,CD, UD> extends Mapper {
    T fromCreateDTO(@NonNull CD dto) throws ItemNotFoundException;



    T fromUpdateDTO(@NonNull UD dto);

    D toDTO(@NonNull T domain);

    List<D> toDTOs(@NonNull List<T> domain);
}
