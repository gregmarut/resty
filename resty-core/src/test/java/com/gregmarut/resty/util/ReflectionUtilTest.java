package com.gregmarut.resty.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class ReflectionUtilTest
{
	@Test
	public void extractFirstGenericClassTest() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericString");
		final Class<?> clazz = ReflectionUtil.extractFirstGenericClass(method).orElse(null);
		Assert.assertEquals(String.class, clazz);
	}
	
	@Test
	public void extractFirstGenericClassTest2() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("genericStringArray");
		final Class<?> clazz = ReflectionUtil.extractFirstGenericClass(method).orElse(null);
		Assert.assertEquals(String[].class, clazz);
	}
}