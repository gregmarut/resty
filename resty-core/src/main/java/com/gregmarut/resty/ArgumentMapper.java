/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

import com.gregmarut.resty.annotation.Body;
import com.gregmarut.resty.annotation.Header;
import com.gregmarut.resty.annotation.Parameter;
import com.gregmarut.resty.exception.ConflictingAnnotationException;
import com.gregmarut.resty.exception.DuplicateParameterNameException;
import com.gregmarut.resty.exception.MultipleBodyException;
import com.gregmarut.resty.exception.ParameterNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for the mapping of arguments to their name
 *
 * @author Greg Marut
 */
public class ArgumentMapper
{
	// holds the map of body and parameter arguments
	private final Map<String, Object> bodyArguments;
	private final Map<String, Object> parameterArguments;
	private final Map<String, Object> headerArguments;
	
	public ArgumentMapper(final Method method, final Object[] args)
	{
		bodyArguments = new HashMap<String, Object>();
		parameterArguments = new HashMap<String, Object>();
		headerArguments = new HashMap<String, Object>();
		
		// map the parameters
		mapParameters(method, args);
	}
	
	private void mapParameters(final Method method, final Object[] args)
	{
		// make sure the arguments array is not null
		if (null != args)
		{
			// get the matrix of annotations
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			
			// for every argument
			for (int i = 0; i < args.length; i++)
			{
				Body b = null;
				Parameter p = null;
				Header h = null;
				
				for (int n = 0; n < parameterAnnotations[i].length; n++)
				{
					// check to see if this annotation is an body
					if (parameterAnnotations[i][n].annotationType().equals(Body.class))
					{
						b = (Body) parameterAnnotations[i][n];
					}
					
					// check to see if this annotation is a parameter
					else if (parameterAnnotations[i][n].annotationType().equals(Parameter.class))
					{
						p = (Parameter) parameterAnnotations[i][n];
					}
					
					// check to see if this annotation is a header
					else if (parameterAnnotations[i][n].annotationType().equals(Header.class))
					{
						h = (Header) parameterAnnotations[i][n];
					}
				}
				
				// check to see if both the parameter and body annotations were set
				if (null != b && null != p)
				{
					throw new ConflictingAnnotationException("A parameter is annotated with both "
						+ Body.class.getName() + " and " + Parameter.class.getName() + ". Only 1 is allowed.");
				}
				// check to see if both the header and body annotations were set
				else if (null != b && null != h)
				{
					throw new ConflictingAnnotationException("A parameter is annotated with both "
						+ Body.class.getName() + " and " + Header.class.getName() + ". Only 1 is allowed.");
				}
				// check to see if both the parameter and header annotations were set
				else if (null != p && null != h)
				{
					throw new ConflictingAnnotationException("A parameter is annotated with both "
						+ Parameter.class.getName() + " and " + Header.class.getName() + ". Only 1 is allowed.");
				}
				
				// check to see if both body and parameter annotations are null
				else if (null == b && null == p && null == h)
				{
					// treat parameters without an annotation as an body
					addToMap(bodyArguments, args[i].toString(), args[i]);
				}
				
				// only body is set
				else if (null != b)
				{
					addToMap(bodyArguments, b.value(), args[i]);
				}
				
				// only parameter is set
				else if (null != p)
				{
					addToMap(parameterArguments, p.value(), args[i]);
				}
				
				// only header is set
				else if (null != h)
				{
					addToMap(headerArguments, h.value(), args[i]);
				}
			}
			
			// make sure there are not multiple bodies
			if (bodyArguments.size() > 1)
			{
				throw new MultipleBodyException(
					"Only 1 body is allowed per method. Parameters that do not contain either " + Body.class.getName()
						+ " or " + Parameter.class.getName() + " are treated as an body.");
			}
		}
	}
	
	private void addToMap(final Map<String, Object> map, final String key, final Object value)
	{
		// make sure this body does not already exist in either
		if (!bodyArguments.containsKey(key) && !parameterArguments.containsKey(key))
		{
			// add the object to the map
			map.put(key, value);
		}
		else
		{
			throw new DuplicateParameterNameException("A parameter already exists with this name: " + key);
		}
	}
	
	public Object getArgument(final String name, final ArgumentType argumentType)
	{
		switch (argumentType)
		{
			case BODY:
				return getBodyArgument(name);
			case HEADER:
				return getHeaderArgument(name);
			case PARAMETER:
				return getParameterArgument(name);
			default:
				throw new IllegalArgumentException("argumentType is not valid");
		}
	}
	
	public Object getBodyArgument()
	{
		Collection<Object> entities = bodyArguments.values();
		
		// check to see if there are entities
		if (!entities.isEmpty())
		{
			return entities.toArray()[0];
		}
		else
		{
			return null;
		}
	}
	
	public Object getBodyArgument(final String name)
	{
		// check to see if the body contains it
		if (bodyArguments.containsKey(name))
		{
			return bodyArguments.get(name);
		}
		else
		{
			throw new ParameterNotFoundException("Parameter with the name \"" + name
				+ "\" was not found. Perhaps it is missing an annotation?");
		}
	}
	
	public Object getParameterArgument(final String name)
	{
		if (parameterArguments.containsKey(name))
		{
			return parameterArguments.get(name);
		}
		else
		{
			throw new ParameterNotFoundException("Parameter with the name \"" + name
				+ "\" was not found. Perhaps it is missing an annotation?");
		}
	}
	
	public Object getHeaderArgument(final String name)
	{
		if (headerArguments.containsKey(name))
		{
			return headerArguments.get(name);
		}
		else
		{
			throw new ParameterNotFoundException("Parameter with the name \"" + name
				+ "\" was not found. Perhaps it is missing an annotation?");
		}
	}
}
