package com.gregmarut.resty.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class UploadController
{
	@PostMapping
	public byte[] upload(@RequestParam("file") final MultipartFile file)
	{
		return file.getOriginalFilename().getBytes();
	}
}
