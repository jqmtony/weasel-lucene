package com.weasel.lucene.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.search.Sort;

/**
 * @author Dylan
 * @time 2013-7-4
 */
public class Page<T> {

	/**
     * witch page we now hold
     */
    private int pageNumber = 0;

    /**
     * how many records per page
     */
    private int pageSize = 20;

    /**
     * 
     */
    private Sort sort; // for sort

    /**
     * how many records we can find from database
     */
    private long total;

    /**
     * the result we get
     */
    private List<T> result = new ArrayList<T>();
    
    public Page(){
            this(0,20);
    }
    public Page(int page, int size) {
            this(page, size, null);
    }
    public Page(int page, int size, Sort sort) {
            if (page < 0)
                    throw new RuntimeException("page must more than or equal 0");
            if (size <= 0)
                    throw new RuntimeException("size must more than 0");
            this.pageNumber = page;
            this.pageSize = size;
            this.sort = sort;
    }

    /**
     * @return
     */
    public int getTotalPage() {
            return (int) Math.ceil((double) total / (double) pageSize);
    }

    /**
     * @return
     */
    public boolean hasNexPage() {
            return (getPageNumber() + 1) * getPageSize() < total;
    }

    /**
     * @return
     */
    public boolean hasPrePage() {
            return getPageNumber() > 0;
    }

    /**
     * @return
     */
    public boolean isLastPage() {
            return !hasNexPage();
    }

    /**
     * d
     * @return
     */
    public boolean isFirstPage() {
            return !hasPrePage();
    }
    
    public Page<T> nextPage(){
            if(hasNexPage()){
                    ++pageNumber;
            }
            return this;
    }
    public Page<T> prePage(){
            if(hasPrePage()){
                    --pageNumber;
            }
            return this;
    }

    /**
     * @return
     */
    public long getTotal() {
            return total;
    }

    /**
     * @param total
     */
    public Page<T> setTotal(long total) {
            this.total = total;
            return this;
    }

    /**
     * @return
     */
    public List<T> getResult() {
            return Collections.unmodifiableList(result);
    }

    /**
     * @param result
     */
    public Page<T> setResult(List<T> _result) {
            result = _result;
            return this;
    }

    /**
     *(non-Javadoc)
     * @see @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    public int getPageNumber() {
            return pageNumber;
    }

    /**
     *(non-Javadoc)
     * @see @see org.springframework.data.domain.Pageable#getPageSize()
     */
    public int getPageSize() {
            return pageSize;
    }

    /**
     *(non-Javadoc)
     * @see @see org.springframework.data.domain.Pageable#getOffset()
     */
    public int getOffset() {
            return pageNumber * pageSize;
    }

    /**
     *(non-Javadoc)
     * @see @see org.springframework.data.domain.Pageable#getSort()
     */
    public Sort getSort() {

            return sort;
    }
}
