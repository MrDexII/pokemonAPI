package com.andrzej.RESTfullPokemonAPI.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PokemonPageable implements Pageable {

    private final int pageNumber;
    private final int pageSize;
    private final long offset;
    private final Sort sort;

    public PokemonPageable(Pageable pageable) {
        this.pageNumber = pageable.getPageNumber();
        this.pageSize = 10;
        this.offset = pageable.getOffset();
        this.sort = pageable.getSort();
    }

    private PokemonPageable(int pageNumber, int pageSize, long offset, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.offset = offset;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new PokemonPageable(this.getPageNumber() + 1, this.getPageSize(), this.getOffset(), this.getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return this.getPageNumber() == 0 ? this : new PokemonPageable(this.getPageNumber() - 1, this.getPageSize(), this.getOffset(), this.getSort());
    }

    @Override
    public Pageable first() {
        return new PokemonPageable(0, this.getPageSize(), this.getOffset(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.getPageNumber() > 0;
    }
}
