package com.riffert.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.riffert.textgroup.entity.Domain;
import com.riffert.textgroup.entity.Equivalence;
import com.riffert.textgroup.entity.Group;
import com.riffert.textgroup.entity.Text;
import com.riffert.textgroup.service.DatabaseRequestService;

@Controller
public class EditController
{
		@Autowired
		private DatabaseRequestService databaseRequestService;
		
		@RequestMapping(value="/add")
		public String add(Model model,
				@RequestParam(defaultValue="1")Domain domain,
				@RequestParam(defaultValue="1")Group group,
				@RequestParam(defaultValue="0")int currentpage,
				@RequestParam(defaultValue="0")Long userId)
		{
				List<Group> groups = databaseRequestService.getGroups(domain,group);
				
				if (userId == 0)
					userId = domain.getNextEquivalenceId();
					
				
				model.addAttribute("userId", userId);
				model.addAttribute("domain", domain);
				model.addAttribute("group", group);
				model.addAttribute("currentpage", currentpage);
				model.addAttribute("groups", groups);
				
				return "add";
		}

		
		@RequestMapping(value="/remove")
		public String delete(Model model,
				@RequestParam(defaultValue="0",name="equivalenceId")Long equivalenceId,
				@RequestParam(defaultValue="1")Domain domain)
		{
				if (equivalenceId != 0)
				{
						databaseRequestService.removeEquivalence(equivalenceId);
				}
				
				return "/?domain="+domain.getId();
		}
		
		@RequestMapping(value="/edit")
		public String edit(Model model,@RequestParam(defaultValue="1")Equivalence equivalence,
				@RequestParam(defaultValue="0")int currentpage,
				@RequestParam(defaultValue="1")Domain domain)
		{
				List<Group> groups = databaseRequestService.getGroups(domain,new Group());
				List<Text> texts = equivalence.getTexts();
			
				for(Group grp:groups)
				{
						if (!isGroupPresent(grp,texts))
						{
							Text text = new Text("");
							text.setEquivalence(equivalence);
							text.setGroup(grp);
							text.setDomain(domain);
							grp.add(text);
							
							databaseRequestService.addText(text, equivalence, grp);
						}
				}
				
				texts = equivalence.getTexts();
				
				model.addAttribute("texts", texts);
				model.addAttribute("currentpage", currentpage);
				model.addAttribute("equivalence", equivalence);
				model.addAttribute("domain",domain);
				
				return "edit";
		}
		
		private boolean isGroupPresent(Group group,List<Text> texts)
		{
				for(Text text:texts)
					if (group == text.getGroup())
						return true;

				return false;
		}
		
	
		@RequestMapping(value="/save",method=RequestMethod.POST)
		public String save(Model model,@RequestParam Map<String, String> params,
						@RequestParam(defaultValue="1")Domain domain,
						@RequestParam(defaultValue="1")Group group,
						@RequestParam(defaultValue="0")int currentpage,
						@RequestParam(defaultValue="0")String userId)
		{		
				Equivalence equivalence = databaseRequestService.getNewEquivalence(group);
				List<Group> groups = databaseRequestService.getGroups(domain,group);
				
				for (Group groop:groups)
				{
						String text = params.get(groop.getName());
						databaseRequestService.addText(new Text(text), equivalence,groop);
				}
				
				Long nUserId;
				
				if (userId.equals("0"))
				{
					nUserId = domain.getNextEquivalenceId();
					domain.incrementNextEquivalenceId();
				}
				else
				{
					System.out.println("** userId :"+userId);
					nUserId = Long.parseLong(userId);
				}
				
				equivalence.setUserId(nUserId);
				
				databaseRequestService.save(equivalence, domain);
			
				return "redirect:/?domain="+domain.getId()+"&currentpage="+currentpage;
		}

		@RequestMapping(value="/update",method=RequestMethod.POST)
		public String update(@RequestParam Map<String, String> params)
		{		
				for (String key : params.keySet())
				{
						//System.out.println("key : "+key+", value : "+params.get(key) );
						if ( !(key.equals("domain") || key.equals("currentpage")))
							databaseRequestService.updateText(Long.parseLong(key), params.get(key));
				}
				
				String currentpage = params.get("currentpage");
				String domain = params.get("domain");
				
				if (currentpage != null && domain != null)
					return "redirect:/?domain="+domain+"&currentpage="+currentpage;
				else
					return "redirect:/";
		}

}
