package main.java.action.fetching.query;

import java.util.List;

public interface Queryable<T> {

	public List<T> where(String expression);
}
