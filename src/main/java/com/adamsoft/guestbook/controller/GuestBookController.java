package com.adamsoft.guestbook.controller;

import com.adamsoft.guestbook.dto.GuestBookDTO;
import com.adamsoft.guestbook.dto.PageRequestDTO;
import com.adamsoft.guestbook.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class GuestBookController {
    private final GuestBookService guestBookService;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping("/guestbook/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list.............");
        model.addAttribute("result", guestBookService.getList(pageRequestDTO));
    }

    //등록을 위한 메서드
    @GetMapping("/guestbook/register") public void register() {
        log.info("regiser get... ");
    }

    @PostMapping( "/guestbook/register")
    public String registerPost(GuestBookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto.••" + dto);
        //새로 추가된 Entity의 번호
        Long gno = guestBookService.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno + " 작성");
        return "redirect:/guestbook/list";
    }

    //@GetMapping("/guestbook/read")
    @GetMapping({"/guestbook/read", "/guestbook/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model ){
        log.info("gno: " + gno);
        GuestBookDTO dto = guestBookService.read(gno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/guestbook/modify")
    public String modify(GuestBookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){


        log.info("post modify.........................................");
        log.info("dto: " + dto);

        guestBookService.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addAttribute("gno",dto.getGno());


        return "redirect:/guestbook/read";

    }


    @PostMapping("/guestbook/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        log.info("gno: " + gno);
        guestBookService.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno + " 삭제");
        return "redirect:/guestbook/list";
    }


}
