package com.freight.dao;

import com.google.common.collect.Iterables;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * Created by toshikijahja on 7/29/17.
 */
public class BaseDao<T> {

    private static final String DATA = "data";
    private static final String SORT_FIELD = "sortField";

    private final SessionProvider sessionProvider;
    protected final Class clazz;

    protected BaseDao(final SessionProvider sessionProvider,
                      final Class clazz) {
        this.sessionProvider = sessionProvider;
        this.clazz = clazz;
    }

    public SessionProvider getSessionProvider() {
        return this.sessionProvider;
    }

    public enum Sort {
        ASC, DESC
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        final Query query = getSessionProvider().getSession().createQuery("FROM " + clazz.getName());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAllSorted(final String sortField, final Sort sort) {
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " ORDER BY " + sortField + " " + sort);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public T getById(final int id) {
        return (T) getSessionProvider().getSession().load(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public Optional<T> getByIdOptional(final int id) {
        final T result = (T) getSessionProvider().getSession().get(clazz, id);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @SuppressWarnings("unchecked")
    public List<T> getByIds(final List<Integer> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " WHERE id IN :ids");
        query.setParameterList("ids", ids);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected Optional<T> getByFieldOptional(final String field, final Object data) {
        requireNonNull(field);
        final List<T> results = getByField(field, data);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @SuppressWarnings("unchecked")
    protected List<T> getByField(final String field, final Object data) {
        requireNonNull(field);
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " WHERE " + field + " = :" + DATA);
        query.setParameter(DATA, data);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected Optional<T> getByFieldsOptional(final String whereQuery, final Map<String, Object> inputParam) {
        requireNonNull(whereQuery);
        requireNonNull(inputParam);
        final List<T> results = getByFields(whereQuery, inputParam);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @SuppressWarnings("unchecked")
    protected List<T> getByFields(final String whereQuery, final Map<String, Object> inputParam) {
        requireNonNull(whereQuery);
        requireNonNull(inputParam);
        if (inputParam.isEmpty()) {
            return emptyList();
        }
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " WHERE " + whereQuery);
        inputParam.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected List<T> getByFieldSorted(final String field,
                                       final Object data,
                                       final String sortField,
                                       final Sort sort) {
        requireNonNull(field);
        requireNonNull(sortField);
        requireNonNull(sort);
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " WHERE " + field + " = :" + DATA + " ORDER BY " + sortField + " " + sort);
        query.setParameter(DATA, data);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected List<T> getByFieldSortedAndPaginated(final String whereQuery,
                                                   final Map<String, Object> inputParam,
                                                   final String sortField,
                                                   final Sort sort,
                                                   final int start,
                                                   final int limit) {
        if (inputParam.isEmpty()) {
            return emptyList();
        }
        requireNonNull(whereQuery);
        requireNonNull(inputParam);
        requireNonNull(sortField);
        requireNonNull(sort);
        final Query query = getSessionProvider().getSession().createQuery(
                "FROM " + clazz.getName() + " WHERE " + whereQuery + " ORDER BY " + sortField + " " + sort);
        inputParam.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        query.setFirstResult(start);
        query.setMaxResults(limit);
        return query.list();
    }

    protected Optional<T> getFirst(final List<T> list) {
        return Optional.ofNullable(Iterables.getFirst(list, null));
    }

}
