package com.gregmarut.resty;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class ReflectionUtilTest
{
	@Test
	public void extractFirstGenericClassTest() throws NoSuchMethodException
	{
		final Method method = DummyObject.class.getDeclaredMethod("listMethod");
		final Class<?> clazz = ReflectionUtil.extractFirstGenericClass(method).orElse(null);
		Assert.assertEquals(String.class, clazz);
	}
}