package com.gregmarut.resty.util;

import com.gregmarut.resty.async.Async;

import java.util.List;

public class DummyObject
{
	public Async<String> genericString()
	{
		return null;
	}
	
	public Async<String[]> genericStringArray()
	{
		return null;
	}
	
	public Async<List<String>> genericStringList()
	{
		return null;
	}
	
	public void noReturn()
	{
	
	}
}
