package com.riffert.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.riffert.bootstrap.Nav;
import com.riffert.bootstrap.Treeview;
import com.riffert.node.Node;
import com.riffert.node.TextNode;
import com.riffert.textgroup.entity.Domain;
import com.riffert.textgroup.entity.Equivalence;
import com.riffert.textgroup.entity.Group;
import com.riffert.textgroup.entity.Text;
import com.riffert.textgroup.service.DatabaseRequestService;

@Controller
public class RootController
{
		@Autowired
		private DatabaseRequestService databaseRequestService;
		
		@RequestMapping(value="/")
		public String index(Model model,
				@RequestParam(defaultValue="0")int currentpage,
				@RequestParam(defaultValue="")String keyword,
				@RequestParam(defaultValue="5")int pagesize,
				@RequestParam(defaultValue="1")Domain domain,
				@RequestParam(defaultValue="1")Group group)
		{
			
				List<Domain> domains = databaseRequestService.getDomains(domain);
				
				List<Group> groups = databaseRequestService.getGroups(domain,group);
				
				if ( groups != null && groups.size() > 0 )
				{
						Page<Text> pageTexts = databaseRequestService.getPage(group, keyword, currentpage, pagesize);
						
						Nav[] navs = null;
						
						int pagesCount = pageTexts.getTotalPages();
						
						navs = new Nav[pagesCount];
						
						for (int i=0;i<pagesCount;i++){
							navs[i] = new Nav(i,currentpage);
						}
						
						Treeview treeview = new Treeview();
						
						for (Text text:pageTexts)
						{
								Equivalence equivalence = text.getEquivalence();
								
								TextNode textnode = new TextNode(text.getValue(), equivalence.getId()+"");
								Node node = treeview.addNode(textnode);
								
								List<Text> texts = equivalence.getTexts();
								
								for (Text txt:texts)
								{
										String value = txt.getValue();
										equivalence = txt.getEquivalence();
										
										if (txt.getGroup() != group) {
											node.addChild(new TextNode(value, equivalence.getId()+""));
										}
								}
						}
						
						treeview.validate();  // mandatory
						
						model.addAttribute("pageTexts", pageTexts);
						model.addAttribute("treeview",treeview);
						model.addAttribute("navs", navs);		
				}

				model.addAttribute("currentpage", currentpage);
				model.addAttribute("keyword",keyword);
				model.addAttribute("pagesize",pagesize);
				
				model.addAttribute("domains", domains);
				model.addAttribute("groups", groups);
				
				model.addAttribute("group", group);
				
				model.addAttribute("domain", domain.getId());
				
				return "list";
		}




		@RequestMapping(value="/delete")
		public String remove(@RequestParam(defaultValue="0")Text text)
		{
				databaseRequestService.deleteText(text);
				return "redirect:/";
		}

		
}
