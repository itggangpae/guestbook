package com.adamsoft.guestbook.service;

import com.adamsoft.guestbook.dto.GuestBookDTO;
import com.adamsoft.guestbook.dto.PageRequestDTO;
import com.adamsoft.guestbook.dto.PageResponseDTO;
import com.adamsoft.guestbook.entity.GuestBook;
import com.adamsoft.guestbook.entity.QGuestBook;
import com.adamsoft.guestbook.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입
public class GuestBookServiceImpl implements GuestBookService{
    private final GuestBookRepository repository;

    @Override
    public Long register(GuestBookDTO dto) {
        log.info("DTO---------------------------------------");
        log.info(dto);
        GuestBook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }

    //데이터 목록을 가져오는 메서드
    @Override
    public PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO);
        Page<GuestBook> result = repository.findAll(booleanBuilder, pageable);
        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));
        return new PageResponseDTO<>(result, fn );
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<GuestBook> guestBook = repository.findById(gno);
        return guestBook.isPresent()? entityToDto(guestBook.get()): null;
    }

    @Override
    public void modify(GuestBookDTO dto) {
        //업데이트 하는 항목은 '제목', '내용'
        Optional<GuestBook> result = repository.findById(dto.getGno());
        if(result.isPresent()){
            GuestBook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;
        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qGuestBook.gno.gt(0L); // gno > 0 조건만 생성
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){ //검색 조건이 없는 경우
            return booleanBuilder;
        }
        //검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

}

