package com.study.BoardProject.controller;

import com.study.BoardProject.data.entity.BoardEntity;
import com.study.BoardProject.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/board")
@Controller
public class BoardController {

    Logger LOGGER = LoggerFactory.getLogger(BoardController.class);
    private BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/write")
    public String boardWrite(){
        LOGGER.info("[boardWrite] 호출");
        return "boardwrite";
    }

    @PostMapping("/writepro")
    public String boardWrite(BoardEntity boardEntity,Model model, MultipartFile multipartFile) throws Exception{
        LOGGER.info("[boardWritepro] 호출");
        LOGGER.info("[boardWritepro] title : {}",boardEntity.toString());
        LOGGER.info("[boardWritepro] content : {}",boardEntity.toString());

        //입력받은 내용 DB에 저장
        boardService.write(boardEntity,multipartFile);

        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }

    @GetMapping("/list")
    public String boardlist(Model model,
        @PageableDefault(page = 0,size = 10,sort = "id",direction = Direction.DESC) Pageable pageable,String strSearch){ //page 현재페이지 size - 한페이지 항목수, direction 정렬순서
        LOGGER.info("[boardlist] 호출");
        LOGGER.info("[boardlist] strSearch : {}",strSearch);
        Page<BoardEntity> page;
        if(strSearch == null) {
            page = boardService.getBoardList(pageable);
        }else{
            page = boardService.boardSearch(strSearch,pageable);
        }
        int nowPgae = page.getPageable().getPageNumber()+1;
        int startPage = Math.max( nowPgae-4,1);
        int endPage=Math.min( nowPgae+4,page.getTotalPages());;

        model.addAttribute("list",page);
        model.addAttribute("nowPage",nowPgae);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "boardlist";
    }

    @GetMapping("/view")
    public String boardview(Model model,Integer id){
        LOGGER.info("[boardview] 호출");
        model.addAttribute("boardentity",boardService.getBoardView(id));
        return "boardview";
    }

    @GetMapping("/delete")
    public String boarddelete(Integer id){
        LOGGER.info("[boarddelete] 호출");
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/modify/{id}")
    public String boardmodify(@PathVariable("id") Integer id,Model model){
        LOGGER.info("[boardmodify] 호출");
        model.addAttribute("boardentity",boardService.getBoardView(id));
        return "boardmodify";
    }

    @PostMapping("/update/{id}")
    public String boardupdate(@PathVariable("id") Integer id,BoardEntity boardEntity,MultipartFile multipartFile) throws  Exception{
        LOGGER.info("[boardupdate] 호출");

        BoardEntity temp = boardService.getBoardView(id);
        temp.setTitle(boardEntity.getTitle());
        temp.setContent(boardEntity.getContent());
        temp.setScore(boardEntity.getScore());
        temp.setHit_cnt(boardEntity.getHit_cnt());
        temp.setLike_cnt(boardEntity.getLike_cnt());
        temp.setDislike_cnt(boardEntity.getDislike_cnt());

        boardService.write(temp,multipartFile);

        return "redirect:/board/list";
    }
}
