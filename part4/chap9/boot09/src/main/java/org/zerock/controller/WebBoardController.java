package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.WebBoard;
import org.zerock.persistence.CustomCrudRepository;
import org.zerock.vo.PageMaker;
import org.zerock.vo.PageVO;

@Controller
@RequestMapping("/boards/")
public class WebBoardController {

	@Autowired
	//private WebBoardRepository repo;
	private CustomCrudRepository repo;
	
	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");
		
		//Page<WebBoard> result = repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
		Page<Object[]> result = repo.getCustomPage(vo.getType(), vo.getKeyword(), page);
		
		System.out.println(page);
		System.out.println(result);
		
		model.addAttribute("result", new PageMaker<>(result));
	}
	
	@GetMapping("/register")
	public void registerGet(@ModelAttribute("vo")WebBoard vo) {
		System.out.println("register get");
	}
	
	@PostMapping("/register")
	public String registerPost(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
		repo.save(vo);
		rttr.addFlashAttribute("msg","success");
		
		return "redirect:/boards/list";
	}
	
	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}
	
	@Secured(value= {"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
	@GetMapping("/modify")
	public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo",board));
	}
	
	@Secured(value= {"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
	@PostMapping("/delete")
	public String delete(Long bno, PageVO vo, RedirectAttributes rttr) {
		repo.deleteById(bno);
		
		rttr.addFlashAttribute("msg","success");
		
		// 페이징과 검색했던 결과로 이동하는 경우
		rttr.addAttribute("page",vo.getPage());
		rttr.addAttribute("size",vo.getSize());
		rttr.addAttribute("type",vo.getType());
		rttr.addAttribute("keyword",vo.getKeyword());
		
		return "redirect:/boards/list";
	}
	
	@Secured(value= {"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
	@PostMapping("/modify")
	public String modifyPost(WebBoard board, PageVO vo, RedirectAttributes rttr) {
		
		repo.findById(board.getBno()).ifPresent(origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());
			
			repo.save(origin);
			rttr.addFlashAttribute("msg","success");
			rttr.addAttribute("bno",origin.getBno());
		});
		
		// 페이징과 검색했던 결과로 이동하는 경우
		rttr.addAttribute("page",vo.getPage());
		rttr.addAttribute("size",vo.getSize());
		rttr.addAttribute("type",vo.getType());
		rttr.addAttribute("keyword",vo.getKeyword());
		
		return "redirect:/boards/view";
	}
}
