package com.study.BoardProject.service;

import com.study.BoardProject.data.entity.BoardEntity;
import com.study.BoardProject.data.repository.BoardRepository;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void write(BoardEntity boardEntity, MultipartFile multipartFile) throws Exception{
        String fileSavePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files"; //프로젝트패스

        UUID uuid = UUID.randomUUID(); //랜덤식별자이름 생성.

        String filename = uuid + "_" + multipartFile.getOriginalFilename(); //랜덤 식별자 이름 + 파일명

        File saveFile= new File(fileSavePath,filename); //파일경로,이름지정
        multipartFile.transferTo(saveFile); //파일 저장

        boardEntity.setFilename(filename); //파일명 설정
        boardEntity.setFilepath("/files/"+filename);//파일패스 설정


        boardRepository.save(boardEntity);
    }

    public Page<BoardEntity> getBoardList(Pageable pageable){

        return boardRepository.findAll(pageable);
    }

    public BoardEntity getBoardView(Integer id){

        return boardRepository.findById(id).get();
    }
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }

    public Page<BoardEntity> boardSearch(String strSearch,Pageable pageable){
        return boardRepository.findByTitleContaining(strSearch,pageable);
    }
}
