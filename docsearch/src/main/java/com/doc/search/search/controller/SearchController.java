package com.doc.search.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.doc.search.search.service.SearchService;

@Controller
public class SearchController {
	
	@Autowired
	SearchService searchService;

	@RequestMapping(value = "/search_user", method = RequestMethod.POST)
	public String showAddCategoryPage(ModelMap model, @RequestParam("search") final String search) {
		model.put("searchresult", searchService.getSearchResult(search));
		return "index";
	}

}
