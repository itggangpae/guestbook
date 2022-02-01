package com.adamsoft.guestbook.service;

import com.adamsoft.guestbook.dto.GuestBookDTO;
import com.adamsoft.guestbook.dto.PageRequestDTO;
import com.adamsoft.guestbook.dto.PageResponseDTO;
import com.adamsoft.guestbook.entity.GuestBook;

public interface GuestBookService {
    public Long register(GuestBookDTO dto);

    //데이터 목록을 가져오는 메서드
    PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);

    GuestBookDTO read(Long gno);

    void modify(GuestBookDTO dto);

    void remove(Long gno);

    default GuestBook dtoToEntity(GuestBookDTO dto) {
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestBookDTO entityToDto(GuestBook entity){
        GuestBookDTO dto  = GuestBookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }


}

