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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Greg Marut
 * @param <T>
 */
public class DefaultSerializer implements Serializer
{
	/**
	 * Converts a Serializable object to a byte array
	 * 
	 * @param serializable
	 * @return
	 */
	@Override
	public byte[] marshall(Object object) throws SerializationException
	{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		
		try
		{
			//assign the output streams
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			
			// return bytes of the serialized object
			return baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new SerializationException(e);
		}
		finally
		{
			//attempt to close the output stream
			if(null != oos)
			{
				try
				{
					oos.close();
				}
				catch (IOException e)
				{
					//ignore this exception
				}
			}
			
			//attempt to close the output stream
			if(null != baos)
			{
				try
				{
					baos.close();
				}
				catch (IOException e)
				{
					//ignore this exception
				}
			}
		}
	}
	
	/**
	 * Restores an object to its original object from a serialized array of
	 * bytes
	 * 
	 * @param bytes
	 * @param clazz
	 * @return
	 * @throws SerializationException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T unmarshall(byte[] bytes, Class<T> clazz) throws SerializationException
	{
		// holds the object to return of type E
		T result;
		
		try
		{
			// decode the body
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			result = (T) ois.readObject();
			ois.close();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new SerializationException(e);
		}
		catch (IOException e)
		{
			throw new SerializationException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new SerializationException(e);
		}
		
		// return the restored object
		return result;
	}
}
