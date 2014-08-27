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
package com.gregmarut.resty.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonSerializer implements Serializer
{
	private final Gson gson;
	
	public GsonSerializer()
	{
		gson = new Gson();
	}
	
	@Override
	public byte[] marshall(final Object object) throws SerializationException
	{
		return gson.toJson(object).getBytes();
	}
	
	@Override
	public <T> T unmarshall(byte[] bytes, Class<T> clazz) throws SerializationException
	{
		try
		{
			return gson.fromJson(new String(bytes), clazz);
		}
		catch(JsonSyntaxException e)
		{
			throw new SerializationException(e);
		}
	}
}
