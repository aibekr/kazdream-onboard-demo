package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.AbstractDto;
import kz.kazdream.demo.onboard.db.entity.AbstractEntity;

import java.util.List;

interface Mapper<E extends AbstractEntity, D extends AbstractDto> {
    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> entityDto);
}
