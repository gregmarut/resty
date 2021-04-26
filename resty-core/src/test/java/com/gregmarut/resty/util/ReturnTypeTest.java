package com.gregmarut.resty.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ReturnTypeTest
{
	@Test
	public void checkAsyncTask() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericString");
		final ReturnType returnType = new ReturnType(method);
		Assert.assertTrue(returnType.isAsync());
	}
	
	@Test
	public void checkAsyncGenericString() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericString");
		final ReturnType returnType = new ReturnType(method);
		Assert.assertEquals(String.class, returnType.getGenericReturnType().map(ReturnType::getActualClass).get());
	}
	
	@Test
	public void checkAsyncGenericStringArray() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericStringArray");
		final ReturnType returnType = new ReturnType(method);
		Assert.assertEquals(String[].class, returnType.getGenericReturnType().map(ReturnType::getActualClass).get());
	}
	
	@Test
	public void checkAsyncGenericStringList() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericStringList");
		final ReturnType returnType = new ReturnType(method);
		final ReturnType genericReturnType = returnType.getGenericReturnType().get();
		Assert.assertEquals(List.class, genericReturnType.getActualClass());
		Assert.assertTrue(genericReturnType.isList());
		Assert.assertEquals(String.class, genericReturnType.getGenericReturnType().get().getActualClass());
	}
	
	@Test
	public void checkAsyncGenericByteArray() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericByteArray");
		final ReturnType returnType = new ReturnType(method);
		final ReturnType genericReturnType = returnType.getGenericReturnType().get();
		Assert.assertEquals(byte[].class, genericReturnType.getActualClass());
	}
	
	@Test
	public void checkByteArray() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("byteArray");
		final ReturnType returnType = new ReturnType(method);
		Assert.assertEquals(byte[].class, returnType.getActualClass());
	}
	
	@Test
	public void noReturnTest() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("noReturn");
		final ReturnType returnType = new ReturnType(method);
		Assert.assertEquals(void.class, returnType.getActualClass());
	}
}
