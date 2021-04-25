package com.gregmarut.resty.util;

import com.gregmarut.resty.async.Async;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReturnType
{
	private final Type type;
	
	public ReturnType(final Type type)
	{
		this.type = type;
	}
	
	public ReturnType(final Method method)
	{
		this(method.getGenericReturnType());
	}
	
	public boolean isAsync()
	{
		return getActualClass().equals(Async.class);
	}
	
	public boolean isList()
	{
		return getActualClass().equals(List.class);
	}
	
	public boolean isArray()
	{
		return type.toString().endsWith("[]");
	}
	
	public Class<?> getActualClass()
	{
		if (getTypeAsClass().isPresent())
		{
			return getTypeAsClass().get();
		}
		else if (getTypeAsParameterizedType().isPresent())
		{
			return (Class<?>) getTypeAsParameterizedType().get().getRawType();
		}
		else if (isArray())
		{
			try
			{
				return Class.forName(type.toString().substring(0, type.toString().length() - 2));
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public Optional<ReturnType> getGenericReturnType()
	{
		return getTypeAsParameterizedType()
			.flatMap(this::getFirstGeneric)
			.map(ReturnType::new);
	}
	
	private Optional<Class<?>> getTypeAsClass()
	{
		return Optional.of(type)
			.filter(t -> t instanceof Class)
			.map(t -> (Class<?>) t);
	}
	
	private Optional<ParameterizedType> getTypeAsParameterizedType()
	{
		return Optional.of(type)
			.filter(t -> t instanceof ParameterizedType)
			.map(t -> (ParameterizedType) t);
	}
	
	/**
	 * Given a parameterizedType, this returns the first defined generic (if any)
	 *
	 * @param parameterizedType
	 * @return
	 */
	private Optional<Type> getFirstGeneric(final ParameterizedType parameterizedType)
	{
		return Optional.ofNullable(parameterizedType.getActualTypeArguments())
			.flatMap(types -> Arrays.stream(types).findFirst());
	}
}
