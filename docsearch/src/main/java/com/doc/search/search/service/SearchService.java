package com.doc.search.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.amazonaws.services.cloudsearchdomain.model.SearchResult;
import com.doc.search.search.model.ResultData;
import com.doc.search.search.model.User;

@Service
public class SearchService {

	@Value("${aws.accesskey}")
	String accesskey;

	@Value("${aws.secretkey}")
	String secretkey;

	@Value("${aws.searchendpoint}")
	String searchEndpoint;

	@Value("${aws.searchregion}")
	String searchRegion;

	public List<ResultData> getSearchResult(String search) {
		ResultData searchResult = new ResultData();
		// Establish Connection with Cloud Search
		AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(searchEndpoint, searchRegion);
		AmazonCloudSearchDomain domain = AmazonCloudSearchDomainClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withEndpointConfiguration(endpointConfiguration).build();

		// Prepare Search Request based on parameter passed.
		SearchRequest searchReq = new SearchRequest().withQuery(search);
		// Get the Search result based on search parameter passed.
		SearchResult s_res = domain.search(searchReq);

		// Parse the JSON Object to get ANme city and dept
		JSONObject obj = new JSONObject(s_res);
		searchResult.setTotalCount(obj.getJSONObject("hits").get("found").toString());
		JSONArray arr = obj.getJSONObject("hits").getJSONArray("hit");
		ArrayList<User> userList = new ArrayList<User>();
		for (int i = 0; i < arr.length(); i++) {
			User user = new User();
			user.setId(arr.getJSONObject(i).get("id").toString());
			user.setName(arr.getJSONObject(i).getJSONObject("fields").get("name").toString());
			user.setDept(arr.getJSONObject(i).getJSONObject("fields").get("dept").toString());
			user.setCity(arr.getJSONObject(i).getJSONObject("fields").get("city").toString());
			userList.add(user);
		}
		searchResult.setUsers(userList);
		return Arrays.asList(searchResult);

	}

}
