package com.sample.JsonDemo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
 
@Controller
public class MainController {
	String message = "Welcome to Spring MVC!";
 
	@RequestMapping("/hello")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
 
		ModelAndView mv = new ModelAndView("samplejson");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getjson", method = RequestMethod.POST)
	public String check(@RequestParam(value = "jsonStr", required = true) String jsonStr, HttpServletRequest request, HttpServletResponse response, Model model) {
	    System.out.println("jsonStr"+ jsonStr);
	    return jsonStr;
	}
	
	@RequestMapping("/aws")
	public ModelAndView awsCall() throws IOException {
		System.out.println("in controller");
		ModelAndView mv = new ModelAndView("samplejson");
		AWSCredentials credentials = new BasicAWSCredentials("acceskeyID", "accessKey");
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_2)
				  .build();
		List<Bucket> buckets = s3client.listBuckets();
		Map<String, InputStream> fileContents = new HashMap<>();
		for(Bucket bucket : buckets) {
		    System.out.println(bucket.getName());
		    ObjectListing objectListing = s3client.listObjects(bucket.getName());
			for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
				System.out.println(os.getKey());
				GetObjectRequest getObjectRequest = new GetObjectRequest(bucket.getName(), os.getKey());
				InputStream inputStream = s3client.getObject(getObjectRequest).getObjectContent();
				fileContents.put(os.getKey(), inputStream);
			}
		}
		System.out.println("FileContents: "+fileContents); 
		for(String key : fileContents.keySet()) {
			System.out.println("\nKey: "+key+"\nContent:"+IOUtils.toString(fileContents.get(key), "UTF-8"));
		}
		mv.addObject("message", message);
		return mv;
	}
}
