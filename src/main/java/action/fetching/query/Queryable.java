package main.java.action.fetching.query;

import java.util.List;

import main.java.action.fetching.exceptions.WrongQueryException;

public interface Queryable<T> {

	public List<T> where(String expression);

	public List<T> where(String field, String relation, String value);

	List<T> where(String field, String relation, String value, QueryMode mode) throws WrongQueryException;
}
