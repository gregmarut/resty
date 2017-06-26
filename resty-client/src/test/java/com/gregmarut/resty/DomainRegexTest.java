package com.gregmarut.resty;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class DomainRegexTest
{
	private final Pattern domainPattern = Pattern.compile(RestInvocationHandler.REGEX_DOMAIN_URL);
	
	@Test
	public void absoluteUriTest()
	{
		Assert.assertTrue(domainPattern.matcher("http://www.example.com").find());
		Assert.assertTrue(domainPattern.matcher("http://www.example.com/").find());
		Assert.assertTrue(domainPattern.matcher("http://www.example.com/rest").find());
	}
	
	@Test
	public void relativeUriTest()
	{
		Assert.assertFalse(domainPattern.matcher("rest/test").find());
		Assert.assertFalse(domainPattern.matcher("/rest/test").find());
		Assert.assertFalse(domainPattern.matcher("/rest/test?http://www.example.com").find());
		Assert.assertFalse(domainPattern.matcher("/rest/test/?http://www.example.com").find());
		Assert.assertFalse(domainPattern.matcher("/rest/test/?http://www.example.com/rest").find());
	}
}
