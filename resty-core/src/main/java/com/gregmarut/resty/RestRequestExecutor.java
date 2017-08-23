package com.gregmarut.resty;

import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;

public interface RestRequestExecutor
{
	/**
	 * Executes a rest request
	 *
	 * @param restRequest
	 * @return
	 * @throws WebServiceException
	 */
	RestResponse executeRequest(RestRequest restRequest) throws WebServiceException;
}
