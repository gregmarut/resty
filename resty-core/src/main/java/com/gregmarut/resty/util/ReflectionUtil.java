package com.gregmarut.resty.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReflectionUtil
{
	/**
	 * Extracts the the first generic class from a method's return type if they are defined
	 *
	 * @param method
	 * @return
	 */
	public static Optional<Class<?>> extractFirstGenericClass(final Method method)
	{
		return extractGenericClasses(method)
			.stream()
			.findFirst();
	}
	
	/**
	 * Extracts the list of generic classes from a method's return type if they are defined
	 *
	 * @param method
	 * @return
	 */
	public static List<Class<?>> extractGenericClasses(final Method method)
	{
		// hold the list of generic classes for the object defined in this field
		final List<Class<?>> generics = new ArrayList<Class<?>>();
		
		// get the generic type for this collection
		final Type genericType = method.getGenericReturnType();
		
		// make sure there is a generic type assigned for this collection
		if (genericType instanceof ParameterizedType)
		{
			// attempt to extract the generic from this collection
			ParameterizedType parameterizedTypes = (ParameterizedType) genericType;
			
			// for each of the actual type arguments
			for (Type type : parameterizedTypes.getActualTypeArguments())
			{
				final Class<?> clazz = (Class<?>) type;
				generics.add(clazz);
			}
		}
		
		return generics;
	}
}
